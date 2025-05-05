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

  type BaseResponseCommentReplyVO_ = {
    code?: number
    data?: CommentReplyVO
    message?: string
  }

  type BaseResponseCommentVO_ = {
    code?: number
    data?: CommentVO
    message?: string
  }

  type BaseResponseDriverVO_ = {
    code?: number
    data?: DriverVO
    message?: string
  }

  type BaseResponseEnergyTypeDictVO_ = {
    code?: number
    data?: EnergyTypeDictVO
    message?: string
  }

  type BaseResponseListCityVO_ = {
    code?: number
    data?: CityVO[]
    message?: string
  }

  type BaseResponseListCommentReplyVO_ = {
    code?: number
    data?: CommentReplyVO[]
    message?: string
  }

  type BaseResponseListCommentVO_ = {
    code?: number
    data?: CommentVO[]
    message?: string
  }

  type BaseResponseListEnergyTypeDictVO_ = {
    code?: number
    data?: EnergyTypeDictVO[]
    message?: string
  }

  type BaseResponseListStoreVO_ = {
    code?: number
    data?: StoreVO[]
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

  type BaseResponseListVehicleTypeDictVO_ = {
    code?: number
    data?: VehicleTypeDictVO[]
    message?: string
  }

  type BaseResponseListVehicleVO_ = {
    code?: number
    data?: VehicleVO[]
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

  type BaseResponsePageCommentReplyVO_ = {
    code?: number
    data?: PageCommentReplyVO_
    message?: string
  }

  type BaseResponsePageCommentVO_ = {
    code?: number
    data?: PageCommentVO_
    message?: string
  }

  type BaseResponsePageDriverVO_ = {
    code?: number
    data?: PageDriverVO_
    message?: string
  }

  type BaseResponsePageEnergyTypeDictVO_ = {
    code?: number
    data?: PageEnergyTypeDictVO_
    message?: string
  }

  type BaseResponsePageRentalOrderVO_ = {
    code?: number
    data?: PageRentalOrderVO_
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

  type BaseResponsePageVehicleBrowsingHistoryVO_ = {
    code?: number
    data?: PageVehicleBrowsingHistoryVO_
    message?: string
  }

  type BaseResponsePageVehicleFavoriteVO_ = {
    code?: number
    data?: PageVehicleFavoriteVO_
    message?: string
  }

  type BaseResponsePageVehicleModelVO_ = {
    code?: number
    data?: PageVehicleModelVO_
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

  type BaseResponseRentalOrderVO_ = {
    code?: number
    data?: RentalOrderVO
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

  type BaseResponseVehicleModelVO_ = {
    code?: number
    data?: VehicleModelVO
    message?: string
  }

  type BaseResponseVehicleTypeDictVO_ = {
    code?: number
    data?: VehicleTypeDictVO
    message?: string
  }

  type BaseResponseVehicleVO_ = {
    code?: number
    data?: VehicleVO
    message?: string
  }

  type BrowsHistoryAddRequest = {
    vehicleId?: number
  }

  type BrowsHistoryQueryRequest = {
    current?: number
    pageSize?: number
    searchText?: string
    sortField?: string
    sortOrder?: string
  }

  type checkVehicleFavoriteUsingGETParams = {
    /** vehicleId */
    vehicleId: number
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

  type CommentAddRequest = {
    content?: string
    driverId?: number
    driverRating?: number
    images?: string
    orderId?: number
    vehicleId?: number
    vehicleRating?: number
  }

  type CommentQueryRequest = {
    current?: number
    driverId?: number
    orderId?: number
    pageSize?: number
    searchText?: string
    sortField?: string
    sortOrder?: string
    userId?: number
    vehicleId?: number
  }

  type CommentReplyAddRequest = {
    commentId?: number
    content?: string
    images?: string
    parentId?: number
    replyToUserId?: number
  }

  type CommentReplyQueryRequest = {
    commentId?: number
    current?: number
    pageSize?: number
    parentId?: number
    replyToUserId?: number
    searchText?: string
    sortField?: string
    sortOrder?: string
    userId?: number
  }

  type CommentReplyUpdateRequest = {
    content?: string
    id?: number
    images?: string
  }

  type CommentReplyVO = {
    commentId?: number
    content?: string
    createTime?: string
    id?: number
    images?: string
    parentId?: number
    replyToUserId?: number
    replyToUserName?: string
    updateTime?: string
    userAvatar?: string
    userId?: number
    userName?: string
  }

  type CommentUpdateRequest = {
    content?: string
    driverRating?: number
    id?: number
    images?: string
    vehicleRating?: number
  }

  type CommentVO = {
    commentTime?: string
    content?: string
    createTime?: string
    driverId?: number
    driverName?: string
    driverRating?: number
    id?: number
    images?: string
    orderId?: number
    replyList?: CommentReplyVO[]
    userAvatar?: string
    userId?: number
    userName?: string
    vehicleId?: number
    vehicleName?: string
    vehicleRating?: number
  }

  type DeleteRequest = {
    id?: number
  }

  type DriverAddRequest = {
    age?: number
    dailyPrice?: number
    driverAvatar?: string
    driverLicenseExpireDate?: string
    driverLicenseImg?: string
    driverLicenseIssueDate?: string
    driverLicenseNo?: string
    driverLicenseType?: string
    driverName?: string
    drivingYears?: number
    gender?: number
    phoneNumber?: string
    workStatus?: number
  }

  type DriverQueryRequest = {
    current?: number
    driverLicenseType?: string
    driverName?: string
    gender?: number
    maxPrice?: number
    minPrice?: number
    pageSize?: number
    sortField?: string
    sortOrder?: string
    workStatus?: number
  }

  type DriverUpdateRequest = {
    age?: number
    dailyPrice?: number
    driverAvatar?: string
    driverLicenseExpireDate?: string
    driverLicenseImg?: string
    driverLicenseIssueDate?: string
    driverLicenseNo?: string
    driverLicenseType?: string
    driverName?: string
    drivingYears?: number
    gender?: number
    id?: number
    phoneNumber?: string
    workStatus?: number
  }

  type DriverVO = {
    age?: number
    createTime?: string
    dailyPrice?: number
    driverAvatar?: string
    driverLicenseExpireDate?: string
    driverLicenseImg?: string
    driverLicenseIssueDate?: string
    driverLicenseNo?: string
    driverLicenseType?: string
    driverName?: string
    drivingYears?: number
    gender?: number
    id?: number
    phoneNumber?: string
    rating?: number
    workStatus?: number
  }

  type EnergyTypeDictAddRequest = {
    typeName?: string
  }

  type EnergyTypeDictQueryRequest = {
    current?: number
    pageSize?: number
    sortField?: string
    sortOrder?: string
    typeName?: string
  }

  type EnergyTypeDictUpdateRequest = {
    id?: number
    typeName?: string
  }

  type EnergyTypeDictVO = {
    createTime?: string
    id?: number
    typeName?: string
  }

  type getCommentByIdUsingGETParams = {
    /** id */
    id: number
  }

  type getCommentReplyByIdUsingGETParams = {
    /** id */
    id: number
  }

  type getDriverVOByIdUsingGETParams = {
    /** id */
    id: number
  }

  type getEnergyTypeDictByIdUsingGETParams = {
    /** id */
    id: number
  }

  type getRentalOrderUsingGETParams = {
    /** orderId */
    orderId: number
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

  type getVehicleModelByIdUsingGETParams = {
    /** id */
    id: number
  }

  type getVehicleTypeDictByIdUsingGETParams = {
    /** id */
    id: number
  }

  type listCommentByVehicleIdUsingGETParams = {
    /** vehicleId */
    vehicleId: number
  }

  type listCommentReplyByCommentIdUsingGETParams = {
    /** commentId */
    commentId: number
  }

  type listStoreByCityNameUsingGETParams = {
    /** cityName */
    cityName: string
  }

  type listVehicleBrandByLetterUsingGETParams = {
    /** letter */
    letter: string
  }

  type LocalTime = {
    hour?: number
    minute?: number
    nano?: number
    second?: number
  }

  type LocalTime1 = {
    hour?: number
    minute?: number
    nano?: number
    second?: number
  }

  type LoginUserVO = {
    createTime?: string
    email?: string
    gender?: number
    id?: number
    memberLevel?: number
    phoneNumber?: string
    updateTime?: string
    userAccount?: string
    userAvatar?: string
    userName?: string
    userProfile?: string
    userRole?: string
  }

  type OrderItem = {
    asc?: boolean
    column?: string
  }

  type PageCommentReplyVO_ = {
    countId?: string
    current?: number
    maxLimit?: number
    optimizeCountSql?: boolean
    orders?: OrderItem[]
    pages?: number
    records?: CommentReplyVO[]
    searchCount?: boolean
    size?: number
    total?: number
  }

  type PageCommentVO_ = {
    countId?: string
    current?: number
    maxLimit?: number
    optimizeCountSql?: boolean
    orders?: OrderItem[]
    pages?: number
    records?: CommentVO[]
    searchCount?: boolean
    size?: number
    total?: number
  }

  type PageDriverVO_ = {
    countId?: string
    current?: number
    maxLimit?: number
    optimizeCountSql?: boolean
    orders?: OrderItem[]
    pages?: number
    records?: DriverVO[]
    searchCount?: boolean
    size?: number
    total?: number
  }

  type PageEnergyTypeDictVO_ = {
    countId?: string
    current?: number
    maxLimit?: number
    optimizeCountSql?: boolean
    orders?: OrderItem[]
    pages?: number
    records?: EnergyTypeDictVO[]
    searchCount?: boolean
    size?: number
    total?: number
  }

  type PageRentalOrderVO_ = {
    countId?: string
    current?: number
    maxLimit?: number
    optimizeCountSql?: boolean
    orders?: OrderItem[]
    pages?: number
    records?: RentalOrderVO[]
    searchCount?: boolean
    size?: number
    total?: number
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

  type PageVehicleBrowsingHistoryVO_ = {
    countId?: string
    current?: number
    maxLimit?: number
    optimizeCountSql?: boolean
    orders?: OrderItem[]
    pages?: number
    records?: VehicleBrowsingHistoryVO[]
    searchCount?: boolean
    size?: number
    total?: number
  }

  type PageVehicleFavoriteVO_ = {
    countId?: string
    current?: number
    maxLimit?: number
    optimizeCountSql?: boolean
    orders?: OrderItem[]
    pages?: number
    records?: VehicleFavoriteVO[]
    searchCount?: boolean
    size?: number
    total?: number
  }

  type PageVehicleModelVO_ = {
    countId?: string
    current?: number
    maxLimit?: number
    optimizeCountSql?: boolean
    orders?: OrderItem[]
    pages?: number
    records?: VehicleModelVO[]
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

  type payOrderUsingGETParams = {
    /** orderId */
    orderId: number
  }

  type pickupVehicleUsingGETParams = {
    /** orderId */
    orderId: number
  }

  type recommendVehicleUsingGETParams = {
    /** vehicleId */
    vehicleId: number
  }

  type refreshCaptchaUsingGETParams = {
    /** captchaKey */
    captchaKey: string
  }

  type RentalOrderAdminPageRequest = {
    actualReturnTime?: string
    actualStartTime?: string
    current?: number
    endTime?: string
    needDriver?: number
    orderNo?: string
    pageSize?: number
    paymentStatus?: number
    pickupStoreId?: number
    returnStoreId?: number
    sortField?: string
    sortOrder?: string
    startTime?: string
    status?: number
    totalDays?: number
    userId?: number
    userName?: string
    vehicleId?: number
    vehicleName?: string
  }

  type RentalOrderCancelRequest = {
    orderId?: number
  }

  type RentalOrderCreateRequest = {
    actualReturnTime?: string
    actualStartTime?: string
    driverId?: number
    endTime?: string
    needDriver?: number
    pickupStoreId?: number
    remark?: string
    returnStoreId?: number
    startTime?: string
    totalDays?: number
    vehicleId?: number
  }

  type RentalOrderPageRequest = {
    current?: number
    pageSize?: number
    searchText?: string
    sortField?: string
    sortOrder?: string
    status?: number
  }

  type RentalOrderVO = {
    actualReturnTime?: string
    actualStartTime?: string
    cancelReason?: string
    cancelTime?: string
    createTime?: string
    driverDailyPrice?: number
    driverId?: number
    driverName?: string
    driverTotalAmount?: number
    endTime?: string
    expireTime?: string
    id?: number
    needDriver?: number
    orderNo?: string
    paymentStatus?: number
    paymentTime?: string
    pickupStoreId?: number
    pickupStoreName?: string
    refundAmount?: number
    refundTime?: string
    remark?: string
    returnStoreId?: number
    returnStoreName?: string
    startTime?: string
    status?: number
    totalAmount?: number
    totalDays?: number
    userId?: number
    userName?: string
    vehicleDailyPrice?: number
    vehicleId?: number
    vehicleImage?: string
    vehicleName?: string
    vehicleTotalAmount?: number
  }

  type returnVehicleUsingGETParams = {
    /** orderId */
    orderId: number
  }

  type StoreCreateRequest = {
    address?: string
    cityName?: string
    closeTime?: LocalTime1
    images?: string
    latitude?: number
    longitude?: number
    mobile?: string
    openTime?: LocalTime1
    status?: number
    storeName?: string
  }

  type StoreQueryRequest = {
    address?: string
    cityName?: string
    closeTime?: LocalTime1
    contactPhone?: string
    current?: number
    id?: number
    openTime?: LocalTime1
    pageSize?: number
    sortField?: string
    sortOrder?: string
    status?: number
    storeName?: string
  }

  type StoreUpdateRequest = {
    address?: string
    cityName?: string
    closeTime?: LocalTime1
    id?: number
    images?: string
    latitude?: number
    longitude?: number
    mobile?: string
    openTime?: LocalTime1
    status?: number
    storeName?: string
  }

  type StoreVO = {
    address?: string
    cityName?: string
    closeTime?: LocalTime
    id?: number
    images?: string
    latitude?: number
    longitude?: number
    mobile?: string
    openTime?: LocalTime
    status?: number
    storeName?: string
  }

  type User = {
    createTime?: string
    email?: string
    gender?: number
    id?: number
    isDelete?: number
    memberLevel?: number
    phoneNumber?: string
    status?: number
    updateTime?: string
    userAccount?: string
    userAvatar?: string
    userName?: string
    userPassword?: string
    userProfile?: string
    userRole?: string
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
    userRole?: string
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
    userRole?: string
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
    userProfile?: string
    userRole?: string
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
    email?: string
    gender?: number
    phoneNumber?: string
    userName?: string
    userProfile?: string
  }

  type UserVO = {
    createTime?: string
    email?: string
    gender?: number
    id?: number
    memberLevel?: number
    phoneNumber?: string
    status?: number
    userAccount?: string
    userAvatar?: string
    userName?: string
    userProfile?: string
    userRole?: string
  }

  type VehicleAddRequest = {
    brandId?: number
    dailyPrice?: number
    description?: string
    energyTypeId?: number
    imageUrl?: string
    modelId?: number
    name?: string
    productionYear?: number
    seatCount?: number
    status?: number
    vehicleNo?: string
    vehicleTypeId?: number
  }

  type VehicleBrandAddRequest = {
    brandLogo?: string
    brandName?: string
    firstLetter?: string
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
    firstLetter?: string
    id?: number
  }

  type VehicleBrandVO = {
    brandLogo?: string
    brandName?: string
    createTime?: string
    firstLetter?: string
    id?: number
  }

  type VehicleBrowsingHistoryVO = {
    brandId?: number
    brandName?: string
    browseTime?: string
    dailyPrice?: number
    description?: string
    energyTypeId?: number
    energyTypeName?: string
    id?: number
    imageUrl?: string
    modelId?: number
    modelName?: string
    productionYear?: number
    seatCount?: number
    status?: number
    vehicleId?: number
    vehicleName?: string
    vehicleTypeId?: number
    vehicleTypeName?: string
  }

  type VehicleFavoriteAddRequest = {
    vehicleId?: number
  }

  type VehicleFavoriteCancelRequest = {
    vehicleId?: number
  }

  type VehicleFavoriteQueryRequest = {
    current?: number
    pageSize?: number
    searchText?: string
    sortField?: string
    sortOrder?: string
  }

  type VehicleFavoriteVO = {
    createTime?: string
    id?: number
    userId?: number
    vehicleId?: number
  }

  type VehicleModelAddRequest = {
    brandId?: number
    modelLogo?: string
    modelName?: string
  }

  type VehicleModelQueryRequest = {
    brandId?: number
    current?: number
    modelName?: string
    pageSize?: number
    sortField?: string
    sortOrder?: string
  }

  type VehicleModelUpdateRequest = {
    brandId?: number
    id?: number
    modelLogo?: string
    modelName?: string
  }

  type VehicleModelVO = {
    brandId?: number
    brandName?: string
    createTime?: string
    id?: number
    modelLogo?: string
    modelName?: string
  }

  type VehicleQueryRequest = {
    brandId?: number
    current?: number
    description?: string
    energyTypeId?: number
    id?: number
    maxDailyPrice?: number
    minDailyPrice?: number
    modelId?: number
    name?: string
    pageSize?: number
    searchText?: string
    seatCount?: number
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
    dailyPrice?: number
    description?: string
    energyTypeId?: number
    id?: number
    imageUrl?: string
    modelId?: number
    name?: string
    productionYear?: number
    seatCount?: number
    status?: number
    vehicleNo?: string
    vehicleTypeId?: number
  }

  type VehicleVO = {
    brandId?: number
    brandName?: string
    createTime?: string
    dailyPrice?: number
    description?: string
    energyTypeId?: number
    energyTypeName?: string
    id?: number
    imageUrl?: string
    modelId?: number
    modelName?: string
    name?: string
    productionYear?: number
    seatCount?: number
    status?: number
    vehicleNo?: string
    vehicleTypeId?: number
    vehicleTypeName?: string
  }
}
