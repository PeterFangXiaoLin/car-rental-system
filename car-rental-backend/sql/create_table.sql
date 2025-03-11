create database if not exists `car_system`;

use `car_system`;

drop table `user`;

-- 用户表
CREATE TABLE IF NOT EXISTS `user`
(
    `id`                      bigint AUTO_INCREMENT COMMENT 'id' PRIMARY KEY,
    `userAccount`             varchar(256)                           NOT NULL COMMENT '账号',
    `userPassword`            varchar(512)                           NOT NULL COMMENT '密码',
    `userName`                varchar(256)                           NULL COMMENT '用户昵称',
    `userAvatar`              varchar(1024)                          NULL COMMENT '用户头像',
    `gender`                  tinyint      DEFAULT 0                 NULL COMMENT '性别: 0-男 1-女',
    `userProfile`             varchar(512)                           NULL COMMENT '用户简介',
    `phoneNumber`             varchar(20)                            NULL COMMENT '手机号码',
    `email`                   varchar(100)                           NULL COMMENT '电子邮箱',
    `realName`                varchar(100)                           NULL COMMENT '真实姓名',
    `idCardNumber`            varchar(50)                            NULL COMMENT '身份证号码',
    `verifyStatus`            tinyint      DEFAULT 0                 NULL COMMENT '实名认证状态：0-未实名，1-认证中，2-已认证，3-认证失败',
    `driverLicenseNo`         varchar(50)                            NULL COMMENT '驾驶证号码',
    `driverLicenseType`       varchar(20)                            NULL COMMENT '驾照类型（A1/A2/B1/B2/C1等）',
    `driverLicenseIssueDate`  date                                   NULL COMMENT '驾照发证日期',
    `driverLicenseExpireDate` date                                   NULL COMMENT '驾照到期日期',
    `verifyResult`            tinyint                                NULL COMMENT '认证结果：0-待审核，1-通过，2-拒绝',
    `rejectReason`            varchar(500) DEFAULT                   NULL COMMENT '拒绝原因',
    `verifyTime`              datetime                               NULL COMMENT '认证时间',
    `reviewId`                bigint                                 NULL COMMENT '审核人ID',
    `reviewTime`              datetime                               NULL COMMENT '审核时间',
    `memberLevel`             tinyint      DEFAULT 0                 NOT NULL COMMENT '会员等级: 0-普通用户，1-vip',
    `userRole`                tinyint      DEFAULT 1                 NOT NULL COMMENT '用户角色：1-用户，2-司机，3-管理员',
    `isDriver`                tinyint      DEFAULT 0                 NOT NULL COMMENT '是否为司机：0-否，1-是',
    `status`                  tinyint      DEFAULT 1                 NOT NULL COMMENT '状态：0-禁用，1-启用',
    `editTime`                datetime     DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '编辑时间',
    `createTime`              datetime     DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '创建时间',
    `updateTime`              datetime     DEFAULT CURRENT_TIMESTAMP NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `isDelete`                tinyint      DEFAULT 0                 NOT NULL COMMENT '是否删除',
    UNIQUE KEY `uk_userAccount` (`userAccount`),
    UNIQUE KEY `uk_phoneNumber` (`phoneNumber`),
    UNIQUE KEY `uk_idCardNumber` (`idCardNumber`),
    UNIQUE KEY `uk_driverLicenseNo` (`driverLicenseNo`),
    INDEX `idx_userName` (`userName`),
    INDEX `idx_realName` (`realName`),
    INDEX `idx_userRole` (`userRole`),
    INDEX `idx_isDriver` (`isDriver`),
    INDEX `idx_verifyStatus` (`verifyStatus`),
    INDEX `idx_status` (`status`),
    INDEX `idx_memberLevel` (`memberLevel`)
) COMMENT '用户' COLLATE = utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `driver`
(
    `id`               bigint       NOT NULL AUTO_INCREMENT COMMENT '司机ID',
    `userId`           bigint       NOT NULL COMMENT '用户ID（关联用户表）',
    `driverName`       varchar(100) NOT NULL COMMENT '司机姓名',
    `driverLicenseNo`  varchar(50)  NOT NULL COMMENT '驾驶证号码',
    `driverLicenseImg` varchar(255)          DEFAULT NULL COMMENT '驾驶证照片URL',
    `idCardNo`         varchar(50)  NOT NULL COMMENT '身份证号码',
    `idCardFrontImg`   varchar(255)          DEFAULT NULL COMMENT '身份证正面照片URL',
    `idCardBackImg`    varchar(255)          DEFAULT NULL COMMENT '身份证背面照片URL',
    `phoneNumber`      varchar(20)  NOT NULL COMMENT '联系电话',
    `gender`           tinyint               DEFAULT '0' COMMENT '性别：0-男，1-女',
    `age`              int                   DEFAULT NULL COMMENT '年龄',
    `drivingYears`     int          NOT NULL COMMENT '驾龄',
    `driverType`       varchar(50)  NOT NULL COMMENT '驾照类型',
    `dailyPrice`       decimal(10, 2)        DEFAULT '0.00' COMMENT '日薪',
    `workStatus`       tinyint      NOT NULL DEFAULT '1' COMMENT '工作状态：0-休息中，1-可接单，2-已接单',
    `rating`           decimal(2, 1)         DEFAULT '5.0' COMMENT '评分（1-5分）',
    `ratingCount`      int                   DEFAULT '0' COMMENT '评价次数',
    `description`      varchar(500)          DEFAULT NULL COMMENT '个人简介',
    `avatarUrl`        varchar(255)          DEFAULT NULL COMMENT '头像URL',
    `createTime`       datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updateTime`       datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `isDelete`         tinyint      NOT NULL DEFAULT '0' COMMENT '是否删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_userId` (`userId`),
    UNIQUE KEY `uk_driverLicenseNo` (`driverLicenseNo`),
    UNIQUE KEY `uk_idCardNo` (`idCardNo`),
    KEY `idx_phoneNumber` (`phoneNumber`),
    KEY `idx_workStatus` (`workStatus`),
    KEY `idx_rating` (`rating`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='司机表';

CREATE TABLE IF NOT EXISTS `vehicle`
(
    `id`             bigint         NOT NULL AUTO_INCREMENT COMMENT '车辆ID',
    `vehicleNo`      varchar(50)    NULL COMMENT '车牌号',
    `brandId`        bigint         NOT NULL COMMENT '品牌ID',
    `modelId`        bigint         NOT NULL COMMENT '型号ID',
    `vehicleTypeId`  bigint                  DEFAULT NULL COMMENT '车型ID',
    `color`          varchar(50)    NULL COMMENT '颜色',
    `productionYear` int            NOT NULL COMMENT '生产年份',
    `dailyPrice`     decimal(10, 2) NOT NULL COMMENT '日租金',
    `deposit`        decimal(10, 2) NOT NULL COMMENT '押金',
    `mileage`        int                     DEFAULT '0' COMMENT '行驶里程',
    `status`         int            NOT NULL DEFAULT '0' COMMENT '状态：0-可用，1-已租出，2-维修中，3-报废',
    `imageUrl`       varchar(1024)           DEFAULT NULL COMMENT '车辆图片URL',
    `description`    text COMMENT '车辆描述',
    `createTime`     datetime       NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updateTime`     datetime       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `isDelete`       tinyint        NOT NULL DEFAULT '0' COMMENT '是否删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_vehicle_no` (`vehicleNo`),
    KEY `idx_brand_id` (`brandId`),
    KEY `idx_model_id` (`modelId`),
    KEY `idx_vehicle_type_id` (`vehicleTypeId`),
    KEY `idx_status` (`status`)
) COMMENT ='车辆表' collate = utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `vehicle_brand`
(
    `id`          bigint       NOT NULL AUTO_INCREMENT COMMENT '品牌ID',
    `brandName`   varchar(100) NOT NULL COMMENT '品牌名称',
    `brandLogo`   varchar(255)          DEFAULT NULL COMMENT '品牌logo',
    `description` varchar(500)          DEFAULT NULL COMMENT '品牌描述',
    `createTime`  datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updateTime`  datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `isDelete`    tinyint      NOT NULL DEFAULT '0' COMMENT '是否删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_brand_name` (`brandName`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='车辆品牌表'
  collate = utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `vehicle_model`
(
    `id`         bigint       NOT NULL AUTO_INCREMENT COMMENT '型号ID',
    `brandId`    bigint       NOT NULL COMMENT '品牌ID',
    `modelName`  varchar(100) NOT NULL COMMENT '型号名称',
    `createTime` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updateTime` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `isDelete`   tinyint      NOT NULL DEFAULT '0' COMMENT '是否删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_brand_model` (`brandId`, `modelName`),
    KEY `idx_brand_id` (`brandId`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='车辆型号表'
  collate = utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `vehicle_type_dict`
(
    `id`         bigint       NOT NULL AUTO_INCREMENT COMMENT '车型ID',
    `typeName`   varchar(100) NOT NULL COMMENT '车型名称',
    `createTime` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updateTime` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `isDelete`   tinyint      NOT NULL DEFAULT '0' COMMENT '是否删除',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='车型字典表'
  collate = utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `rental_order`
(
    `id`                bigint         NOT NULL AUTO_INCREMENT COMMENT '订单ID',
    `orderNo`           varchar(50)    NOT NULL COMMENT '订单编号',
    `userId`            bigint         NOT NULL COMMENT '用户ID',
    `vehicleId`         bigint         NOT NULL COMMENT '车辆ID',
    `startTime`         datetime       NOT NULL COMMENT '租赁开始时间',
    `endTime`           datetime       NOT NULL COMMENT '租赁结束时间',
    `actualReturnTime`  datetime                DEFAULT NULL COMMENT '实际归还时间',
    `dailyPrice`        decimal(10, 2) NOT NULL COMMENT '日租金',
    `totalDays`         int            NOT NULL COMMENT '租赁天数',
    `totalAmount`       decimal(10, 2) NOT NULL COMMENT '总金额',
    `deposit`           decimal(10, 2) NOT NULL COMMENT '押金',
    `needDriver`        tinyint        NOT NULL DEFAULT '0' COMMENT '是否需要司机：0-不需要，1-需要',
    `driverId`          bigint                  DEFAULT NULL COMMENT '司机ID',
    `driverPrice`       decimal(10, 2)          DEFAULT '0.00' COMMENT '司机服务费用/天',
    `driverTotalAmount` decimal(10, 2)          DEFAULT '0.00' COMMENT '司机服务总费用',
    `status`            int            NOT NULL DEFAULT '0' COMMENT '订单状态：0-待支付，1-已支付待取车，2-已取车，3-已还车，4-已完成，5-已取消',
    `paymentStatus`     int            NOT NULL DEFAULT '0' COMMENT '支付状态：0-未支付，1-已支付，2-已退款',
    `paymentTime`       datetime                DEFAULT NULL COMMENT '支付时间',
    `paymentMethod`     varchar(50)             DEFAULT NULL COMMENT '支付方式',
    `transactionId`     varchar(100)            DEFAULT NULL COMMENT '支付交易号',
    `pickupLocation`    varchar(255)            DEFAULT NULL COMMENT '取车地点',
    `returnLocation`    varchar(255)            DEFAULT NULL COMMENT '还车地点',
    `remark`            varchar(500)            DEFAULT NULL COMMENT '备注',
    `createTime`        datetime       NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updateTime`        datetime       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `isDelete`          tinyint        NOT NULL DEFAULT '0' COMMENT '是否删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_orderNo` (`orderNo`),
    KEY `idx_userId` (`userId`),
    KEY `idx_vehicleId` (`vehicleId`),
    KEY `idx_driverId` (`driverId`),
    KEY `idx_status` (`status`),
    KEY `idx_paymentStatus` (`paymentStatus`),
    KEY `idx_needDriver` (`needDriver`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='租赁订单表';
-- 支付记录表
CREATE TABLE IF NOT EXISTS `payment_record`
(
    `id`            bigint         NOT NULL AUTO_INCREMENT COMMENT '支付记录ID',
    `orderId`       bigint         NOT NULL COMMENT '订单ID',
    `userId`        bigint         NOT NULL COMMENT '用户ID',
    `paymentNo`     varchar(100)   NOT NULL COMMENT '支付流水号',
    `transactionId` varchar(100)            DEFAULT NULL COMMENT '第三方交易号',
    `paymentMethod` varchar(50)    NOT NULL COMMENT '支付方式',
    `amount`        decimal(10, 2) NOT NULL COMMENT '支付金额',
    `status`        tinyint        NOT NULL DEFAULT '0' COMMENT '状态：0-待支付，1-支付成功，2-支付失败，3-已退款',
    `paymentTime`   datetime                DEFAULT NULL COMMENT '支付时间',
    `refundTime`    datetime                DEFAULT NULL COMMENT '退款时间',
    `refundAmount`  decimal(10, 2)          DEFAULT NULL COMMENT '退款金额',
    `refundReason`  varchar(500)            DEFAULT NULL COMMENT '退款原因',
    `createTime`    datetime       NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updateTime`    datetime       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `isDelete`      tinyint        NOT NULL DEFAULT '0' COMMENT '是否删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_paymentNo` (`paymentNo`),
    KEY `idx_orderId` (`orderId`),
    KEY `idx_userId` (`userId`),
    KEY `idx_status` (`status`),
    KEY `idx_transactionId` (`transactionId`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='支付记录表';

CREATE TABLE IF NOT EXISTS `driver`
(
    `id`               bigint       NOT NULL AUTO_INCREMENT COMMENT '司机ID',
    `userId`           bigint       NOT NULL COMMENT '用户ID（关联用户表）',
    `driverName`       varchar(100) NOT NULL COMMENT '司机姓名',
    `driverLicenseNo`  varchar(50)  NOT NULL COMMENT '驾驶证号码',
    `driverLicenseImg` varchar(255)          DEFAULT NULL COMMENT '驾驶证照片URL',
    `idCardNo`         varchar(50)  NOT NULL COMMENT '身份证号码',
    `idCardFrontImg`   varchar(255)          DEFAULT NULL COMMENT '身份证正面照片URL',
    `idCardBackImg`    varchar(255)          DEFAULT NULL COMMENT '身份证背面照片URL',
    `phoneNumber`      varchar(20)  NOT NULL COMMENT '联系电话',
    `gender`           tinyint               DEFAULT '0' COMMENT '性别：0-男，1-女',
    `age`              int                   DEFAULT NULL COMMENT '年龄',
    `drivingYears`     int          NOT NULL COMMENT '驾龄',
    `driverType`       varchar(50)  NOT NULL COMMENT '驾照类型',
    `dailyPrice`       decimal(10, 2)        DEFAULT '0.00' COMMENT '日薪',
    `workStatus`       tinyint      NOT NULL DEFAULT '1' COMMENT '工作状态：0-休息中，1-可接单，2-已接单',
    `rating`           decimal(2, 1)         DEFAULT '5.0' COMMENT '评分（1-5分）',
    `description`      varchar(500)          DEFAULT NULL COMMENT '个人简介',
    `avatarUrl`        varchar(255)          DEFAULT NULL COMMENT '头像URL',
    `verifyStatus`     tinyint      NOT NULL DEFAULT '0' COMMENT '认证状态：0-未认证，1-认证中，2-已认证，3-认证失败',
    `verifyTime`       datetime              DEFAULT NULL COMMENT '认证时间',
    `rejectReason`     varchar(500)          DEFAULT NULL COMMENT '拒绝原因',
    `createTime`       datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updateTime`       datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `isDelete`         tinyint      NOT NULL DEFAULT '0' COMMENT '是否删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_userId` (`userId`),
    UNIQUE KEY `uk_driverLicenseNo` (`driverLicenseNo`),
    UNIQUE KEY `uk_idCardNo` (`idCardNo`),
    KEY `idx_phoneNumber` (`phoneNumber`),
    KEY `idx_workStatus` (`workStatus`),
    KEY `idx_verifyStatus` (`verifyStatus`),
    KEY `idx_rating` (`rating`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='司机表';

CREATE TABLE IF NOT EXISTS `driver_schedule`
(
    `id`           bigint   NOT NULL AUTO_INCREMENT COMMENT '排班ID',
    `driverId`     bigint   NOT NULL COMMENT '司机ID',
    `scheduleDate` date     NOT NULL COMMENT '排班日期',
    `startTime`    time     NOT NULL COMMENT '开始时间',
    `endTime`      time     NOT NULL COMMENT '结束时间',
    `status`       tinyint  NOT NULL DEFAULT '1' COMMENT '状态：0-休息，1-工作中，2-已预约',
    `orderId`      bigint            DEFAULT NULL COMMENT '关联订单ID（如果已预约）',
    `createTime`   datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updateTime`   datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `isDelete`     tinyint  NOT NULL DEFAULT '0' COMMENT '是否删除',
    PRIMARY KEY (`id`),
    KEY `idx_driverId` (`driverId`),
    KEY `idx_scheduleDate` (`scheduleDate`),
    KEY `idx_status` (`status`),
    KEY `idx_orderId` (`orderId`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='司机排班表';

drop table `verify_record`;

CREATE TABLE IF NOT EXISTS `verify_record`
(
    `id`                      bigint       NOT NULL AUTO_INCREMENT COMMENT '记录ID',
    `userId`                  bigint       NOT NULL COMMENT '用户ID',
    `userAccount`             varchar(256) NOT NULL COMMENT '账号',
    `verifyStatus`            tinyint      NOT NULL COMMENT '认证状态：0-未认证，1-认证中，2-已认证，3-认证失败',
    `realName`                varchar(100) NULL COMMENT '真实姓名',
    `idCardNumber`            varchar(50)  NULL COMMENT '身份证号码',
    `driverLicenseNo`         varchar(50)  NULL COMMENT '驾驶证号码',
    `driverLicenseType`       varchar(20)  NULL COMMENT '驾照类型（A1/A2/B1/B2/C1等）',
    `driverLicenseIssueDate`  date         NULL COMMENT '驾照发证日期',
    `driverLicenseExpireDate` date         NULL COMMENT '驾照到期日期',
    `drivingYears`            int          NULL COMMENT '驾龄',
    `verifyResult`            tinyint               DEFAULT 0 NOT NULL COMMENT '认证结果：0-待审核，1-通过，2-拒绝',
    `rejectReason`            varchar(500)          DEFAULT NULL COMMENT '拒绝原因',
    `verifyTime`              datetime     NOT NULL COMMENT '认证时间',
    `reviewId`                bigint       NOT NULL COMMENT '审核人ID',
    `reviewTime`              datetime     NULL COMMENT '审核时间',
    `createTime`              datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updateTime`              datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_userId` (`userId`),
    KEY `idx_verifyStatus` (`verifyStatus`),
    KEY `idx_reviewId` (`reviewId`),
    KEY `idx_verifyTime` (`verifyTime`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='认证记录表';

CREATE TABLE IF NOT EXISTS `city`
(
    `id`           bigint         NOT NULL AUTO_INCREMENT COMMENT '城市ID',
    `cityName`     varchar(50)    NOT NULL COMMENT '城市名称',
    `provinceName` varchar(50)    NOT NULL COMMENT '省份名称',
    `adcode`       varchar(20)    NOT NULL COMMENT '行政区划编码',
    `citycode`     varchar(20)    NOT NULL COMMENT '城市编码',
    `level`        varchar(20)    NOT NULL COMMENT '行政级别（province/city/district）',
    `longitude`    decimal(10, 6) NOT NULL COMMENT '中心点经度',
    `latitude`     decimal(10, 6) NOT NULL COMMENT '中心点纬度',
    `status`       tinyint        NOT NULL DEFAULT '1' COMMENT '状态：0-禁用，1-启用',
    `sortOrder`    int                     DEFAULT '0' COMMENT '排序',
    `createTime`   datetime       NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updateTime`   datetime       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `isDelete`     tinyint        NOT NULL DEFAULT '0' COMMENT '是否删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_adcode` (`adcode`),
    KEY `idx_cityName` (`cityName`),
    KEY `idx_provinceName` (`provinceName`),
    KEY `idx_status` (`status`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='城市表';

CREATE TABLE IF NOT EXISTS `store`
(
    `id`            bigint       NOT NULL AUTO_INCREMENT COMMENT '门店ID',
    `storeName`     varchar(100) NOT NULL COMMENT '门店名称',
    `address`       varchar(255) NOT NULL COMMENT '门店地址',
    `province`      varchar(50)  NOT NULL COMMENT '省份',
    `city`          varchar(50)  NOT NULL COMMENT '城市',
    `district`      varchar(50)  NOT NULL COMMENT '区县',
    `adcode`        varchar(20)  NOT NULL COMMENT '行政区划编码（高德地图）',
    `citycode`      varchar(20)  NOT NULL COMMENT '城市编码（高德地图）',
    `longitude`     decimal(10, 6)        DEFAULT NULL COMMENT '经度',
    `latitude`      decimal(10, 6)        DEFAULT NULL COMMENT '纬度',
    `contactPhone`  varchar(20)  NOT NULL COMMENT '联系电话',
    `businessHours` varchar(100)          DEFAULT NULL COMMENT '营业时间',
    `status`        tinyint      NOT NULL DEFAULT '1' COMMENT '状态：0-关闭，1-营业中',
    `images`        text                  DEFAULT NULL COMMENT '门店图片URL，多个用逗号分隔',
    `description`   varchar(500)          DEFAULT NULL COMMENT '门店描述',
    `createTime`    datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updateTime`    datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `isDelete`      tinyint      NOT NULL DEFAULT '0' COMMENT '是否删除',
    PRIMARY KEY (`id`),
    KEY `idx_city` (`city`),
    KEY `idx_adcode` (`adcode`),
    KEY `idx_citycode` (`citycode`),
    KEY `idx_status` (`status`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='门店表';

