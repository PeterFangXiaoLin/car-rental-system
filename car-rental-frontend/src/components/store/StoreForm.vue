<template>
  <el-dialog v-model="dialogVisible" title="新增店铺" width="50%" destroy-on-close center="center">
    <el-form :model="form" :rules="rules" label-width="100px" label-position="right">
      <el-form-item label="店铺名称" prop="storeName">
        <el-input v-model="form.storeName" placeholder="请输入店铺名称" />
      </el-form-item>
      <el-form-item label="店铺地址" prop="address">
        <el-input v-model="form.address" placeholder="请输入店铺地址" />
      </el-form-item>
      <el-form-item label="店铺电话" prop="contractPhone">
        <el-input v-model="form.contractPhone" placeholder="请输入店铺电话" />
      </el-form-item>
      <el-form-item label="店铺图片" prop="images">
        <StoreImagesUpload v-model="form.images" />
      </el-form-item>
      <el-form-item label="店铺描述" prop="description">
        <el-input v-model="form.description" placeholder="请输入店铺描述" />
      </el-form-item>
      <el-form-item label="店铺状态" prop="status">
        <el-select v-model="form.status" placeholder="请选择店铺状态">
          <el-option label="营业中" value="1" />
          <el-option label="关闭" value="0" />
        </el-select>
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button type="primary" @click="handleSubmit">提交</el-button>
      <el-button @click="dialogVisible = false">取消</el-button>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
import { ref } from 'vue'

const dialogVisible = ref(false)

const form = ref({
  storeName: '',
  address: '',
  contractPhone: '',
  images: [],
  description: '',
  status: '',
})
</script>

<style scoped></style>
