// @ts-ignore
/* eslint-disable */
import request from '@/request'

/** 添加司机 POST /api/driver/add */
export async function addDriverUsingPost(
  body: API.DriverAddRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseLong_>('/api/driver/add', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** 删除司机 POST /api/driver/delete */
export async function deleteDriverUsingPost(
  body: API.DeleteRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseBoolean_>('/api/driver/delete', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** 获取司机 GET /api/driver/get/vo */
export async function getDriverVoByIdUsingGet(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getDriverVOByIdUsingGETParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseDriverVO_>('/api/driver/get/vo', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  })
}

/** 分页获取司机列表 POST /api/driver/list/page */
export async function listDriverVoByPageUsingPost(
  body: API.DriverQueryRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponsePageDriverVO_>('/api/driver/list/page', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** 修改司机 POST /api/driver/update */
export async function updateDriverUsingPost(
  body: API.DriverUpdateRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseBoolean_>('/api/driver/update', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}
