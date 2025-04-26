// @ts-ignore
/* eslint-disable */
import request from '@/request'

/** 添加收藏 POST /api/vehicleFavorite/add */
export async function addVehicleFavoriteUsingPost(
  body: API.VehicleFavoriteAddRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseLong_>('/api/vehicleFavorite/add', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** 取消收藏 POST /api/vehicleFavorite/cancel */
export async function cancelVehicleFavoriteUsingPost(
  body: API.VehicleFavoriteCancelRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseBoolean_>('/api/vehicleFavorite/cancel', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** 检查是否收藏 GET /api/vehicleFavorite/check */
export async function checkVehicleFavoriteUsingGet(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.checkVehicleFavoriteUsingGETParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseBoolean_>('/api/vehicleFavorite/check', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  })
}

/** 分页查询收藏 POST /api/vehicleFavorite/page */
export async function pageVehicleFavoriteUsingPost(
  body: API.VehicleFavoriteQueryRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponsePageVehicleFavoriteVO_>('/api/vehicleFavorite/page', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}
