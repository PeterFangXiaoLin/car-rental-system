// @ts-ignore
/* eslint-disable */
import request from '@/request'

/** 添加能源类型字典 POST /api/energyTypeDict/add */
export async function addEnergyTypeDictUsingPost(
  body: API.EnergyTypeDictAddRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseLong_>('/api/energyTypeDict/add', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** 删除能源类型字典 POST /api/energyTypeDict/delete */
export async function deleteEnergyTypeDictUsingPost(
  body: API.DeleteRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseBoolean_>('/api/energyTypeDict/delete', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** 根据id获取能源类型字典 GET /api/energyTypeDict/get */
export async function getEnergyTypeDictByIdUsingGet(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getEnergyTypeDictByIdUsingGETParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseEnergyTypeDictVO_>('/api/energyTypeDict/get', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  })
}

/** 获取能源类型字典列表 POST /api/energyTypeDict/list */
export async function listEnergyTypeDictUsingPost(options?: { [key: string]: any }) {
  return request<API.BaseResponseListEnergyTypeDictVO_>('/api/energyTypeDict/list', {
    method: 'POST',
    ...(options || {}),
  })
}

/** 分页获取能源类型字典列表 POST /api/energyTypeDict/list/page */
export async function listEnergyTypeDictByPageUsingPost(
  body: API.EnergyTypeDictQueryRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponsePageEnergyTypeDictVO_>('/api/energyTypeDict/list/page', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** 更新能源类型字典 POST /api/energyTypeDict/update */
export async function updateEnergyTypeDictUsingPost(
  body: API.EnergyTypeDictUpdateRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseBoolean_>('/api/energyTypeDict/update', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}
