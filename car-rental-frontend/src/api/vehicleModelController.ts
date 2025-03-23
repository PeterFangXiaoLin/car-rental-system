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
