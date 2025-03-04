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

/** 刷新图形验证码 GET /api/sms/auth/refresh */
export async function refreshCaptchaUsingGet(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.refreshCaptchaUsingGETParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseAuthCaptchaVO_>('/api/sms/auth/refresh', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  })
}
