<template>
  <div id="side-menu">
    <el-scrollbar>
      <!-- 系统名称和Logo -->
      <div class="logo-container">
        <div class="logo-icon" v-if="!isCollapse">
          <el-icon :size="36" color="#409EFF"><van /></el-icon>
        </div>
        <div class="logo-icon-small" v-else>
          <el-icon :size="28" color="#409EFF"><van /></el-icon>
        </div>
        <h1 class="system-name" v-if="!isCollapse">租车系统</h1>
      </div>

      <el-menu
        :collapse="isCollapse"
        :default-active="activeIndex"
        @select="handleSelect"
        class="el-menu-vertical"
        background-color="#001529"
        text-color="#fff"
        active-text-color="#ffd04b"
        router
      >
        <!-- 首页菜单项 -->
        <el-menu-item index="/">
          <el-icon><home-filled /></el-icon>
          <template #title>首页</template>
        </el-menu-item>

        <!-- 管理菜单项，仅管理员可见 -->
        <template v-if="isAdmin">
          <!-- 用户管理 -->
          <el-menu-item index="/user/manage">
            <el-icon><user /></el-icon>
            <template #title>用户管理</template>
          </el-menu-item>

          <!-- 司机管理 -->
          <el-menu-item index="/driver/manage">
            <el-icon><van /></el-icon>
            <template #title>司机管理</template>
          </el-menu-item>

          <!-- 门店管理 -->
          <el-menu-item index="/store/manage">
            <el-icon><shop /></el-icon>
            <template #title>门店管理</template>
          </el-menu-item>

          <!-- 车辆管理 -->
          <el-sub-menu index="/vehicle">
            <template #title>
              <el-icon><box /></el-icon>
              <span>车辆管理</span>
            </template>
            <el-menu-item index="/vehicle/manage">
              <span>车辆管理</span>
            </el-menu-item>
            <el-menu-item index="/vehicle/brand/manage">
              <span>车辆品牌管理</span>
            </el-menu-item>
            <el-menu-item index="/vehicle/model/manage">
              <span>车辆型号管理</span>
            </el-menu-item>
            <el-menu-item index="/vehicle/type/manage">
              <span>车型管理</span>
            </el-menu-item>
            <el-menu-item index="/vehicle/energy/manage">
              <span>能源类型管理</span>
            </el-menu-item>
          </el-sub-menu>
        </template>
      </el-menu>
    </el-scrollbar>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useLoginUserStore } from '@/stores/useLoginUserStore'
import { Van, User, HomeFilled, Box, Shop } from '@element-plus/icons-vue'
import ACCESS_ENUM from '@/access/accessEnum'

// 定义props
defineProps({
  isCollapse: {
    type: Boolean,
    default: false,
  },
})

const router = useRouter()
const route = useRoute()
const loginUserStore = useLoginUserStore()

// 判断是否为管理员
const isAdmin = computed(() => {
  return loginUserStore.loginUser?.userRole === ACCESS_ENUM.ADMIN
})

// 当前激活的菜单项，跟随路由变化
const activeIndex = computed(() => {
  // 错误页面和未找到页面默认激活首页
  if (route.path === '/404' || route.path === '/401') {
    return '/'
  }

  // 特定子页面的情况
  if (route.path.includes('vehicle/')) {
    // 对于子菜单项，直接返回完整路径以激活具体项
    if (
      [
        '/vehicle/manage',
        '/vehicle/brand/manage',
        '/vehicle/model/manage',
        '/vehicle/type/manage',
        '/vehicle/energy/manage',
      ].includes(route.path)
    ) {
      return route.path
    }

    // 其他车辆相关页面，激活父菜单
    return '/vehicle'
  }

  return route.path
})

// 处理菜单选择
const handleSelect = (index: string) => {
  router.push(index)
}
</script>

<style scoped>
.el-menu-vertical:not(.el-menu--collapse) {
  width: 200px;
  min-height: calc(100vh - 70px);
  border-right: none;
}

.el-menu-vertical.el-menu--collapse {
  border-right: none;
}

#side-menu {
  height: 100%;
  background-color: #001529;
  width: 100%;
  box-sizing: border-box;
}

.logo-container {
  height: 70px;
  display: flex;
  align-items: center;
  padding: 0 20px 0 16px;
  background-color: #002140;
  overflow: hidden;
  box-sizing: border-box;
  width: 100%;
}

.logo-icon {
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 16px;
}

.logo-icon-small {
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto;
}

.system-name {
  color: white;
  font-size: 20px;
  font-weight: 600;
  margin: 0;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
</style>
