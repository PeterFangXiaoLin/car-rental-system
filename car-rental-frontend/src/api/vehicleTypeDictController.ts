// @ts-ignore
/* eslint-disable */
import request from '@/request'

/** 增加车辆类型 POST /api/vehicleTypeDict/add */
export async function addVehicleTypeDictUsingPost(
  body: API.VehicleTypeDictAddRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseLong_>('/api/vehicleTypeDict/add', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** 删除车辆类型 POST /api/vehicleTypeDict/delete */
export async function deleteVehicleTypeDictUsingPost(
  body: API.DeleteRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseBoolean_>('/api/vehicleTypeDict/delete', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** 获取车辆类型列表 POST /api/vehicleTypeDict/list */
export async function listVehicleTypeDictUsingPost(options?: { [key: string]: any }) {
  return request<API.BaseResponseListVehicleTypeDictVO_>('/api/vehicleTypeDict/list', {
    method: 'POST',
    ...(options || {}),
  })
}

/** 获取车辆类型列表 POST /api/vehicleTypeDict/page */
export async function pageVehicleTypeDictUsingPost(
  body: API.VehicleTypeDictQueryRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponsePageVehicleTypeDictVO_>('/api/vehicleTypeDict/page', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** 修改车辆类型 POST /api/vehicleTypeDict/update */
export async function updateVehicleTypeDictUsingPost(
  body: API.VehicleTypeDictUpdateRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseBoolean_>('/api/vehicleTypeDict/update', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}
