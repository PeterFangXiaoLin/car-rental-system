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

  type BaseResponseListEnergyTypeDictVO_ = {
    code?: number
    data?: EnergyTypeDictVO[]
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

  type getDriverVOByIdUsingGETParams = {
    /** id */
    id: number
  }

  type getEnergyTypeDictByIdUsingGETParams = {
    /** id */
    id: number
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

  type listVehicleBrandByLetterUsingGETParams = {
    /** letter */
    letter: string
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

  type refreshCaptchaUsingGETParams = {
    /** captchaKey */
    captchaKey: string
  }

  type RentalOrderCreateRequest = {
    driverId?: number
    endTime?: string
    needDriver?: number
    pickupLocation?: string
    remark?: string
    returnLocation?: string
    startTime?: string
    totalDays?: number
    vehicleId?: number
  }

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
