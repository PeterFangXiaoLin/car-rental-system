// @ts-ignore
/* eslint-disable */
import request from '@/request'

/** 添加车辆品牌 POST /api/vehicle/brand/add */
export async function addVehicleBrandUsingPost(
  body: API.VehicleBrandAddRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseLong_>('/api/vehicle/brand/add', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** 删除车辆品牌 POST /api/vehicle/brand/delete */
export async function deleteVehicleBrandUsingPost(
  body: API.DeleteRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseBoolean_>('/api/vehicle/brand/delete', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** 根据id获取车辆品牌 GET /api/vehicle/brand/get */
export async function getVehicleBrandByIdUsingGet(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getVehicleBrandByIdUsingGETParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseVehicleBrandVO_>('/api/vehicle/brand/get', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  })
}

/** 查询车辆品牌 POST /api/vehicle/brand/list */
export async function listVehicleBrandUsingPost(options?: { [key: string]: any }) {
  return request<API.BaseResponseListVehicleBrandVO_>('/api/vehicle/brand/list', {
    method: 'POST',
    ...(options || {}),
  })
}

/** 查询车辆品牌 POST /api/vehicle/brand/list/page */
export async function listVehicleBrandByPageUsingPost(
  body: API.VehicleBrandQueryRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponsePageVehicleBrandVO_>('/api/vehicle/brand/list/page', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** 更新车辆品牌 POST /api/vehicle/brand/update */
export async function updateVehicleBrandUsingPost(
  body: API.VehicleBrandUpdateRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseBoolean_>('/api/vehicle/brand/update', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}
