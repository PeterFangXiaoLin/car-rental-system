// @ts-ignore
/* eslint-disable */
import request from '@/request'

/** 增加/更新浏览历史 POST /api/vehicle/brows/addOrUpdate */
export async function addOrUpdateBrowsHistoryUsingPost(
  body: API.BrowsHistoryAddRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseLong_>('/api/vehicle/brows/addOrUpdate', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** 清空浏览历史 POST /api/vehicle/brows/clear */
export async function clearBrowsHistoryUsingPost(options?: { [key: string]: any }) {
  return request<API.BaseResponseBoolean_>('/api/vehicle/brows/clear', {
    method: 'POST',
    ...(options || {}),
  })
}

/** 删除浏览历史 POST /api/vehicle/brows/delete */
export async function deleteBrowsHistoryUsingPost(
  body: API.DeleteRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseBoolean_>('/api/vehicle/brows/delete', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** 分页查询浏览历史 POST /api/vehicle/brows/page */
export async function pageBrowseHistoryUsingPost(
  body: API.BrowsHistoryQueryRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponsePageVehicleBrowsingHistoryVO_>('/api/vehicle/brows/page', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}
