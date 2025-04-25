create database if not exists `car_system`;

use `car_system`;

drop table if exists `vehicle`;

CREATE TABLE IF NOT EXISTS `vehicle`
(
    `id`             bigint         NOT NULL AUTO_INCREMENT COMMENT '车辆ID',
    `name`           varchar(100)   NOT NULL COMMENT '车辆名称',
    `vehicleNo`      varchar(50)    NULL COMMENT '车牌号',
    `brandId`        bigint         NOT NULL COMMENT '品牌ID',
    `modelId`        bigint         NOT NULL COMMENT '型号ID',
    `energyTypeId`   bigint         NOT NULL COMMENT '能源类型ID',
    `vehicleTypeId`  bigint                  DEFAULT NULL COMMENT '车型ID',
    `productionYear` int            NOT NULL COMMENT '生产年份',
    `dailyPrice`     decimal(10, 2) NOT NULL COMMENT '日租金',
    `seatCount`      int            NOT NULL COMMENT '座位数',
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

drop table if exists `energy_type_dict`;

CREATE TABLE IF NOT EXISTS `energy_type_dict`
(
    `id`         bigint       NOT NULL AUTO_INCREMENT COMMENT '能源类型ID',
    `typeName`   varchar(100) NOT NULL COMMENT '能源类型名称',
    `createTime` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updateTime` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `isDelete`   tinyint      NOT NULL DEFAULT '0' COMMENT '是否删除',
    PRIMARY KEY (`id`)
) COMMENT ='能源类型字典表' collate = utf8mb4_unicode_ci;


drop table if exists `vehicle_brand`;

CREATE TABLE IF NOT EXISTS `vehicle_brand`
(
    `id`          bigint       NOT NULL AUTO_INCREMENT COMMENT '品牌ID',
    `brandName`   varchar(100) NOT NULL COMMENT '品牌名称',
    `brandLogo`   varchar(255)          DEFAULT NULL COMMENT '品牌logo',
    `firstLetter` varchar(5)            DEFAULT NULL COMMENT '首字母',
    `createTime`  datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updateTime`  datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `isDelete`    tinyint      NOT NULL DEFAULT '0' COMMENT '是否删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_brand_name` (`brandName`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='车辆品牌表'
  collate = utf8mb4_unicode_ci;

drop table if exists `vehicle_model`;

CREATE TABLE IF NOT EXISTS `vehicle_model`
(
    `id`         bigint       NOT NULL AUTO_INCREMENT COMMENT '型号ID',
    `brandId`    bigint       NOT NULL COMMENT '品牌ID',
    `modelName`  varchar(100) NOT NULL COMMENT '型号名称',
    `modelLogo`  varchar(255)          DEFAULT NULL COMMENT '型号logo',
    `createTime` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updateTime` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `isDelete`   tinyint      NOT NULL DEFAULT '0' COMMENT '是否删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_brand_model` (`brandId`, `modelName`),
    KEY `idx_brand_id` (`brandId`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='车辆型号表'
  collate = utf8mb4_unicode_ci;

drop table if exists `vehicle_type_dict`;

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

drop table if exists `rental_order`;

CREATE TABLE IF NOT EXISTS `rental_order`
(
    `id`                  bigint         NOT NULL AUTO_INCREMENT COMMENT '订单ID',
    `orderNo`             varchar(50)    NOT NULL COMMENT '订单编号',
    `userId`              bigint         NOT NULL COMMENT '用户ID',
    `vehicleId`           bigint         NOT NULL COMMENT '车辆ID',
    `startTime`           datetime       NOT NULL COMMENT '租赁开始时间',
    `endTime`             datetime       NOT NULL COMMENT '租赁结束时间',
    `actualStartTime`     datetime                DEFAULT NULL COMMENT '实际取车时间',
    `actualReturnTime`    datetime                DEFAULT NULL COMMENT '实际归还时间',
    `dailyPrice`          decimal(10, 2) NOT NULL COMMENT '日租金',
    `totalDays`           int            NOT NULL COMMENT '租赁天数',
    `totalAmount`         decimal(10, 2) NOT NULL COMMENT '总金额',
    `needDriver`          tinyint        NOT NULL DEFAULT '0' COMMENT '是否需要司机：0-不需要，1-需要',
    `driverId`            bigint                  DEFAULT NULL COMMENT '司机ID',
    `driverPrice`         decimal(10, 2)          DEFAULT '0.00' COMMENT '司机服务费用/天',
    `driverTotalAmount`   decimal(10, 2)          DEFAULT '0.00' COMMENT '司机服务总费用',
    `status`              int            NOT NULL DEFAULT '0' COMMENT '订单状态：0-待支付，1-已支付待取车，2-已取车，3-已还车，4-已完成，5-已取消',
    `paymentStatus`       int            NOT NULL DEFAULT '0' COMMENT '支付状态：0-未支付，1-已支付，2-已退款，3-部分退款',
    `paymentTime`         datetime                DEFAULT NULL COMMENT '支付时间',
    `refundAmount`        decimal(10, 2)          DEFAULT NULL COMMENT '退款金额',
    `refundTime`          datetime                DEFAULT NULL COMMENT '退款时间',
    `pickupLocation`      varchar(255)            DEFAULT NULL COMMENT '取车地点',
    `returnLocation`      varchar(255)            DEFAULT NULL COMMENT '还车地点',
    `cancelReason`        varchar(255)            DEFAULT NULL COMMENT '取消原因',
    `cancelTime`          datetime                DEFAULT NULL COMMENT '取消时间',
    `expireTime`          datetime                DEFAULT NULL COMMENT '订单支付过期时间',
    `remark`              varchar(500)            DEFAULT NULL COMMENT '备注',
    `createTime`          datetime       NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updateTime`          datetime       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `isDelete`            tinyint        NOT NULL DEFAULT '0' COMMENT '是否删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_orderNo` (`orderNo`),
    KEY `idx_userId` (`userId`),
    KEY `idx_vehicleId` (`vehicleId`),
    KEY `idx_driverId` (`driverId`),
    KEY `idx_status` (`status`),
    KEY `idx_paymentStatus` (`paymentStatus`),
    KEY `idx_startTime` (`startTime`),
    KEY `idx_endTime` (`endTime`),
    KEY `idx_createTime` (`createTime`),
    KEY `idx_needDriver` (`needDriver`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='租赁订单表';

-- 评论表
CREATE TABLE IF NOT EXISTS `vehicle_comment`
(
    `id`             bigint       NOT NULL AUTO_INCREMENT COMMENT '评论ID',
    `orderId`        bigint       NOT NULL COMMENT '订单ID',
    `userId`         bigint       NOT NULL COMMENT '用户ID',
    `vehicleId`      bigint       NOT NULL COMMENT '车辆ID',
    `driverId`       bigint                DEFAULT NULL COMMENT '司机ID',
    `content`        varchar(1000) NOT NULL COMMENT '评论内容',
    `vehicleRating`  int          NOT NULL COMMENT '车辆评分(1-5星)',
    `driverRating`   int                   DEFAULT NULL COMMENT '司机评分(1-5星)',
    `commentTime`    datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '评论时间',
    `images`         text                  DEFAULT NULL COMMENT '评论图片URL，多个以逗号分隔',
    `createTime`     datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updateTime`     datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `isDelete`       tinyint      NOT NULL DEFAULT '0' COMMENT '是否删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_orderId` (`orderId`),
    KEY `idx_userId` (`userId`),
    KEY `idx_vehicleId` (`vehicleId`),
    KEY `idx_driverId` (`driverId`),
    KEY `idx_createTime` (`createTime`),
    KEY `idx_vehicleRating` (`vehicleRating`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='车辆评论表';

-- 评论回复表
CREATE TABLE IF NOT EXISTS `comment_reply`
(
    `id`             bigint       NOT NULL AUTO_INCREMENT COMMENT '回复ID',
    `commentId`      bigint       NOT NULL COMMENT '评论ID',
    `userId`         bigint       NOT NULL COMMENT '回复用户ID',
    `content`        varchar(500) NOT NULL COMMENT '回复内容',
    `parentId`       bigint                DEFAULT NULL COMMENT '父回复ID（回复其他回复时使用）',
    `replyToUserId`  bigint                DEFAULT NULL COMMENT '被回复用户ID',
    `images`         text                  DEFAULT NULL COMMENT '回复图片URL，多个以逗号分隔',
    `createTime`     datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updateTime`     datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `isDelete`       tinyint      NOT NULL DEFAULT '0' COMMENT '是否删除',
    PRIMARY KEY (`id`),
    KEY `idx_commentId` (`commentId`),
    KEY `idx_userId` (`userId`),
    KEY `idx_parentId` (`parentId`),
    KEY `idx_replyToUserId` (`replyToUserId`),
    KEY `idx_createTime` (`createTime`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='评论回复表';

-- 支付记录表
CREATE TABLE IF NOT EXISTS `payment_record`
(
    `id`             bigint         NOT NULL AUTO_INCREMENT COMMENT '支付记录ID',
    `orderId`        bigint         NOT NULL COMMENT '订单ID',
    `userId`         bigint         NOT NULL COMMENT '用户ID',
    `paymentNo`      varchar(100)   NOT NULL COMMENT '支付流水号（交易号）',
    `amount`         decimal(10, 2) NOT NULL COMMENT '支付金额',
    `status`         tinyint        NOT NULL DEFAULT '0' COMMENT '状态：0-待支付，1-支付成功，2-支付失败，3-已退款',
    `paymentTime`    datetime                DEFAULT NULL COMMENT '支付时间',
    `refundTime`     datetime                DEFAULT NULL COMMENT '退款时间',
    `refundAmount`   decimal(10, 2)          DEFAULT NULL COMMENT '退款金额',
    `refundReason`   varchar(500)            DEFAULT NULL COMMENT '退款原因',
    `createTime`     datetime       NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updateTime`     datetime       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `isDelete`       tinyint        NOT NULL DEFAULT '0' COMMENT '是否删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_paymentNo` (`paymentNo`),
    KEY `idx_orderId` (`orderId`),
    KEY `idx_userId` (`userId`),
    KEY `idx_status` (`status`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='支付记录表';

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

drop table if exists `city`;

CREATE TABLE IF NOT EXISTS `city`
(
    `id`         bigint         NOT NULL AUTO_INCREMENT COMMENT '城市ID',
    `name`       varchar(50)    NOT NULL COMMENT '城市名称',
    `adcode`     varchar(20)    NOT NULL COMMENT '行政区划编码',
    `citycode`   varchar(20)    NOT NULL COMMENT '城市编码',
    `longitude`  decimal(10, 6) NOT NULL COMMENT '中心点经度',
    `latitude`   decimal(10, 6) NOT NULL COMMENT '中心点纬度',
    `createTime` datetime       NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updateTime` datetime       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `isDelete`   tinyint        NOT NULL DEFAULT '0' COMMENT '是否删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_adcode` (`adcode`),
    KEY `idx_cityName` (`name`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='城市表';

drop table if exists `store`;

CREATE TABLE IF NOT EXISTS `store`
(
    `id`         bigint       NOT NULL AUTO_INCREMENT COMMENT '门店ID',
    `storeName`  varchar(100) NOT NULL COMMENT '门店名称',
    `address`    varchar(255) NOT NULL COMMENT '门店地址',
    `cityName`       varchar(50)  NOT NULL COMMENT '门店所在城市',
    `longitude`  decimal(10, 6)        DEFAULT NULL COMMENT '经度',
    `latitude`   decimal(10, 6)        DEFAULT NULL COMMENT '纬度',
    `mobile`     varchar(30)  NOT NULL COMMENT '联系电话',
    `openTime`   time         NOT NULL DEFAULT '09:00:00' COMMENT '开始营业时间',
    `closeTime`  time         NOT NULL DEFAULT '21:00:00' COMMENT '结束营业时间',
    `status`     tinyint      NOT NULL DEFAULT '1' COMMENT '状态：0-关闭，1-营业中',
    `images`     text                  DEFAULT NULL COMMENT '门店图片URL，多个用逗号分隔',
    `createTime` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updateTime` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `isDelete`   tinyint      NOT NULL DEFAULT '0' COMMENT '是否删除',
    PRIMARY KEY (`id`),
    KEY `idx_status` (`status`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='门店表';

drop table if exists `driver`;

-- 司机表
CREATE TABLE IF NOT EXISTS `driver`
(
    `id`                      bigint       NOT NULL AUTO_INCREMENT COMMENT '司机ID',
    `driverName`              varchar(100) NOT NULL COMMENT '司机姓名',
    `driverAvatar`            varchar(255)          DEFAULT NULL COMMENT '头像URL',
    `gender`                  tinyint               DEFAULT '0' COMMENT '性别：0-男，1-女',
    `age`                     int                   DEFAULT NULL COMMENT '年龄',
    `phoneNumber`             varchar(20)  NOT NULL COMMENT '联系电话',
    `driverLicenseNo`         varchar(50)  NOT NULL COMMENT '驾驶证号码',
    `driverLicenseType`       varchar(20)  NOT NULL COMMENT '驾照类型（A1/A2/B1/B2/C1等）',
    `driverLicenseImg`        varchar(255)          DEFAULT NULL COMMENT '驾驶证照片URL',
    `driverLicenseIssueDate`  date         NOT NULL COMMENT '驾照发证日期',
    `driverLicenseExpireDate` date         NOT NULL COMMENT '驾照到期日期',
    `drivingYears`            int          NOT NULL COMMENT '驾龄',
    `dailyPrice`              decimal(10, 2)        DEFAULT '0.00' COMMENT '日薪',
    `workStatus`              tinyint      NOT NULL DEFAULT '0' COMMENT '工作状态：0-休息中，1-可接单，2-已接单',
    `rating`                  decimal(2, 1)         DEFAULT '5.0' COMMENT '评分（1-5分）',
    `status`                  tinyint      NOT NULL DEFAULT '1' COMMENT '状态：0-禁用，1-启用',
    `createTime`              datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updateTime`              datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `isDelete`                tinyint      NOT NULL DEFAULT '0' COMMENT '是否删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_driverLicenseNo` (`driverLicenseNo`),
    UNIQUE KEY `uk_phoneNumber` (`phoneNumber`),
    KEY `idx_driverName` (`driverName`),
    KEY `idx_workStatus` (`workStatus`),
    KEY `idx_rating` (`rating`),
    KEY `idx_status` (`status`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='司机表';

drop table if exists `user`;

CREATE TABLE IF NOT EXISTS `user`
(
    `id`           bigint AUTO_INCREMENT COMMENT 'id' PRIMARY KEY,
    `userAccount`  varchar(256)                          NOT NULL COMMENT '账号',
    `userPassword` varchar(512)                          NOT NULL COMMENT '密码',
    `userName`     varchar(256)                          NULL COMMENT '用户昵称',
    `userAvatar`   varchar(1024)                         NULL COMMENT '用户头像',
    `gender`       tinyint     DEFAULT 0                 NULL COMMENT '性别: 0-男 1-女',
    `userProfile`  varchar(512)                          NULL COMMENT '用户简介',
    `phoneNumber`  varchar(20)                           NULL COMMENT '手机号码',
    `email`        varchar(100)                          NULL COMMENT '电子邮箱',
    `memberLevel`  tinyint     DEFAULT 0                 NOT NULL COMMENT '会员等级: 0-普通用户，1-vip',
    `userRole`     varchar(20) DEFAULT 'user'            NOT NULL COMMENT '用户角色: user-普通用户, admin-管理员',
    `status`       tinyint     DEFAULT 1                 NOT NULL COMMENT '状态：0-禁用，1-启用',
    `createTime`   datetime    DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '创建时间',
    `updateTime`   datetime    DEFAULT CURRENT_TIMESTAMP NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `isDelete`     tinyint     DEFAULT 0                 NOT NULL COMMENT '是否删除',
    UNIQUE KEY `uk_userAccount` (`userAccount`),
    UNIQUE KEY `uk_phoneNumber` (`phoneNumber`),
    INDEX `idx_userName` (`userName`),
    INDEX `idx_userRole` (`userRole`),
    INDEX `idx_status` (`status`),
    INDEX `idx_memberLevel` (`memberLevel`)
) COMMENT '用户' COLLATE = utf8mb4_unicode_ci;


CREATE TABLE `vehicle_browsing_history`
(
    `id`         bigint   NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `userId`     bigint   NOT NULL COMMENT '用户ID',
    `vehicleId`  bigint   NOT NULL COMMENT '车辆ID',
    `browseTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '浏览时间',
    `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `isDelete`   tinyint  NOT NULL DEFAULT '0' COMMENT '是否删除(0-未删, 1-已删)',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`userId`),
    KEY `idx_vehicle_id` (`vehicleId`),
    KEY `idx_create_time` (`createTime`),
    UNIQUE KEY `uk_user_vehicle` (`userId`, `vehicleId`, `isDelete`) COMMENT '同一用户对同一车辆的浏览记录唯一'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='车辆浏览历史表';

CREATE TABLE `vehicle_favorite`
(
    `id`          bigint   NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `userId`     bigint   NOT NULL COMMENT '用户ID',
    `vehicleId`  bigint   NOT NULL COMMENT '车辆ID',
    `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `isDelete`   tinyint  NOT NULL DEFAULT '0' COMMENT '是否删除(0-未删, 1-已删)',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`userId`),
    KEY `idx_vehicle_id` (`vehicleId`),
    KEY `idx_create_time` (`createTime`),
    UNIQUE KEY `uk_user_vehicle` (userId, `vehicleId`, `isDelete`) COMMENT '同一用户对同一车辆的收藏记录唯一'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='车辆收藏表';