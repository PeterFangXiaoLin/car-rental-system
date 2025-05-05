// @ts-ignore
/* eslint-disable */
import request from '@/request'

/** 新增车辆 POST /api/vehicle/add */
export async function addVehicleUsingPost(
  body: API.VehicleAddRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseLong_>('/api/vehicle/add', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** 删除车辆 POST /api/vehicle/delete */
export async function deleteVehicleUsingPost(
  body: API.DeleteRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseBoolean_>('/api/vehicle/delete', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** 根据 id 获取车辆 GET /api/vehicle/get */
export async function getVehicleByIdUsingGet(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getVehicleByIdUsingGETParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseVehicleVO_>('/api/vehicle/get', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  })
}

/** 分页获取车辆列表 POST /api/vehicle/page */
export async function listVehicleByPageUsingPost(
  body: API.VehicleQueryRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponsePageVehicleVO_>('/api/vehicle/page', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** 推荐车辆 GET /api/vehicle/recommend */
export async function recommendVehicleUsingGet(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.recommendVehicleUsingGETParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseListVehicleVO_>('/api/vehicle/recommend', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  })
}

/** 修改车辆 POST /api/vehicle/update */
export async function updateVehicleUsingPost(
  body: API.VehicleUpdateRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseBoolean_>('/api/vehicle/update', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}
