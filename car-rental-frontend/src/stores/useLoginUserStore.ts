import { defineStore } from 'pinia'
import { ref } from 'vue'
import { getLoginUserUsingGet } from '@/api/userController.ts'

export const useLoginUserStore = defineStore('loginUser', () => {
  const loginUser = ref<API.UserRespVO>({
    userName: '未登录',
  })

  /**
   * 远程获取登录用户信息
   */
  async function fetchLoginUser() {
    const res = await getLoginUserUsingGet()
    if (res.data.code === 0 && res.data.data) {
      setLoginUser(res.data.data)
    }
  }

  /**
   * 设置登录用户
   */
  function setLoginUser(user: API.UserRespVO) {
    loginUser.value = user
  }

  return { loginUser, getLoginUser, setLoginUser }
})
