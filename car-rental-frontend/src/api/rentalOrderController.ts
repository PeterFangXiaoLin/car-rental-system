// @ts-ignore
/* eslint-disable */
import request from '@/request'

/** 取消订单 POST /api/rentalOrder/cancel */
export async function cancelRentalOrderUsingPost(
  body: API.RentalOrderCancelRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseBoolean_>('/api/rentalOrder/cancel', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

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

/** 删除订单 POST /api/rentalOrder/delete */
export async function deleteRentalOrderUsingPost(
  body: API.DeleteRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseBoolean_>('/api/rentalOrder/delete', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** 获取订单详情 GET /api/rentalOrder/get */
export async function getRentalOrderUsingGet(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getRentalOrderUsingGETParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseRentalOrderVO_>('/api/rentalOrder/get', {
    method: 'GET',
    params: {
      ...params,
    },
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

/** 获取订单分页 POST /api/rentalOrder/page */
export async function pageRentalOrderUsingPost(
  body: API.RentalOrderAdminPageRequest,
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

/** 获取当前登录用户的订单列表 POST /api/rentalOrder/page/my */
export async function pageMyRentalOrderUsingPost(
  body: API.RentalOrderPageRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponsePageRentalOrderVO_>('/api/rentalOrder/page/my', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** 支付订单 GET /api/rentalOrder/pay */
export async function payOrderUsingGet(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.payOrderUsingGETParams,
  options?: { [key: string]: any }
) {
  return request<string>('/api/rentalOrder/pay', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  })
}

/** 取车 GET /api/rentalOrder/pickup */
export async function pickupVehicleUsingGet(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.pickupVehicleUsingGETParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseBoolean_>('/api/rentalOrder/pickup', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  })
}

/** 还车 GET /api/rentalOrder/return */
export async function returnVehicleUsingGet(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.returnVehicleUsingGETParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseBoolean_>('/api/rentalOrder/return', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  })
}
