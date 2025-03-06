create database if not exists `car_system`;

use `car_system`;

drop table `user`;

-- 用户表
create table if not exists user
(
    id               bigint auto_increment comment 'id' primary key,
    userAccount      varchar(256)                       not null comment '账号',
    userPassword     varchar(512)                       not null comment '密码',
    userName         varchar(256)                       null comment '用户昵称',
    userAvatar       varchar(1024)                      null comment '用户头像',
    gender           tinyint    default 0                       null comment '性别: 0-男 1-女',
    userProfile      varchar(512)                       null comment '用户简介',
    realName         varchar(100)                       null comment '真实姓名',
    idCardNumber     varchar(50)                        null comment '身份证号码',
    phoneNumber      varchar(20)                        null comment '手机号码',
    email            varchar(100)                       null comment '电子邮箱',
    drivingLicenseNo varchar(50)                        null comment '驾驶证号码',
    drivingYears     int                                null comment '驾龄',
    creditScore      int      default 10                not null comment '信用评分',
    memberLevel      int      default 0                 not null comment '会员等级: 0-普通用户，1-vip',
    userRole         int      default 1                 not null comment '用户角色：1-用户，2-司机，3-管理员',
    editTime         datetime default CURRENT_TIMESTAMP not null comment '编辑时间',
    createTime       datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime       datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete         tinyint  default 0                 not null comment '是否删除',
    UNIQUE KEY uk_userAccount (userAccount),
    UNIQUE KEY uk_phoneNumber (phoneNumber),
    UNIQUE KEY uk_idCardNumber (idCardNumber),
    INDEX idx_userName (userName),
    INDEX idx_realName (realName),
    INDEX idx_creditScore (creditScore)
) comment '用户' collate = utf8mb4_unicode_ci;

CREATE TABLE if not exists `vehicle`
(
    `id`             bigint         NOT NULL AUTO_INCREMENT COMMENT '车辆ID',
    `vehicle_no`     varchar(50)    NULL COMMENT '车牌号',
    `brand`          varchar(100)   NULL COMMENT '品牌',
    `model`          varchar(100)   NULL COMMENT '型号',
    `vehicle_type`   varchar(50)    NULL COMMENT '车型（轿车/SUV/卡车等）',
    `color`          varchar(50)    NULL COMMENT '颜色',
    `seats`          int            NULL COMMENT '座位数',
    `dailyPrice`     decimal(10, 2) NOT NULL COMMENT '日租金',
    `deposit`        decimal(10, 2) NOT NULL COMMENT '押金',
    `mileage`        int                     DEFAULT '0' COMMENT '行驶里程',
    `productionYear` int            NOT NULL COMMENT '生产年份',
    `status`         int            NOT NULL DEFAULT '0' COMMENT '状态：0-可用，1-已租出，2-维修中，3-报废',
    `imageUrl`       varchar(1024)           DEFAULT NULL COMMENT '车辆图片URL',
    `description`    text COMMENT '车辆描述',
    `createTime`     datetime       NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updateTime`     datetime       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `isDelete`       tinyint        NOT NULL DEFAULT '0' COMMENT '是否删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_vehicle_no` (`vehicle_no`),
    KEY `idx_status` (`status`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='车辆表';

