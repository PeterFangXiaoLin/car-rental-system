<template>
  <div id="vehicleDetailPage">
    <el-row :gutter="16" class="mb-4">
      <!--      左侧区域：车辆图片和评论区-->
      <el-col :sm="14" :lg="16">
        <!-- 车辆图片 -->
        <el-card header="车辆图片" class="mb-4">
          <div v-loading="loading">
            <el-image :src="vehicle?.imageUrl" fit="contain" style="width: 100%" />
          </div>
        </el-card>

        <!-- 评论区 -->
        <el-card header="用户评价">
          <div v-loading="commentsLoading">
            <div v-if="comments.length > 0">
              <div
                v-for="comment in comments"
                :key="comment.id"
                class="comment-item py-3 border-b border-gray-200"
              >
                <el-row>
                  <el-col :span="2">
                    <el-avatar :src="comment.userAvatar" :size="50">
                      {{ comment.userName?.charAt(0) }}
                    </el-avatar>
                  </el-col>
                  <el-col :span="22">
                    <div class="flex justify-between">
                      <div class="font-bold">{{ comment.userName }}</div>
                      <div class="text-gray-500 text-sm">
                        {{ dayjs(comment.createTime).format('YYYY-MM-DD HH:mm:ss') }}
                      </div>
                    </div>
                    <div class="my-2">
                      <el-rate
                        v-model="comment.vehicleRating"
                        disabled
                        show-score
                        text-color="#ff9900"
                      />
                    </div>
                    <div class="my-2">{{ comment.content }}</div>
                    <div v-if="comment.images" class="mt-2 flex gap-2">
                      <el-image
                        v-for="(img, index) in comment.images.split(',')"
                        :key="index"
                        :src="img"
                        style="width: 100px; height: 100px; object-fit: cover; border-radius: 4px"
                        :preview-src-list="comment.images.split(',')"
                      />
                    </div>

                    <!-- 回复按钮 -->
                    <div class="mt-2 text-right">
                      <el-button type="primary" link @click="showReplyForm(comment.id)">
                        回复
                      </el-button>
                    </div>

                    <!-- 回复表单 -->
                    <div
                      v-if="activeReplyCommentId == comment.id"
                      class="mt-2 bg-gray-50 p-3 rounded"
                    >
                      <el-input
                        v-model="replyContent"
                        type="textarea"
                        rows="2"
                        placeholder="请输入回复内容..."
                        class="mb-2"
                      />
                      <div class="text-right">
                        <el-button @click="cancelReply">取消</el-button>
                        <el-button
                          type="primary"
                          @click="submitReply(comment.id)"
                          :loading="replyLoading"
                        >
                          提交回复
                        </el-button>
                      </div>
                    </div>

                    <!-- 回复列表 -->
                    <div
                      v-if="comment.replyList && comment.replyList.length > 0"
                      class="mt-3 bg-gray-50 p-3 rounded"
                    >
                      <div
                        v-for="reply in comment.replyList"
                        :key="reply.id"
                        class="reply-item py-2"
                      >
                        <div class="flex gap-2">
                          <el-avatar :src="reply.userAvatar" :size="30">
                            {{ reply.userName?.charAt(0) }}
                          </el-avatar>
                          <div class="flex-1">
                            <div class="flex justify-between">
                              <div>
                                <span class="font-bold">{{ reply.userName }}</span>
                                <span v-if="reply.replyToUserName" class="text-gray-500 mx-1">
                                  回复 <span class="text-primary">{{ reply.replyToUserName }}</span>
                                </span>
                              </div>
                              <div class="text-gray-500 text-xs">
                                {{ dayjs(reply.createTime).format('YYYY-MM-DD HH:mm:ss') }}
                              </div>
                            </div>
                            <div class="mt-1">{{ reply.content }}</div>
                            <div v-if="reply.images" class="mt-1 flex gap-2">
                              <el-image
                                v-for="(img, index) in reply.images.split(',')"
                                :key="index"
                                :src="img"
                                style="
                                  width: 80px;
                                  height: 80px;
                                  object-fit: cover;
                                  border-radius: 4px;
                                "
                                :preview-src-list="reply.images.split(',')"
                              />
                            </div>
                            <!-- 回复的回复按钮 -->
                            <div class="mt-1 text-right">
                              <el-button
                                type="primary"
                                link
                                @click="showReplyForm(comment.id, reply.userId, reply.userName)"
                              >
                                回复
                              </el-button>
                            </div>
                          </div>
                        </div>
                      </div>
                    </div>
                  </el-col>
                </el-row>
              </div>

              <!-- 分页控件 -->
              <div class="mt-4 flex justify-center">
                <el-pagination
                  v-model:current-page="commentPagination.current"
                  v-model:page-size="commentPagination.pageSize"
                  :page-sizes="[5, 10, 20, 50]"
                  layout="total, sizes, prev, pager, next, jumper"
                  :total="commentPagination.total"
                  @size-change="handleCommentSizeChange"
                  @current-change="handleCommentCurrentChange"
                />
              </div>
            </div>
            <el-empty v-else description="暂无评价" />
          </div>
        </el-card>
      </el-col>

      <!--      右侧区域：车辆信息和猜你喜欢-->
      <el-col :sm="10" :lg="8">
        <!-- 车辆信息 -->
        <el-card header="车辆信息" class="mb-4">
          <el-descriptions :column="1" v-loading="loading" border>
            <el-descriptions-item label="名称">
              {{ vehicle?.name ?? '未命名' }}
            </el-descriptions-item>
            <el-descriptions-item label="描述">
              {{ vehicle?.description ?? '-' }}
            </el-descriptions-item>
            <el-descriptions-item label="车牌号">
              {{ vehicle?.vehicleNo ?? '-' }}
            </el-descriptions-item>
            <el-descriptions-item label="品牌">
              {{ vehicle?.brandName ?? '-' }}
            </el-descriptions-item>
            <el-descriptions-item label="系列">
              {{ vehicle?.modelName ?? '-' }}
            </el-descriptions-item>
            <el-descriptions-item label="车型">
              {{ vehicle?.vehicleTypeName ?? '-' }}
            </el-descriptions-item>
            <el-descriptions-item label="能源类型">
              {{ vehicle?.energyTypeName ?? '-' }}
            </el-descriptions-item>
            <el-descriptions-item label="生产年份">
              {{ vehicle?.productionYear ?? '-' }}
            </el-descriptions-item>
            <el-descriptions-item label="日租金">
              <div class="color-#f56c6c">{{ vehicle?.dailyPrice ?? '-' }} 元/天</div>
            </el-descriptions-item>
            <el-descriptions-item label="座位数">
              {{ vehicle?.seatCount ?? '-' }}
              <span v-if="vehicle?.seatCount">座</span>
            </el-descriptions-item>
            <el-descriptions-item label="状态">
              <el-tag v-if="vehicle?.status === VehicleStatusEnum.AVAILABLE"> 可租</el-tag>
              <el-tag v-else-if="vehicle?.status === VehicleStatusEnum.RENTED"> 已租出</el-tag>
              <el-tag v-else-if="vehicle?.status === VehicleStatusEnum.MAINTENANCE">
                维修中
              </el-tag>
              <el-tag v-else-if="vehicle?.status === VehicleStatusEnum.SCRAPPED"> 报废</el-tag>
            </el-descriptions-item>
          </el-descriptions>

          <!-- 下单 -->
          <el-space class="mt-4 flex justify-center items-center">
            <el-button type="primary" @click="handleOrder">
              <el-icon>
                <ShoppingCart />
              </el-icon>
              立即下单
            </el-button>

            <el-button
              :type="isFavorite ? 'danger' : 'default'"
              @click="toggleFavorite"
              :loading="favoriteLoading"
            >
              <el-icon>
                <Star />
              </el-icon>
              {{ isFavorite ? '取消收藏' : '收藏' }}
            </el-button>
          </el-space>
        </el-card>

        <!-- 猜你喜欢 -->
        <el-card header="猜你喜欢" v-loading="recommendLoading">
          <div v-if="recommendedVehicles.length > 0">
            <el-scrollbar height="400px">
              <div
                v-for="item in recommendedVehicles"
                :key="item.id"
                class="recommend-item mb-3 cursor-pointer"
                @click="goToVehicleDetail(item)"
              >
                <el-row :gutter="8">
                  <el-col :span="8">
                    <el-image
                      :src="item.imageUrl"
                      fit="cover"
                      style="width: 100%; height: 60px; border-radius: 4px"
                    />
                  </el-col>
                  <el-col :span="16">
                    <div class="text-sm font-bold truncate">{{ item.name }}</div>
                    <div class="text-xs text-gray-500 truncate">
                      {{ item.brandName }} {{ item.modelName }}
                    </div>
                    <div class="text-xs color-#f56c6c font-bold">¥{{ item.dailyPrice }}/天</div>
                  </el-col>
                </el-row>
              </div>
            </el-scrollbar>
          </div>
          <el-empty v-else description="暂无推荐车辆" />
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useLoginUserStore } from '@/stores/useLoginUserStore.ts'
import { ElMessage } from 'element-plus'
import { ShoppingCart, Star } from '@element-plus/icons-vue'
import { useRouter } from 'vue-router'
import { getVehicleByIdUsingGet, recommendVehicleUsingGet } from '@/api/vehicleController.ts'
import VehicleStatusEnum from '@/enums/VehicleStatusEnum.ts'
import {
  addVehicleFavoriteUsingPost,
  cancelVehicleFavoriteUsingPost,
  checkVehicleFavoriteUsingGet,
} from '@/api/vehicleFavoriteController.ts'
import { addOrUpdateBrowsHistoryUsingPost } from '@/api/vehicleBrowsingHistoryController.ts'
import { pageCommentByVehicleIdUsingPost } from '@/api/commentController.ts'
import { addCommentReplyUsingPost } from '@/api/commentReplyController.ts'
import dayjs from 'dayjs'
import { watch } from 'vue'

