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
