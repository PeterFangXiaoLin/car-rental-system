// @ts-ignore
/* eslint-disable */
import request from '@/request'

/** 增加评论 POST /api/comment/add */
export async function addCommentUsingPost(
  body: API.CommentAddRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseLong_>('/api/comment/add', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** 删除评论 POST /api/comment/delete */
export async function deleteCommentUsingPost(
  body: API.DeleteRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseBoolean_>('/api/comment/delete', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** 获取评论详情 GET /api/comment/get */
export async function getCommentByIdUsingGet(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getCommentByIdUsingGETParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseCommentVO_>('/api/comment/get', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  })
}

/** 分页获取评论列表 POST /api/comment/list/page */
export async function listCommentByPageUsingPost(
  body: API.CommentQueryRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponsePageCommentVO_>('/api/comment/list/page', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** 获取车辆的评论列表 GET /api/comment/list/vehicle */
export async function listCommentByVehicleIdUsingGet(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.listCommentByVehicleIdUsingGETParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseListCommentVO_>('/api/comment/list/vehicle', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  })
}

/** 修改评论 POST /api/comment/update */
export async function updateCommentUsingPost(
  body: API.CommentUpdateRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseBoolean_>('/api/comment/update', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}