interface Props {
  id: string | number
}

const router = useRouter()
const loginUserStore = useLoginUserStore()

const props = defineProps<Props>()
const vehicle = ref<API.VehicleVO>()
const loading = ref(false)
const isFavorite = ref(false)
const favoriteLoading = ref(false)

// 评论相关
const comments = ref<API.CommentVO[]>([])
const commentsLoading = ref(false)
const commentPagination = ref({
  current: 1,
  pageSize: 10,
  total: 0,
})

// 回复相关
const activeReplyCommentId = ref<string | null>(null)
const replyToUserId = ref<string | null>(null)
const replyToUserName = ref<string | null>(null)
const replyContent = ref('')
const replyLoading = ref(false)

// 推荐车辆相关
const recommendedVehicles = ref<API.VehicleVO[]>([])
const recommendLoading = ref(false)

// 获取车辆详情
const fetchPictureDetail = async () => {
  loading.value = true
  try {
    const res = await getVehicleByIdUsingGet({ id: props.id })
    if (res.data.code === 0 && res.data.data) {
      vehicle.value = res.data.data
    } else {
      ElMessage.error('获取车辆详情失败：' + res.data.message)
    }
  } catch (error) {
    ElMessage.error('获取车辆详情失败：' + error)
  } finally {
    loading.value = false
  }
}

