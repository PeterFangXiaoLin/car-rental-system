// @ts-ignore
/* eslint-disable */
import request from '@/request'

/** 增加城市 POST /api/city/add */
export async function addCityUsingPost(body: API.CityAddRequest, options?: { [key: string]: any }) {
  return request<API.BaseResponseLong_>('/api/city/add', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** 删除城市 POST /api/city/delete */
export async function deleteCityUsingPost(
  body: API.DeleteRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseBoolean_>('/api/city/delete', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** 导入城市数据 POST /api/city/import */
export async function importCityDataUsingPost(
  body: {},
  file?: File,
  options?: { [key: string]: any }
) {
  const formData = new FormData()

  if (file) {
    formData.append('file', file)
  }

  Object.keys(body).forEach((ele) => {
    const item = (body as any)[ele]

    if (item !== undefined && item !== null) {
      if (typeof item === 'object' && !(item instanceof File)) {
        if (item instanceof Array) {
          item.forEach((f) => formData.append(ele, f || ''))
        } else {
          formData.append(ele, JSON.stringify(item))
        }
      } else {
        formData.append(ele, item)
      }
    }
  })

  return request<API.BaseResponseBoolean_>('/api/city/import', {
    method: 'POST',
    data: formData,
    requestType: 'form',
    ...(options || {}),
  })
}

/** 获取城市列表 GET /api/city/list */
export async function listCityUsingGet(options?: { [key: string]: any }) {
  return request<API.BaseResponseListCityVO_>('/api/city/list', {
    method: 'GET',
    ...(options || {}),
  })
}

/** 修改城市 POST /api/city/update */
export async function updateCityUsingPost(
  body: API.CityUpdateRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseBoolean_>('/api/city/update', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}
