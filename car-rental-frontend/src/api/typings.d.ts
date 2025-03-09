declare namespace API {
  type AuthCaptchaVO = {
    captchaKey?: string
    image?: string
  }

  type BaseResponseAuthCaptchaVO_ = {
    code?: number
    data?: AuthCaptchaVO
    message?: string
  }

  type BaseResponseBoolean_ = {
    code?: number
    data?: boolean
    message?: string
  }

  type BaseResponseLoginUserVO_ = {
    code?: number
    data?: LoginUserVO
    message?: string
  }

  type BaseResponseLong_ = {
    code?: number
    data?: number
    message?: string
  }

  type BaseResponsePageUserVO_ = {
    code?: number
    data?: PageUserVO_
    message?: string
  }

  type BaseResponseString_ = {
    code?: number
    data?: string
    message?: string
  }

  type BaseResponseUser_ = {
    code?: number
    data?: User
    message?: string
  }

  type BaseResponseUserVO_ = {
    code?: number
    data?: UserVO
    message?: string
  }

  type DeleteRequest = {
    id?: number
  }

  type getUserByIdUsingGETParams = {
    /** id */
    id: number
  }

  type getUserUsingGETParams = {
    /** id */
    id: number
  }

  type LoginUserVO = {
    createTime?: string
    creditScore?: number
    drivingYears?: number
    email?: string
    gender?: number
    id?: number
    memberLevel?: number
    phoneNumber?: string
    realName?: string
    userAccount?: string
    userAvatar?: string
    userName?: string
    userProfile?: string
    userRole?: number
  }

  type OrderItem = {
    asc?: boolean
    column?: string
  }

  type PageUserVO_ = {
    countId?: string
    current?: number
    maxLimit?: number
    optimizeCountSql?: boolean
    orders?: OrderItem[]
    pages?: number
    records?: UserVO[]
    searchCount?: boolean
    size?: number
    total?: number
  }

  type refreshCaptchaUsingGETParams = {
    /** captchaKey */
    captchaKey: string
  }

  type User = {
    createTime?: string
    creditScore?: number
    drivingLicenseNo?: string
    drivingYears?: number
    editTime?: string
    email?: string
    gender?: number
    id?: number
    idCardNumber?: string
    isDelete?: number
    memberLevel?: number
    phoneNumber?: string
    realName?: string
    updateTime?: string
    userAccount?: string
    userAvatar?: string
    userName?: string
    userPassword?: string
    userProfile?: string
    userRole?: number
  }

  type UserAddRequest = {
    creditScore?: number
    drivingLicenseNo?: string
    drivingYears?: number
    email?: string
    gender?: number
    idCardNumber?: string
    memberLevel?: number
    phoneNumber?: string
    realName?: string
    userAccount?: string
    userAvatar?: string
    userName?: string
    userPassword?: string
    userProfile?: string
    userRole?: number
  }

  type UserAdminUpdateRequest = {
    creditScore?: number
    drivingLicenseNo?: string
    drivingYears?: number
    email?: string
    gender?: number
    id?: number
    idCardNumber?: string
    memberLevel?: number
    phoneNumber?: string
    realName?: string
    userAccount?: string
    userAvatar?: string
    userName?: string
    userProfile?: string
    userRole?: number
  }

  type UserLoginRequest = {
    userAccount?: string
    userPassword?: string
  }

  type UserQueryRequest = {
    current?: number
    email?: string
    gender?: number
    id?: number
    memberLevel?: number
    pageSize?: number
    phoneNumber?: string
    realName?: string
    sortField?: string
    sortOrder?: string
    userAccount?: string
    userName?: string
    userRole?: number
  }

  type UserRegisterRequest = {
    captchaCode?: string
    captchaKey?: string
    checkPassword?: string
    userAccount?: string
    userPassword?: string
  }

  type UserUpdatePasswordRequest = {
    checkPassword?: string
    newPassword?: string
    oldPassword?: string
  }

  type UserUpdateRequest = {
    drivingLicenseNo?: string
    drivingYears?: number
    email?: string
    gender?: number
    idCardNumber?: string
    phoneNumber?: string
    realName?: string
    userName?: string
    userProfile?: string
  }

  type UserVO = {
    createTime?: string
    creditScore?: number
    drivingLicenseNo?: string
    drivingYears?: number
    editTime?: string
    email?: string
    gender?: number
    id?: number
    idCardNumber?: string
    memberLevel?: number
    phoneNumber?: string
    realName?: string
    userAccount?: string
    userAvatar?: string
    userName?: string
    userProfile?: string
    userRole?: number
  }
}
