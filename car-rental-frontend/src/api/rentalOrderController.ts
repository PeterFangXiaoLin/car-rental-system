// @ts-ignore
/* eslint-disable */
import request from '@/request'

/** 创建订单 POST /api/rentalOrder/create */
export async function createRentalOrderUsingPost(
  body: API.RentalOrderCreateRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseLong_>('/api/rentalOrder/create', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** 支付宝异步通知 POST /api/rentalOrder/notify */
export async function alipayNotifyUsingPost(options?: { [key: string]: any }) {
  return request<string>('/api/rentalOrder/notify', {
    method: 'POST',
    ...(options || {}),
  })
}

/** 获取订单列表 POST /api/rentalOrder/page */
export async function pageRentalOrderUsingPost(
  body: API.RentalOrderPageRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponsePageRentalOrderVO_>('/api/rentalOrder/page', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** 支付订单 POST /api/rentalOrder/pay */
export async function payOrderUsingPost(
  body: API.RentalOrderPayRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseString_>('/api/rentalOrder/pay', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}
