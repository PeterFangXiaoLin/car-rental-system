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

  type BaseResponseListCityVO_ = {
    code?: number
    data?: CityVO[]
    message?: string
  }

  type BaseResponseListString_ = {
    code?: number
    data?: string[]
    message?: string
  }

  type BaseResponseListVehicleBrandVO_ = {
    code?: number
    data?: VehicleBrandVO[]
    message?: string
  }

  type BaseResponseListVehicleModelVO_ = {
    code?: number
    data?: VehicleModelVO[]
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

  type BaseResponsePageStoreVO_ = {
    code?: number
    data?: PageStoreVO_
    message?: string
  }

  type BaseResponsePageUserVO_ = {
    code?: number
    data?: PageUserVO_
    message?: string
  }

  type BaseResponsePageVehicleBrandVO_ = {
    code?: number
    data?: PageVehicleBrandVO_
    message?: string
  }

  type BaseResponsePageVehicleTypeDictVO_ = {
    code?: number
    data?: PageVehicleTypeDictVO_
    message?: string
  }

  type BaseResponsePageVehicleVO_ = {
    code?: number
    data?: PageVehicleVO_
    message?: string
  }

  type BaseResponseStoreVO_ = {
    code?: number
    data?: StoreVO
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

  type BaseResponseVehicleBrandVO_ = {
    code?: number
    data?: VehicleBrandVO
    message?: string
  }

  type BaseResponseVehicleVO_ = {
    code?: number
    data?: VehicleVO
    message?: string
  }

  type CityAddRequest = {
    adcode?: string
    citycode?: string
    latitude?: number
    longitude?: number
    name?: string
  }

  type CityUpdateRequest = {
    adcode?: string
    citycode?: string
    id?: number
    latitude?: number
    longitude?: number
    name?: string
  }

  type CityVO = {
    adcode?: string
    citycode?: string
    id?: number
    latitude?: number
    level?: string
    longitude?: number
    name?: string
  }

  type DeleteRequest = {
    id?: number
  }

  type getStoreUsingGETParams = {
    /** id */
    id: number
  }

  type getUserByIdUsingGETParams = {
    /** id */
    id: number
  }

  type getUserUsingGETParams = {
    /** id */
    id: number
  }

  type getVehicleBrandByIdUsingGETParams = {
    /** id */
    id: number
  }

  type getVehicleByIdUsingGETParams = {
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

  type PageStoreVO_ = {
    countId?: string
    current?: number
    maxLimit?: number
    optimizeCountSql?: boolean
    orders?: OrderItem[]
    pages?: number
    records?: StoreVO[]
    searchCount?: boolean
    size?: number
    total?: number
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

  type PageVehicleBrandVO_ = {
    countId?: string
    current?: number
    maxLimit?: number
    optimizeCountSql?: boolean
    orders?: OrderItem[]
    pages?: number
    records?: VehicleBrandVO[]
    searchCount?: boolean
    size?: number
    total?: number
  }

  type PageVehicleTypeDictVO_ = {
    countId?: string
    current?: number
    maxLimit?: number
    optimizeCountSql?: boolean
    orders?: OrderItem[]
    pages?: number
    records?: VehicleTypeDictVO[]
    searchCount?: boolean
    size?: number
    total?: number
  }

  type PageVehicleVO_ = {
    countId?: string
    current?: number
    maxLimit?: number
    optimizeCountSql?: boolean
    orders?: OrderItem[]
    pages?: number
    records?: VehicleVO[]
    searchCount?: boolean
    size?: number
    total?: number
  }

  type refreshCaptchaUsingGETParams = {
    /** captchaKey */
    captchaKey: string
  }

  type RentalOrderCreateRequest = true

  type StoreCreateRequest = {
    adcode?: string
    address?: string
    city?: string
    citycode?: string
    closeTime?: string
    contactPhone?: string
    description?: string
    district?: string
    images?: string
    latitude?: number
    longitude?: number
    openTime?: string
    province?: string
    status?: number
    storeName?: string
  }

  type StoreQueryRequest = {
    adcode?: string
    address?: string
    city?: string
    citycode?: string
    closeTime?: string
    contactPhone?: string
    current?: number
    description?: string
    district?: string
    id?: number
    openTime?: string
    pageSize?: number
    province?: string
    sortField?: string
    sortOrder?: string
    status?: number
    storeName?: string
  }

  type StoreUpdateRequest = {
    adcode?: string
    address?: string
    city?: string
    citycode?: string
    closeTime?: string
    contactPhone?: string
    description?: string
    district?: string
    id?: number
    images?: string
    latitude?: number
    longitude?: number
    openTime?: string
    province?: string
    status?: number
    storeName?: string
  }

  type StoreVO = {
    adcode?: string
    address?: string
    city?: string
    citycode?: string
    closeTime?: string
    contactPhone?: string
    description?: string
    district?: string
    id?: number
    images?: string
    latitude?: number
    longitude?: number
    openTime?: string
    province?: string
    status?: number
    storeName?: string
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

  type VehicleAddRequest = {
    brandId?: number
    color?: string
    dailyPrice?: number
    deposit?: number
    description?: string
    imageUrl?: string
    mileage?: number
    modelId?: number
    productionYear?: number
    status?: number
    vehicleNo?: string
    vehicleTypeId?: number
  }

  type VehicleBrandAddRequest = {
    brandLogo?: string
    brandName?: string
    description?: string
  }

  type VehicleBrandQueryRequest = {
    current?: number
    pageSize?: number
    searchText?: string
    sortField?: string
    sortOrder?: string
  }

  type VehicleBrandUpdateRequest = {
    brandLogo?: string
    brandName?: string
    description?: string
    id?: number
  }

  type VehicleBrandVO = {
    brandLogo?: string
    brandName?: string
    createTime?: string
    description?: string
    id?: number
  }

  type VehicleModelAddRequest = {
    brandId?: number
    modelName?: string
  }

  type VehicleModelQueryRequest = {
    brandId?: number
  }

  type VehicleModelUpdateRequest = {
    brandId?: number
    id?: number
    modelName?: string
  }

  type VehicleModelVO = {
    brandId?: number
    createTime?: string
    id?: number
    modelName?: string
  }

  type VehicleQueryRequest = {
    brandId?: number
    color?: string
    current?: number
    description?: string
    id?: number
    maxDailyPrice?: number
    maxDeposit?: number
    minDailyPrice?: number
    minDeposit?: number
    modelId?: number
    pageSize?: number
    sortField?: string
    sortOrder?: string
    status?: number
    vehicleNo?: string
    vehicleTypeId?: number
  }

  type VehicleTypeDictAddRequest = {
    typeName?: string
  }

  type VehicleTypeDictQueryRequest = {
    current?: number
    pageSize?: number
    sortField?: string
    sortOrder?: string
    typeName?: string
  }

  type VehicleTypeDictUpdateRequest = {
    id?: number
    typeName?: string
  }

  type VehicleTypeDictVO = {
    createTime?: string
    id?: number
    typeName?: string
  }

  type VehicleUpdateRequest = {
    brandId?: number
    color?: string
    dailyPrice?: number
    deposit?: number
    description?: string
    id?: number
    imageUrl?: string
    mileage?: number
    modelId?: number
    productionYear?: number
    status?: number
    vehicleNo?: string
    vehicleTypeId?: number
  }

  type VehicleVO = {
    brandId?: number
    brandName?: string
    color?: string
    createTime?: string
    dailyPrice?: number
    deposit?: number
    description?: string
    id?: number
    imageUrl?: string
    mileage?: number
    modelId?: number
    modelName?: string
    productionYear?: number
    status?: number
    vehicleNo?: string
    vehicleTypeId?: number
    vehicleTypeName?: string
  }
}
