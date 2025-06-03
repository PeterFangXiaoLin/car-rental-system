// @ts-ignore
/* eslint-disable */
import request from '@/request'

/** 增加评论回复 POST /api/comment/reply/add */
export async function addCommentReplyUsingPost(
  body: API.CommentReplyAddRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseLong_>('/api/comment/reply/add', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** 追加评论 POST /api/comment/reply/add/reply */
export async function addReplyToCommentUsingPost(
  body: API.ReplyToCommentRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseBoolean_>('/api/comment/reply/add/reply', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** 删除评论回复 POST /api/comment/reply/delete */
export async function deleteCommentReplyUsingPost(
  body: API.DeleteRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseBoolean_>('/api/comment/reply/delete', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** 获取评论回复详情 GET /api/comment/reply/get */
export async function getCommentReplyByIdUsingGet(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getCommentReplyByIdUsingGETParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseCommentReplyVO_>('/api/comment/reply/get', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  })
}

/** 获取评论的回复列表 GET /api/comment/reply/list/comment */
export async function listCommentReplyByCommentIdUsingGet(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.listCommentReplyByCommentIdUsingGETParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseListCommentReplyVO_>('/api/comment/reply/list/comment', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  })
}

/** 分页获取评论回复列表 POST /api/comment/reply/list/page */
export async function listCommentReplyByPageUsingPost(
  body: API.CommentReplyQueryRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponsePageCommentReplyVO_>('/api/comment/reply/list/page', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** 修改评论回复 POST /api/comment/reply/update */
export async function updateCommentReplyUsingPost(
  body: API.CommentReplyUpdateRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseBoolean_>('/api/comment/reply/update', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}