// 获取评论列表
const fetchComments = async () => {
  commentsLoading.value = true
  try {
    const res = await pageCommentByVehicleIdUsingPost({
      vehicleId: props.id,
      current: commentPagination.value.current,
      pageSize: commentPagination.value.pageSize,
    })
    if (res.data.code === 0 && res.data.data) {
      comments.value = res.data.data.records || []
      commentPagination.value.total = Number(res.data.data.total) || 0
    } else {
      ElMessage.error('获取评论失败：' + res.data.message)
    }
  } catch (error) {
    ElMessage.error('获取评论失败：' + error)
  } finally {
    commentsLoading.value = false
  }
}

// 显示回复表单
const showReplyForm = (commentId: string, userId?: string, userName?: string) => {
  // 判断是否登录
  if (!loginUserStore.loginUser?.id || loginUserStore.loginUser?.userName === '未登录') {
    ElMessage.warning('请先登录后再进行回复操作')
    // 跳转到登录页，并设置登录成功后的回调地址为当前页面
    router.push({
      path: '/auth/login',
      query: {
        redirect: router.currentRoute.value.fullPath,
      },
    })
    return
  }

  activeReplyCommentId.value = commentId
  replyToUserId.value = userId || null
  replyToUserName.value = userName || null
  replyContent.value = ''
}

// 取消回复
const cancelReply = () => {
  activeReplyCommentId.value = null
  replyToUserId.value = null
  replyToUserName.value = null
  replyContent.value = ''
}

// 提交回复
const submitReply = async (commentId: string) => {
  if (!replyContent.value.trim()) {
    ElMessage.warning('回复内容不能为空')
    return
  }

  replyLoading.value = true
  try {
    const replyData = {
      commentId,
      content: replyContent.value,
      replyToUserId: replyToUserId.value || undefined,
    }

    const res = await addCommentReplyUsingPost(replyData)
    if (res.data.code === 0 && res.data.data) {
      ElMessage.success('回复成功')
      // 刷新评论列表以获取新的回复
      await fetchComments()
      // 清空回复表单
      cancelReply()
    } else {
      ElMessage.error(res.data.message || '回复失败')
    }
  } catch (error) {
    ElMessage.error('回复失败：' + error)
  } finally {
    replyLoading.value = false
  }
}

