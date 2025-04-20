// @ts-ignore
/* eslint-disable */
import request from '@/request'

/** 新增车辆型号 POST /api/vehicle/model/add */
export async function addVehicleModelUsingPost(
  body: API.VehicleModelAddRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseLong_>('/api/vehicle/model/add', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** 删除车辆型号 POST /api/vehicle/model/delete */
export async function deleteVehicleModelUsingPost(
  body: API.DeleteRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseBoolean_>('/api/vehicle/model/delete', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** 获取车辆型号VO GET /api/vehicle/model/get */
export async function getVehicleModelByIdUsingGet(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getVehicleModelByIdUsingGETParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseVehicleModelVO_>('/api/vehicle/model/get', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  })
}

/** 获取车辆型号列表 POST /api/vehicle/model/list */
export async function listVehicleModelUsingPost(
  body: API.VehicleModelQueryRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseListVehicleModelVO_>('/api/vehicle/model/list', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** 获取车辆型号分页 POST /api/vehicle/model/page */
export async function listVehicleModelByPageUsingPost(
  body: API.VehicleModelQueryRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponsePageVehicleModelVO_>('/api/vehicle/model/page', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** 修改车辆型号 POST /api/vehicle/model/update */
export async function updateVehicleModelUsingPost(
  body: API.VehicleModelUpdateRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseBoolean_>('/api/vehicle/model/update', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}
