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
    driverLicenseExpireDate?: string
    driverLicenseIssueDate?: string
    driverLicenseNo?: string
    driverLicenseType?: string
    drivingYears?: number
    email?: string
    gender?: number
    id?: number
    idCardNumber?: string
    isDriver?: number
    memberLevel?: number
    phoneNumber?: string
    realName?: string
    rejectReason?: string
    status?: number
    userAccount?: string
    userAvatar?: string
    userName?: string
    userProfile?: string
    userRole?: number
    verifyStatus?: number
    verifyTime?: string
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
    driverLicenseExpireDate?: string
    driverLicenseIssueDate?: string
    driverLicenseNo?: string
    driverLicenseType?: string
    editTime?: string
    email?: string
    gender?: number
    id?: number
    idCardNumber?: string
    isDelete?: number
    isDriver?: number
    memberLevel?: number
    phoneNumber?: string
    realName?: string
    rejectReason?: string
    reviewId?: number
    reviewTime?: string
    status?: number
    updateTime?: string
    userAccount?: string
    userAvatar?: string
    userName?: string
    userPassword?: string
    userProfile?: string
    userRole?: number
    verifyResult?: number
    verifyStatus?: number
    verifyTime?: string
  }

  type UserAddRequest = {
    email?: string
    gender?: number
    memberLevel?: number
    phoneNumber?: string
    userAccount?: string
    userAvatar?: string
    userName?: string
    userPassword?: string
    userProfile?: string
    userRole?: number
  }

  type UserAdminUpdateRequest = {
    email?: string
    gender?: number
    id?: number
    memberLevel?: number
    phoneNumber?: string
    userAvatar?: string
    userName?: string
    userProfile?: string
    userRole?: number
  }

  type UserAuthRequest = {
    driverLicenseExpireDate?: string
    driverLicenseIssueDate?: string
    driverLicenseNo?: string
    driverLicenseType?: string
    idCardNumber?: string
    realName?: string
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

  type UserReviewRequest = {
    id?: number
    rejectReason?: string
    reviewStatus?: number
  }

  type UserUpdatePasswordRequest = {
    checkPassword?: string
    newPassword?: string
    oldPassword?: string
  }

  type UserUpdateRequest = {
    email?: string
    gender?: number
    phoneNumber?: string
    userName?: string
    userProfile?: string
  }

  type UserVO = {
    createTime?: string
    driverLicenseExpireDate?: string
    driverLicenseIssueDate?: string
    driverLicenseNo?: string
    driverLicenseType?: string
    drivingYears?: number
    editTime?: string
    email?: string
    gender?: number
    id?: number
    idCardNumber?: string
    isDriver?: number
    memberLevel?: number
    phoneNumber?: string
    realName?: string
    rejectReason?: string
    status?: number
    userAccount?: string
    userAvatar?: string
    userName?: string
    userProfile?: string
    userRole?: number
    verifyStatus?: number
    verifyTime?: string
  }
}
