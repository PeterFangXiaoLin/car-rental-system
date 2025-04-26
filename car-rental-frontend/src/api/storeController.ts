// @ts-ignore
/* eslint-disable */
import request from '@/request'

/** 创建门店 POST /api/store/create */
export async function createStoreUsingPost(
  body: API.StoreCreateRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseLong_>('/api/store/create', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** 删除门店 POST /api/store/delete */
export async function deleteStoreUsingPost(
  body: API.DeleteRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseBoolean_>('/api/store/delete', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** 根据id获取门店 GET /api/store/get */
export async function getStoreUsingGet(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getStoreUsingGETParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseStoreVO_>('/api/store/get', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  })
}

/** 根据城市名获取门店列表 GET /api/store/list */
export async function listStoreByCityNameUsingGet(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.listStoreByCityNameUsingGETParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseListStoreVO_>('/api/store/list', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  })
}

/** 分页获取门店列表 POST /api/store/page */
export async function listStoreByPageUsingPost(
  body: API.StoreQueryRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponsePageStoreVO_>('/api/store/page', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** 更新门店 POST /api/store/update */
export async function updateStoreUsingPost(
  body: API.StoreUpdateRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseBoolean_>('/api/store/update', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** 上传门店图片 POST /api/store/upload */
export async function uploadStoreImageUsingPost(
  body: {
    /** files */
    files: any[]
  },
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseListString_>('/api/store/upload', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded',
    },
    data: body,
    ...(options || {}),
  })
}