// 获取推荐车辆
const fetchRecommendedVehicles = async () => {
  recommendLoading.value = true
  try {
    const res = await recommendVehicleUsingGet({ vehicleId: props.id })
    if (res.data.code === 0 && res.data.data) {
      // 过滤掉当前车辆
      recommendedVehicles.value = res.data.data.filter((item) => item.id != props.id).slice(0, 5)
    } else {
      ElMessage.error('获取推荐车辆失败：' + res.data.message)
    }
  } catch (error) {
    ElMessage.error('获取推荐车辆失败：' + error)
  } finally {
    recommendLoading.value = false
  }
}

// 跳转到车辆详情页
const goToVehicleDetail = (vehicle: API.VehicleVO) => {
  if (vehicle.id) {
    router.push(`/vehicle/${vehicle.id}`)
  }
}

// 记录浏览历史
const recordBrowsingHistory = async () => {
  // 如果用户已登录，记录浏览历史
  if (loginUserStore.loginUser?.id) {
    try {
      await addOrUpdateBrowsHistoryUsingPost({ vehicleId: props.id })
    } catch (error) {
      ElMessage.error('记录浏览历史失败：' + error)
    }
  }
}

// 检查收藏状态
const checkFavoriteStatus = async () => {
  // 如果用户已登录，检查收藏状态
  if (loginUserStore.loginUser?.id) {
    try {
      const res = await checkVehicleFavoriteUsingGet({ vehicleId: props.id })
      if (res.data.code === 0) {
        isFavorite.value = !!res.data.data
      }
    } catch (error) {
      ElMessage.error('检查收藏状态失败：' + error)
    }
  }
}

// 切换收藏状态
const toggleFavorite = async () => {
  // 判断是否登录
  if (!loginUserStore.loginUser?.id || loginUserStore.loginUser?.userName === '未登录') {
    ElMessage.warning('请先登录后再进行收藏操作')
    // 跳转到登录页，并设置登录成功后的回调地址为当前页面
    router.push({
      path: '/auth/login',
      query: {
        redirect: router.currentRoute.value.fullPath,
      },
    })
    return
  }

  favoriteLoading.value = true
  try {
    if (isFavorite.value) {
      // 已收藏，取消收藏
      const res = await cancelVehicleFavoriteUsingPost({
        vehicleId: props.id,
      })
      if (res.data.code === 0) {
        isFavorite.value = false
        ElMessage.success('取消收藏成功')
      } else {
        ElMessage.error('取消收藏失败：' + res.data.message)
      }
    } else {
      // 未收藏，添加收藏
      const res = await addVehicleFavoriteUsingPost({ vehicleId: props.id })
      if (res.data.code === 0) {
        isFavorite.value = true
        ElMessage.success('收藏成功')
      } else {
        ElMessage.error('收藏失败：' + res.data.message)
      }
    }
  } catch (error) {
    ElMessage.error('操作失败：' + error)
  } finally {
    favoriteLoading.value = false
  }
}

// 处理下单操作
const handleOrder = () => {
  // 判断是否登录
  if (!loginUserStore.loginUser?.id || loginUserStore.loginUser?.userName === '未登录') {
    ElMessage.warning('请先登录后再进行下单操作')
    // 跳转到登录页，并设置登录成功后的回调地址为当前页面
    router.push({
      path: '/auth/login',
      query: {
        redirect: router.currentRoute.value.fullPath,
      },
    })
    return
  }

  if (vehicle.value?.status !== VehicleStatusEnum.AVAILABLE) {
    ElMessage.error('该车辆不可租')
    return
  }

  // 已登录，跳转到订单生成页面
  router.push({
    path: '/order/create',
    query: {
      vehicleId: props.id,
    },
  })
}

watch(
  () => props.id,
  (newId) => {
    if (newId) {
      // 路由参数变化时重新加载数据
      fetchPictureDetail()
      recordBrowsingHistory()
      checkFavoriteStatus()
      fetchComments()
      fetchRecommendedVehicles()
    }
  },
)

onMounted(() => {
  fetchPictureDetail()
  recordBrowsingHistory()
  checkFavoriteStatus()
  fetchComments()
  fetchRecommendedVehicles()
})

// 处理评论页码变化
const handleCommentCurrentChange = (current: number) => {
  commentPagination.value.current = current
  fetchComments()
}

// 处理评论每页条数变化
const handleCommentSizeChange = (pageSize: number) => {
  commentPagination.value.pageSize = pageSize
  commentPagination.value.current = 1
  fetchComments()
}
</script>

<style scoped>
#vehicleDetailPage {
  margin-bottom: 16px;
}

.recommend-item {
  transition: all 0.3s;
  padding: 8px;
  border-radius: 4px;
}

.recommend-item:hover {
  background-color: #f5f7fa;
}

.comment-item:last-child {
  border-bottom: none;
}
</style>
