// @ts-ignore
/* eslint-disable */
import request from '@/request'

/** 生成图形验证码 GET /api/sms/auth/getCaptcha */
export async function getCaptchaUsingGet(options?: { [key: string]: any }) {
  return request<API.BaseResponseAuthCaptchaVO_>('/api/sms/auth/getCaptcha', {
    method: 'GET',
    ...(options || {}),
  })
}
