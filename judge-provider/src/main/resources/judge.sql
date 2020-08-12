/*
 Navicat Premium Data Transfer

 Source Server         : PDaaS
 Source Server Type    : MySQL
 Source Server Version : 80018
 Source Host           : 192.168.40.147:3306
 Source Schema         : judge

 Target Server Type    : MySQL
 Target Server Version : 80018
 File Encoding         : 65001

 Date: 11/08/2020 11:20:16
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for audit_task
-- ----------------------------
DROP TABLE IF EXISTS `audit_task`;
CREATE TABLE `audit_task`  (
  `id` int(16) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_code` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '租户code',
  `product_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '产品编号',
  `flow_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '产品编号',
  `apply_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '商户申请流水号',
  `user_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '用户id',
  `user_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '用户姓名',
  `mobile` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '手机号',
  `id_card` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '身份证号',
  `order_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '订单id',
  `ip` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'ip',
  `device_finger_print` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '设备指纹',
  `transaction_time` datetime(0) NOT NULL COMMENT '交易时间',
  `task_status` tinyint(4) NOT NULL DEFAULT 0 COMMENT '业务状态 0 请求已接收 1 请求转发成功 2 请求转发失败  3 变量接受成功  4, 变量计算失败 5 审核中 6 审核完成 7 审核失败 8 审核完成回调中 9 回调成功 10 回调失败',
  `audit_score` int(16) NOT NULL DEFAULT 0 COMMENT '审核评分',
  `audit_code` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '审核结果码  拒绝：REJECT 人工初审：MANUAL_REVIEW 人工终审：MANUAL_FINAL_REVIEW 通过:PASS',
  `callback_url` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '回调地址',
  `retry_count` tinyint(4) NOT NULL DEFAULT 0 COMMENT '重试jury次数',
  `callback_count` tinyint(4) NOT NULL DEFAULT 0 COMMENT '回调次数',
  `complete_time` datetime(0) NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '规则执行完成时间',
  `gmt_create` datetime(0) NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime(0) NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_tenantId_applyId`(`tenant_code`, `apply_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 138 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '风控申请表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for audit_task_param
-- ----------------------------
DROP TABLE IF EXISTS `audit_task_param`;
CREATE TABLE `audit_task_param`  (
  `id` int(16) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_code` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '租户id',
  `task_id` int(16) NOT NULL DEFAULT 0 COMMENT '业务流水号',
  `apply_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '商户申请流水号',
  `input_raw_param` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '入参',
  `output_raw_param` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '出参',
  `var_result` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '规则变量结果',
  `gmt_create` datetime(0) NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime(0) NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_tenantId_applyId`(`tenant_code`, `apply_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 71 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '风控入参出参表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for audit_task_triggered_rule
-- ----------------------------
DROP TABLE IF EXISTS `audit_task_triggered_rule`;
CREATE TABLE `audit_task_triggered_rule`  (
  `id` int(16) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_code` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '租户code',
  `task_id` int(16) NOT NULL DEFAULT 0 COMMENT '业务流水号',
  `apply_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '商户申请流水号',
  `flow_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '业务流code',
  `index` tinyint(4) NOT NULL DEFAULT 0 COMMENT '包执行顺序',
  `rule_package_code` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '规则包编号',
  `rule_package_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '规则包名',
  `rule_package_version` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '规则包版本号',
  `rule_code` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '规则编号',
  `rule_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '规则名',
  `rule_version` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '规则版本',
  `expression` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '条件表达式',
  `condition` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '规则条件值',
  `param` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '实际参数',
  `score` tinyint(4) NOT NULL DEFAULT 0 COMMENT '规则命中分值',
  `result` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '命中结果 0：拒绝  1：人工初审  2：人工终审  3：通过',
  `gmt_create` datetime(0) NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 154 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '规则命中表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for auth_authority
-- ----------------------------
DROP TABLE IF EXISTS `auth_authority`;
CREATE TABLE `auth_authority`  (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `display_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `first_menu` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `second_menu` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `gmt_created` datetime(0) NULL DEFAULT NULL,
  `gmt_modified` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 23 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of auth_authority
-- ----------------------------
INSERT INTO `auth_authority` VALUES (7, '查看', 'user_list', '系统管理', '账号列表', '2020-06-04 11:00:00', '2020-06-04 11:00:00');
INSERT INTO `auth_authority` VALUES (8, '查询', 'user_query', '系统管理', '账号列表', '2020-06-04 11:00:00', '2020-06-04 11:00:00');
INSERT INTO `auth_authority` VALUES (9, '增加', 'user_add', '系统管理', '账号列表', '2020-06-04 11:00:00', '2020-06-04 11:00:00');
INSERT INTO `auth_authority` VALUES (10, '修改', 'user_modify', '系统管理', '账号列表', '2020-06-04 11:00:00', '2020-06-04 11:00:00');
INSERT INTO `auth_authority` VALUES (11, '删除', 'user_delete', '系统管理', '账号列表', '2020-06-04 11:00:00', '2020-06-04 11:00:00');
INSERT INTO `auth_authority` VALUES (12, '重置密码', 'user_password_reset', '系统管理', '账号列表', '2020-06-04 11:00:00', '2020-06-04 11:00:00');
INSERT INTO `auth_authority` VALUES (13, '查看', 'role_list', '系统管理', '角色列表', '2020-06-04 11:00:00', '2020-06-04 11:00:00');
INSERT INTO `auth_authority` VALUES (14, '查询', 'role_query', '系统管理', '角色列表', '2020-06-04 11:00:00', '2020-06-04 11:00:00');
INSERT INTO `auth_authority` VALUES (15, '增加', 'role_add', '系统管理', '角色列表', '2020-06-04 11:00:00', '2020-06-04 11:00:00');
INSERT INTO `auth_authority` VALUES (16, '修改', 'role_modify', '系统管理', '角色列表', '2020-06-04 11:00:00', '2020-06-04 11:00:00');
INSERT INTO `auth_authority` VALUES (17, '删除', 'role_delete', '系统管理', '角色列表', '2020-06-04 11:00:00', '2020-06-04 11:00:00');
INSERT INTO `auth_authority` VALUES (18, '查看', 'department_list', '系统管理', '部门列表', '2020-06-04 11:00:00', '2020-06-04 11:00:00');
INSERT INTO `auth_authority` VALUES (19, '查询', 'department_query', '系统管理', '部门列表', '2020-06-04 11:00:00', '2020-06-04 11:00:00');
INSERT INTO `auth_authority` VALUES (20, '增加', 'department_add', '系统管理', '部门列表', '2020-06-04 11:00:00', '2020-06-04 11:00:00');
INSERT INTO `auth_authority` VALUES (21, '修改', 'department_modify', '系统管理', '部门列表', '2020-06-04 11:00:00', '2020-06-04 11:00:00');
INSERT INTO `auth_authority` VALUES (22, '删除', 'department_delete', '系统管理', '部门列表', '2020-06-04 11:00:00', '2020-06-04 11:00:00');

-- ----------------------------
-- Table structure for auth_department
-- ----------------------------
DROP TABLE IF EXISTS `auth_department`;
CREATE TABLE `auth_department`  (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `manager_id` int(11) NULL DEFAULT NULL COMMENT '部门负责人',
  `operator` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `gmt_created` datetime(0) NULL DEFAULT NULL,
  `gmt_modified` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for auth_role
-- ----------------------------
DROP TABLE IF EXISTS `auth_role`;
CREATE TABLE `auth_role`  (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `authorities` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `operator` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `gmt_created` datetime(0) NOT NULL,
  `gmt_modified` datetime(0) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_name`(`name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 23 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for auth_tenant
-- ----------------------------
DROP TABLE IF EXISTS `auth_tenant`;
CREATE TABLE `auth_tenant`  (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `tenant_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `gmt_created` datetime(0) NOT NULL,
  `gmt_modified` datetime(0) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for auth_user
-- ----------------------------
DROP TABLE IF EXISTS `auth_user`;
CREATE TABLE `auth_user`  (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `username` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `roles` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '',
  `expired` tinyint(4) NULL DEFAULT 0,
  `locked` tinyint(4) NULL DEFAULT 0,
  `credentials_expired` tinyint(4) NULL DEFAULT 0,
  `enabled` tinyint(4) NULL DEFAULT 1,
  `mobile` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `user_type` tinyint(4) NULL DEFAULT NULL COMMENT '用户类型 0 平台 1 商户',
  `tenants` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `department_id` int(11) NULL DEFAULT NULL COMMENT '部门id',
  `department` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '部门',
  `operator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `gmt_created` datetime(0) NOT NULL,
  `gmt_modified` datetime(0) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_username`(`username`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of auth_user
-- ----------------------------
INSERT INTO `auth_user` VALUES (1, 'admin', 'admin', '$2a$10$CfV.iL.BiTbmM/Oh4jYf5.bcGrlyJDeK0och4c8gwu2CmrjXSdJwW', 'admin', 0, 0, 0, 1, '123', 0, '[{\"tenantCode\":\"t1\",\"tenantName\":\"t1\"}]', NULL, '', 'admin', '2020-08-10 13:39:19', '2020-08-10 10:01:28');

-- ----------------------------
-- Table structure for config_flow
-- ----------------------------
DROP TABLE IF EXISTS `config_flow`;
CREATE TABLE `config_flow`  (
  `id` int(16) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `tenant_code` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '租户code',
  `product_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `product_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '产品编号',
  `flow_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '业务流编码',
  `flow_version` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `flow_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '业务流名称',
  `package_id` int(16) NOT NULL DEFAULT 0 COMMENT 'jar包id（config_package主键id）',
  `package_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `package_version` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `package_url` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'jar包url',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `status` tinyint(2) NOT NULL DEFAULT 0 COMMENT '状态 0未启用 1启用 2 禁用',
  `operator` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '操作人姓名',
  `gmt_created` datetime(0) NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime(0) NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_code`(`flow_code`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 18 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of config_flow
-- ----------------------------
INSERT INTO `config_flow` VALUES (1, 'ps', 'ps', 'ojk', '645e43ef87b3444fb1985dbbe66a5a79', 'ojk_new_customer', 'v1.0', '印尼OJk新用户', 14, '0715', 'v1.0', 'http://factoryfilestore.oss-cn-hangzhou.aliyuncs.com/2020-07-15/1594806444485.jar?Expires=1625910444&OSSAccessKeyId=LTAI4Ftbiuu38HBpvMeikex1&Signature=L1HyfP4ePnmY9nMZQEZylIwmDuE%3D', NULL, 1, 'admin', '2020-08-10 06:57:23', '2020-08-10 06:57:23');
INSERT INTO `config_flow` VALUES (2, 'ps', 'ps', 'ojk', '645e43ef87b3444fb1985dbbe66a5a79', 'ojk_old_customer', 'v1.0', '印尼OJk复借用户', 14, '0715', 'v1.0', 'http://factoryfilestore.oss-cn-hangzhou.aliyuncs.com/2020-07-15/1594806444485.jar?Expires=1625910444&OSSAccessKeyId=LTAI4Ftbiuu38HBpvMeikex1&Signature=L1HyfP4ePnmY9nMZQEZylIwmDuE%3D', NULL, 0, 'admin', '2020-08-10 06:57:23', '2020-08-10 06:00:15');
INSERT INTO `config_flow` VALUES (3, 'ps', 'ps', 'product_test', '9a2439454e1d414d8aa69a7bdb125bde', 'idn_new_customer', 'v1.0', '印尼测试新用户', 14, '0715', 'v1.0', 'http://factoryfilestore.oss-cn-hangzhou.aliyuncs.com/2020-07-15/1594806444485.jar?Expires=1625910444&OSSAccessKeyId=LTAI4Ftbiuu38HBpvMeikex1&Signature=L1HyfP4ePnmY9nMZQEZylIwmDuE%3D', NULL, 0, 'admin', '2020-08-10 06:57:23', '2020-08-10 08:57:28');
INSERT INTO `config_flow` VALUES (4, 'ps', 'ps', 'product_test', '9a2439454e1d414d8aa69a7bdb125bde', 'idn_old_customer', 'v1.0', '印尼测试复借用户', 14, '0715', 'v1.0', 'http://factoryfilestore.oss-cn-hangzhou.aliyuncs.com/2020-07-15/1594806444485.jar?Expires=1625910444&OSSAccessKeyId=LTAI4Ftbiuu38HBpvMeikex1&Signature=L1HyfP4ePnmY9nMZQEZylIwmDuE%3D', NULL, 1, 'admin', '2020-08-10 06:57:23', '2020-08-10 06:57:23');

-- ----------------------------
-- Table structure for config_flow_rule_package
-- ----------------------------
DROP TABLE IF EXISTS `config_flow_rule_package`;
CREATE TABLE `config_flow_rule_package`  (
  `id` int(16) NOT NULL,
  `flow_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '业务流编码',
  `rule_package_version_id` int(16) NOT NULL DEFAULT 0 COMMENT '规则版本id',
  `sort` tinyint(4) NOT NULL DEFAULT 0 COMMENT '排序',
  `gmt_created` datetime(0) NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime(0) NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of config_flow_rule_package
-- ----------------------------
INSERT INTO `config_flow_rule_package` VALUES (1, 'ojk_new_customer', 1, 1, '2020-08-10 09:01:20', '2020-08-10 09:01:23');
INSERT INTO `config_flow_rule_package` VALUES (2, 'ojk_old_customer', 2, 1, '2020-08-10 09:01:20', '2020-08-10 09:01:23');
INSERT INTO `config_flow_rule_package` VALUES (3, 'idn_new_customer', 1, 1, '2020-08-10 09:01:20', '2020-08-10 09:01:23');
INSERT INTO `config_flow_rule_package` VALUES (4, 'idn_old_customer', 2, 1, '2020-08-10 09:01:20', '2020-08-10 09:01:23');

-- ----------------------------
-- Table structure for config_package
-- ----------------------------
DROP TABLE IF EXISTS `config_package`;
CREATE TABLE `config_package`  (
  `id` int(16) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_code` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '租户id',
  `tenant_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `product_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `product_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `flow` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '业务流id',
  `package_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'jar包名称',
  `version` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `url` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'jar包地址',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `status` tinyint(4) NULL DEFAULT NULL,
  `operator` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `gmt_created` datetime(0) NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime(0) NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for config_product
-- ----------------------------
DROP TABLE IF EXISTS `config_product`;
CREATE TABLE `config_product`  (
  `id` int(16) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_code` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '租户id',
  `tenant_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `product_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '产品名称',
  `product_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '产品编号',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `status` tinyint(4) NOT NULL DEFAULT 0 COMMENT '产品状态：0禁用 1 启用',
  `operator` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '操作人姓名',
  `gmt_created` datetime(0) NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime(0) NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_code`(`product_code`) USING BTREE,
  UNIQUE INDEX `uk_name`(`product_name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 17 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of config_product
-- ----------------------------
INSERT INTO `config_product` VALUES (1, 'ps', 'ps', 'ojk', '645e43ef87b3444fb1985dbbe66a5a79', '', 1, 'admin', '2020-08-10 07:22:31', '2020-08-10 07:22:31');
INSERT INTO `config_product` VALUES (2, 'ps', 'ps', 'test', '9a2439454e1d414d8aa69a7bdb125bde', '', 1, 'admin', '2020-08-10 09:38:44', '2020-08-10 09:38:44');

-- ----------------------------
-- Table structure for config_rule
-- ----------------------------
DROP TABLE IF EXISTS `config_rule`;
CREATE TABLE `config_rule`  (
  `id` int(16) NOT NULL AUTO_INCREMENT,
  `tenant_code` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '商户编码',
  `rule_package_version_id` int(16) NOT NULL DEFAULT 0 COMMENT '规则版本id',
  `code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '规则编号',
  `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '规则名',
  `version` int(16) NOT NULL DEFAULT 0 COMMENT '规则版本',
  `salience` int(8) NOT NULL DEFAULT 100 COMMENT '执行顺序，数字越大越先执行',
  `condition_relation` tinyint(4) NOT NULL DEFAULT 1 COMMENT '条件关系 0: 或（||） 1:且(,)',
  `status` tinyint(4) NOT NULL DEFAULT 1 COMMENT '状态 0 关闭 1开启',
  `score` tinyint(4) NOT NULL DEFAULT 0 COMMENT '规则命中分值',
  `result` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '规则命中结果 0：拒绝  1：人工初审  2：人工终审  3：通过',
  `gmt_created` datetime(0) NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime(0) NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_rule`(`rule_package_version_id`, `code`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 61 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of config_rule
-- ----------------------------
INSERT INTO `config_rule` VALUES (1, 'ps', 1, 'AR0001', '人像对比相似度', 0, 100, 1, 1, 0, '0', '2020-07-20 15:14:09', '2020-07-20 15:14:12');
INSERT INTO `config_rule` VALUES (2, 'ps', 1, 'AR0002', '活体识别不为活体', 0, 100, 1, 1, 0, '0', '2020-07-20 15:14:09', '2020-07-20 15:14:12');
INSERT INTO `config_rule` VALUES (3, 'ps', 1, 'AR0003', '借款用户当前逾期', 0, 100, 1, 1, 0, '0', '2020-07-20 15:14:09', '2020-07-20 15:14:12');
INSERT INTO `config_rule` VALUES (4, 'ps', 1, 'AR0004', '用户年龄太小', 0, 100, 1, 1, 0, '0', '2020-07-20 15:25:07', '2020-07-20 15:25:10');
INSERT INTO `config_rule` VALUES (5, 'ps', 1, 'AR0005', '用户年龄太大', 0, 100, 1, 1, 0, '0', '2020-07-20 15:25:07', '2020-07-20 15:25:10');
INSERT INTO `config_rule` VALUES (6, 'ps', 1, 'AR0006', '用户年龄缺失', 0, 100, 1, 1, 0, '0', '2020-07-21 07:39:30', '2020-07-21 07:53:41');
INSERT INTO `config_rule` VALUES (7, 'ps', 1, 'AR0007', '用户职业不符合', 0, 100, 1, 1, 0, '0', '2020-07-21 07:39:30', '2020-07-21 07:53:41');
INSERT INTO `config_rule` VALUES (8, 'ps', 1, 'AR0008', '用户是否在准入城市内', 0, 100, 1, 1, 0, '0', '2020-07-21 07:39:30', '2020-07-21 07:53:41');
INSERT INTO `config_rule` VALUES (9, 'ps', 1, 'AR0009', '身份证ID下有未完结订单', 0, 100, 1, 1, 0, '0', '2020-07-21 07:39:30', '2020-07-21 07:53:41');
INSERT INTO `config_rule` VALUES (10, 'ps', 1, 'AR0010', '同盾实名验证失败', 0, 100, 1, 1, 0, '0', '2020-07-21 07:39:30', '2020-07-21 07:53:41');
INSERT INTO `config_rule` VALUES (11, 'ps', 1, 'BL0001', '用户银行卡匹配自有黑名单银行卡', 0, 100, 1, 1, 0, '0', '2020-07-21 07:39:30', '2020-07-21 07:53:41');
INSERT INTO `config_rule` VALUES (12, 'ps', 1, 'BL0002', '用户手机号码匹配自有黑名单', 0, 100, 1, 1, 0, '0', '2020-07-21 07:39:30', '2020-07-21 07:53:41');
INSERT INTO `config_rule` VALUES (13, 'ps', 1, 'BL0003', '用户设备指纹匹配自有黑名单', 0, 100, 1, 1, 0, '0', '2020-07-21 07:39:30', '2020-07-21 07:53:41');
INSERT INTO `config_rule` VALUES (14, 'ps', 1, 'BL0004', '用户身份证号匹配自有黑名单', 0, 100, 1, 1, 0, '0', '2020-07-21 07:39:30', '2020-07-21 07:53:41');
INSERT INTO `config_rule` VALUES (15, 'ps', 1, 'BL0005', '命中同盾数据黑名单', 0, 100, 1, 1, 0, '1', '2020-07-21 07:39:30', '2020-07-21 07:53:41');
INSERT INTO `config_rule` VALUES (16, 'ps', 1, 'MPL001', '命中同盾多头3天内去重多头', 0, 100, 1, 1, 0, '0', '2020-07-21 07:39:30', '2020-07-21 07:53:41');
INSERT INTO `config_rule` VALUES (17, 'ps', 1, 'MPL002', '命中同盾多头7天内去重多头', 0, 100, 1, 1, 0, '0', '2020-07-21 07:39:30', '2020-07-21 07:53:41');
INSERT INTO `config_rule` VALUES (18, 'ps', 1, 'MPL003', '命中同盾多头14天内去重多头', 0, 100, 1, 1, 0, '0', '2020-07-21 07:39:30', '2020-07-21 07:53:41');
INSERT INTO `config_rule` VALUES (19, 'ps', 1, 'MPL004', '命中同盾多头30天内去重多头', 0, 100, 1, 1, 0, '0', '2020-07-21 07:39:30', '2020-07-21 07:53:41');
INSERT INTO `config_rule` VALUES (20, 'ps', 1, 'MPL005', '命中同盾多头90天内去重多头', 0, 100, 1, 1, 0, '0', '2020-07-21 07:39:30', '2020-07-21 07:53:41');
INSERT INTO `config_rule` VALUES (21, 'ps', 1, 'MPL006', '命中同盾夜间去重多头30天内申请', 0, 100, 1, 1, 0, '0', '2020-07-21 07:39:30', '2020-07-21 07:53:41');
INSERT INTO `config_rule` VALUES (22, 'ps', 1, 'MPL007', '命中同盾夜间去重多头30天内申请次数/命中同盾多头30天内去重多头', 0, 100, 1, 1, 0, '1', '2020-07-21 07:39:30', '2020-07-21 07:53:41');
INSERT INTO `config_rule` VALUES (23, 'ps', 1, 'NB001', '设备使用时长', 0, 100, 1, 1, 0, '0', '2020-07-21 07:39:30', '2020-07-21 07:53:41');
INSERT INTO `config_rule` VALUES (24, 'ps', 1, 'NB002', '设备使用时长_数据缺失', 0, 100, 1, 1, 0, '0', '2020-07-21 07:39:30', '2020-07-21 07:53:41');
INSERT INTO `config_rule` VALUES (25, 'ps', 1, 'NB003', '同设备号下有未完结订单', 0, 100, 1, 1, 0, '1', '2020-07-21 07:39:30', '2020-07-21 07:53:41');
INSERT INTO `config_rule` VALUES (26, 'ps', 1, 'NB004', '新客用于注册和申请时获取的GPS定位距离', 0, 100, 1, 0, 0, '1', '2020-07-21 07:39:30', '2020-07-21 07:53:41');
INSERT INTO `config_rule` VALUES (27, 'ps', 1, 'NB005', '客户注册到申请时间', 0, 100, 1, 1, 0, '0', '2020-07-21 07:39:30', '2020-07-21 07:53:41');
INSERT INTO `config_rule` VALUES (28, 'ps', 1, 'GL001', '用户手机号码匹配灰名单', 0, 100, 1, 1, 0, '1', '2020-07-21 07:39:30', '2020-07-21 07:53:41');
INSERT INTO `config_rule` VALUES (29, 'ps', 1, 'GL002', '用户设备指纹匹配灰名单', 0, 100, 1, 1, 0, '1', '2020-07-21 07:39:30', '2020-07-21 07:53:41');
INSERT INTO `config_rule` VALUES (30, 'ps', 1, 'GL003', '用户身份证号匹配灰名单', 0, 100, 1, 1, 0, '1', '2020-07-21 07:39:30', '2020-07-21 07:53:41');
INSERT INTO `config_rule` VALUES (31, 'ps', 1, 'GL004', '用户银行卡匹配灰名单', 0, 100, 1, 1, 0, '1', '2020-07-21 07:39:30', '2020-07-21 07:53:41');
INSERT INTO `config_rule` VALUES (32, 'ps', 1, 'FG001', '用户手机号码关联疑是团伙', 0, 100, 1, 1, 0, '1', '2020-07-21 07:39:30', '2020-07-21 07:53:41');
INSERT INTO `config_rule` VALUES (33, 'ps', 1, 'FG002', '用户IP地址关联疑是团伙', 0, 100, 1, 1, 0, '1', '2020-07-21 07:39:30', '2020-07-21 07:53:41');
INSERT INTO `config_rule` VALUES (34, 'ps', 1, 'FG003', '用户GPS定位信息关联疑是团伙', 0, 100, 1, 1, 0, '1', '2020-07-21 07:39:30', '2020-07-21 07:53:41');
INSERT INTO `config_rule` VALUES (35, 'ps', 1, 'FG004', '用户设备号关联疑是团伙', 0, 100, 1, 1, 0, '1', '2020-07-21 07:39:30', '2020-07-21 07:53:41');
INSERT INTO `config_rule` VALUES (36, 'ps', 1, 'RR001', '身份证ID下有未完结订单', 0, 100, 1, 1, 0, '0', '2020-07-21 07:39:30', '2020-07-21 07:53:41');
INSERT INTO `config_rule` VALUES (37, 'ps', 1, 'RR002', '同设备号下有未完结订单', 0, 100, 1, 1, 0, '1', '2020-07-21 07:39:30', '2020-07-21 07:53:41');
INSERT INTO `config_rule` VALUES (38, 'ps', 1, 'RR003', '银行卡对应多个Uid', 0, 100, 1, 1, 0, '0', '2020-07-21 07:39:30', '2020-07-21 07:53:41');
INSERT INTO `config_rule` VALUES (39, 'ps', 1, 'RR004', '紧急联系人号码同时出现在其他用户的紧急联系人中', 0, 100, 1, 1, 0, '0', '2020-07-21 07:39:30', '2020-07-21 07:53:41');
INSERT INTO `config_rule` VALUES (40, 'ps', 2, 'CBR001', '命中同盾多头3天内去重多头较上次申请新增', 0, 100, 1, 1, 0, '1', '2020-07-21 07:39:30', '2020-07-21 07:53:41');
INSERT INTO `config_rule` VALUES (41, 'ps', 2, 'CBR002', '命中同盾多头7天内去重多头较上次申请', 0, 100, 1, 1, 0, '1', '2020-07-21 07:39:30', '2020-07-21 07:53:41');
INSERT INTO `config_rule` VALUES (42, 'ps', 2, 'CBR003', '命中同盾多头14天内去重多头较上次申请', 0, 100, 1, 1, 0, '1', '2020-07-21 07:39:30', '2020-07-21 07:53:41');
INSERT INTO `config_rule` VALUES (43, 'ps', 2, 'CBR004', '命中同盾多头3天内去重多头较上次申请新增', 0, 100, 1, 1, 0, '0', '2020-07-21 07:39:30', '2020-07-21 07:53:41');
INSERT INTO `config_rule` VALUES (44, 'ps', 2, 'CBR005', '命中同盾多头7天内去重多头较上次申请', 0, 100, 1, 1, 0, '0', '2020-07-21 07:39:30', '2020-07-21 07:53:41');
INSERT INTO `config_rule` VALUES (45, 'ps', 2, 'CBR006', '近两次申请所使用的设备是否有更换', 0, 100, 1, 1, 0, '1', '2020-07-21 07:39:30', '2020-07-21 07:53:41');
INSERT INTO `config_rule` VALUES (46, 'ps', 2, 'CBR007', '用户设备指纹关联身份证ID', 0, 100, 1, 1, 0, '1', '2020-07-21 07:39:30', '2020-07-21 07:53:41');
INSERT INTO `config_rule` VALUES (47, 'ps', 2, 'CBR008', '两次申请时GPS定位距离差距', 0, 100, 1, 1, 0, '1', '2020-07-21 07:39:30', '2020-07-21 07:53:41');
INSERT INTO `config_rule` VALUES (48, 'ps', 2, 'CBR009', 'GPS地址获取为空', 0, 100, 1, 1, 0, '1', '2020-07-21 07:39:30', '2020-07-21 07:53:41');
INSERT INTO `config_rule` VALUES (49, 'ps', 2, 'HOR001', '用户身份证是否有关联订单未结清状态', 0, 100, 1, 1, 0, '0', '2020-07-21 07:39:30', '2020-07-21 07:53:41');
INSERT INTO `config_rule` VALUES (50, 'ps', 2, 'HOR002', '用户设备号是否有关联订单未结清状态', 0, 100, 1, 1, 0, '1', '2020-07-21 07:39:30', '2020-07-21 07:53:41');
INSERT INTO `config_rule` VALUES (51, 'ps', 2, 'HOR003', '用户手机号是否有关联订单未结清状态', 0, 100, 1, 1, 0, '0', '2020-07-21 07:39:30', '2020-07-21 07:53:41');
INSERT INTO `config_rule` VALUES (52, 'ps', 2, 'HOR005', '复借人最近一笔还款逾期', 0, 100, 1, 1, 0, '1', '2020-07-21 07:39:30', '2020-07-21 07:53:41');
INSERT INTO `config_rule` VALUES (53, 'ps', 2, 'HOR006', '复借人最近一笔借款未全额还款', 0, 100, 1, 1, 0, '0', '2020-07-21 07:39:30', '2020-07-21 07:53:41');
INSERT INTO `config_rule` VALUES (54, 'ps', 2, 'GL001', '用户手机号码匹配灰名单', 0, 100, 1, 1, 0, '1', '2020-07-21 07:39:30', '2020-07-21 07:53:41');
INSERT INTO `config_rule` VALUES (55, 'ps', 2, 'GL002', '用户设备指纹匹配灰名单', 0, 100, 1, 1, 0, '1', '2020-07-21 07:39:30', '2020-07-21 07:53:41');
INSERT INTO `config_rule` VALUES (56, 'ps', 2, 'GL003', '用户身份证号匹配灰名单', 0, 100, 1, 1, 0, '1', '2020-07-21 07:39:30', '2020-07-21 07:53:41');
INSERT INTO `config_rule` VALUES (57, 'ps', 2, 'HBR001', '订单总逾期天数', 0, 100, 1, 1, 0, '0', '2020-07-21 07:39:30', '2020-07-21 07:53:41');
INSERT INTO `config_rule` VALUES (58, 'ps', 2, 'HBR002', '订单总逾期天数', 0, 100, 1, 1, 0, '1', '2020-07-21 07:39:30', '2020-07-21 07:53:41');
INSERT INTO `config_rule` VALUES (59, 'ps', 2, 'HBR003', '单笔订单最大逾期天数', 0, 100, 1, 1, 0, '1', '2020-07-21 07:39:30', '2020-07-21 07:53:41');
INSERT INTO `config_rule` VALUES (60, 'ps', 2, 'HBR004', '单笔订单最大逾期天数', 0, 100, 1, 1, 0, '0', '2020-07-21 07:39:30', '2020-07-21 07:53:41');

-- ----------------------------
-- Table structure for config_rule_condition
-- ----------------------------
DROP TABLE IF EXISTS `config_rule_condition`;
CREATE TABLE `config_rule_condition`  (
  `id` int(16) NOT NULL AUTO_INCREMENT,
  `rule_id` int(16) NOT NULL DEFAULT 0 COMMENT '规则编号',
  `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '条件描述名称',
  `operator` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '比较符',
  `operand` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '条件值',
  `function` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '函数',
  `variable_code` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '变量名',
  `variable_type` tinyint(4) NOT NULL DEFAULT 0 COMMENT '变量类型 0:String 1:int 2:double',
  `status` tinyint(4) NOT NULL DEFAULT 1 COMMENT '状态 0 关闭 1开启',
  `gmt_created` datetime(0) NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime(0) NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 65 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '单变量条件' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of config_rule_condition
-- ----------------------------
INSERT INTO `config_rule_condition` VALUES (1, 1, '人像对比相似度小于0.7', '<', '0.7', '', 'order_personalInfoIdn_portraitContrastSimilarity', 2, 1, '2020-07-20 15:15:00', '2020-07-20 15:15:04');
INSERT INTO `config_rule_condition` VALUES (2, 2, '命中活体识别不为活体', '==', '\'1\'', '', 'order_personalInfoIdn_livingIdentification', 0, 1, '2020-07-20 15:15:00', '2020-07-20 15:15:04');
INSERT INTO `config_rule_condition` VALUES (3, 3, '命中借款用户当前逾期', '==', '\'1\'', '', 'platform_platformBusinessInfoIdn_currentlyOverdue', 0, 1, '2020-07-20 15:15:00', '2020-07-20 15:15:04');
INSERT INTO `config_rule_condition` VALUES (4, 4, '用户年龄小于18', '<', '18', '', 'order_personalInfoIdn_age', 1, 1, '2020-07-20 15:15:00', '2020-07-20 15:15:04');
INSERT INTO `config_rule_condition` VALUES (5, 5, '用户年龄大于50', '>=', '50', '', 'order_personalInfoIdn_age', 1, 1, '2020-07-20 15:15:00', '2020-07-20 15:15:04');
INSERT INTO `config_rule_condition` VALUES (6, 6, '用户年龄缺失', '==', '-999999', '', 'order_personalInfoIdn_age', 1, 1, '2020-07-21 09:00:27', '2020-07-21 09:00:27');
INSERT INTO `config_rule_condition` VALUES (7, 7, '用户职业不符合', 'in', '( \'police officer\', \'soldier\', \'lawyer\', \'attorney\', \'journalist\', \'reporter\' )', '', 'order_personalInfoIdn_profession', 0, 1, '2020-07-21 09:00:27', '2020-07-21 09:00:27');
INSERT INTO `config_rule_condition` VALUES (8, 8, '用户是否在准入城市内', '==', '\'1\'', '', 'order_deviceInfoIdn_inOpenCity', 0, 1, '2020-07-21 09:00:27', '2020-07-21 09:00:27');
INSERT INTO `config_rule_condition` VALUES (9, 9, '命中身份证ID下有未完结订单', '==', '\'1\'', '', 'platform_platformBusinessInfoIdn_idCardRelatedUnfinishedOrder', 0, 1, '2020-07-21 09:00:27', '2020-07-21 09:00:27');
INSERT INTO `config_rule_condition` VALUES (10, 10, '命中同盾实名验证失败', '==', '\'1\'', '', 'order_personalInfoIdn_tdRealNameVerificationResults', 0, 1, '2020-07-21 09:00:27', '2020-07-21 09:00:27');
INSERT INTO `config_rule_condition` VALUES (11, 11, '命中用户银行卡匹配自有黑名单银行卡', '==', '\'1\'', '', 'platform_blackListInfoIdn_bankCardHitsBlackList', 0, 1, '2020-07-21 09:00:27', '2020-07-21 09:00:27');
INSERT INTO `config_rule_condition` VALUES (12, 12, '命中用户手机号码匹配自有黑名单', '==', '\'1\'', '', 'platform_blackListInfoIdn_mobileHitsBlackList', 0, 1, '2020-07-21 09:00:27', '2020-07-21 09:00:27');
INSERT INTO `config_rule_condition` VALUES (13, 13, '命中用户设备指纹匹配自有黑名单', '==', '\'1\'', '', 'platform_blackListInfoIdn_deviceFingerPrintHitsBlackList', 0, 1, '2020-07-21 09:00:27', '2020-07-21 09:00:27');
INSERT INTO `config_rule_condition` VALUES (14, 14, '命中用户身份证号匹配自有黑名单', '==', '\'1\'', '', 'platform_blackListInfoIdn_idCardHitsBlackList', 0, 1, '2020-07-21 09:00:27', '2020-07-21 09:00:27');
INSERT INTO `config_rule_condition` VALUES (15, 15, '命中同盾数据黑名单', '==', '\'1\'', '', 'platform_blackListInfoIdn_tdHitsBlackList', 0, 1, '2020-07-21 09:00:27', '2020-07-21 09:00:27');
INSERT INTO `config_rule_condition` VALUES (16, 16, '命中同盾多头3天内去重多头', '>=', '2', '', 'platform_multiplatformLoanInfoIdn_tdMultiplatformApplyCount3D', 1, 1, '2020-07-21 09:00:27', '2020-07-21 09:00:27');
INSERT INTO `config_rule_condition` VALUES (17, 17, '命中同盾多头7天内去重多头', '>=', '4', '', 'platform_multiplatformLoanInfoIdn_tdMultiplatformApplyCount7D', 1, 1, '2020-07-21 09:00:27', '2020-07-21 09:00:27');
INSERT INTO `config_rule_condition` VALUES (18, 18, '命中同盾多头14天内去重多头', '>=', '7', '', 'platform_multiplatformLoanInfoIdn_tdMultiplatformApplyCount14D', 1, 1, '2020-07-21 09:00:27', '2020-07-21 09:00:27');
INSERT INTO `config_rule_condition` VALUES (19, 19, '命中同盾多头30天内去重多头', '>=', '12', '', 'platform_multiplatformLoanInfoIdn_tdMultiplatformApplyCount30D', 1, 1, '2020-07-21 09:00:27', '2020-07-21 09:00:27');
INSERT INTO `config_rule_condition` VALUES (20, 20, '命中同盾多头90天内去重多头', '>=', '30', '', 'platform_multiplatformLoanInfoIdn_tdMultiplatformApplyCount90D', 1, 1, '2020-07-21 09:00:27', '2020-07-21 09:00:27');
INSERT INTO `config_rule_condition` VALUES (21, 21, '命中同盾夜间去重多头30天内申请', '>=', '8', '', 'platform_multiplatformLoanInfoIdn_tdMultiplatformApplyNightApplyCount30D', 1, 1, '2020-07-21 09:00:27', '2020-07-21 09:00:27');
INSERT INTO `config_rule_condition` VALUES (22, 22, '命中同盾夜间去重多头30天内申请次数/命中同盾多头30天内去重多头', '>', '0.8', '', 'platform_multiplatformLoanInfoIdn_tdMultiplatformApplyCountRatioNightToDay30D', 2, 1, '2020-07-21 09:00:27', '2020-07-21 09:00:27');
INSERT INTO `config_rule_condition` VALUES (23, 23, '设备使用时长小于3小时', '<=', '3', '', 'order_deviceInfoIdn_deviceUsedTime', 2, 1, '2020-07-21 09:00:27', '2020-07-21 09:00:27');
INSERT INTO `config_rule_condition` VALUES (24, 24, '设备使用时长缺失', '==', '-999999', '', 'order_deviceInfoIdn_deviceUsedTime', 1, 1, '2020-07-21 09:00:27', '2020-07-21 09:00:27');
INSERT INTO `config_rule_condition` VALUES (25, 25, '命中同设备号下有未完结订单', '==', '\'1\'', '', 'platform_platformBusinessInfoIdn_deviceFingerPrintRelatedUnfinishedOrder', 0, 1, '2020-07-21 09:00:27', '2020-07-21 09:00:27');
INSERT INTO `config_rule_condition` VALUES (26, 26, '新客用于注册和申请时获取的GPS定位距离大于等于80KM', '>=', '80', '', 'order_deviceInfoIdn_distanceRegisteringAndApplying', 2, 1, '2020-07-21 09:00:27', '2020-07-21 09:00:27');
INSERT INTO `config_rule_condition` VALUES (27, 27, '客户注册到申请时间小于等于120s', '<=', '120', '', 'order_orderBusinessInfoIdn_registeredToAppliedTime', 1, 1, '2020-07-21 09:00:27', '2020-07-21 09:00:27');
INSERT INTO `config_rule_condition` VALUES (28, 28, '用户手机号码匹配灰名单', '==', '\'1\'', '', 'platform_greyListInfoIdn_mobileHitsGreyList', 0, 1, '2020-07-21 09:00:27', '2020-07-21 09:00:27');
INSERT INTO `config_rule_condition` VALUES (29, 29, '用户设备指纹匹配灰名单', '==', '\'1\'', '', 'platform_greyListInfoIdn_deviceFingerPrintHitsGreyList', 0, 1, '2020-07-21 09:00:27', '2020-07-21 09:00:27');
INSERT INTO `config_rule_condition` VALUES (30, 30, '用户身份证号匹配灰名单', '==', '\'1\'', '', 'platform_greyListInfoIdn_idCardHitsGreyList', 0, 1, '2020-07-21 09:00:27', '2020-07-21 09:00:27');
INSERT INTO `config_rule_condition` VALUES (31, 31, '用户银行卡匹配灰名单', '==', '\'1\'', '', 'platform_greyListInfoIdn_bankCardHitsGreyList', 0, 1, '2020-07-21 09:00:27', '2020-07-21 09:00:27');
INSERT INTO `config_rule_condition` VALUES (32, 32, '用户手机号码关联疑是团伙', '==', '\'1\'', '', 'platform_loanGangIdn_mobileHitsLoanGang', 0, 1, '2020-07-21 09:00:27', '2020-07-21 09:00:27');
INSERT INTO `config_rule_condition` VALUES (33, 33, '用户IP地址关联疑是团伙', '==', '\'1\'', '', 'platform_loanGangIdn_ipHitsLoanGang', 0, 1, '2020-07-21 09:00:27', '2020-07-21 09:00:27');
INSERT INTO `config_rule_condition` VALUES (34, 34, '用户GPS定位信息关联疑是团伙', '==', '\'1\'', '', 'platform_loanGangIdn_gpsHitsLoanGang', 0, 1, '2020-07-21 09:00:27', '2020-07-21 09:00:27');
INSERT INTO `config_rule_condition` VALUES (35, 35, '用户设备号关联疑是团伙', '==', '\'1\'', '', 'platform_loanGangIdn_deviceFingerPrintHitsLoanGang', 0, 1, '2020-07-21 09:00:27', '2020-07-21 09:00:27');
INSERT INTO `config_rule_condition` VALUES (36, 36, '身份证ID下有未完结订单', '==', '\'1\'', '', 'platform_platformBusinessInfoIdn_idCardRelatedUnfinishedOrder', 0, 1, '2020-07-21 09:00:27', '2020-07-21 09:00:27');
INSERT INTO `config_rule_condition` VALUES (37, 37, '同设备号下有未完结订单', '==', '\'1\'', '', 'platform_platformBusinessInfoIdn_deviceFingerPrintRelatedUnfinishedOrder', 0, 1, '2020-07-21 09:00:27', '2020-07-21 09:00:27');
INSERT INTO `config_rule_condition` VALUES (38, 38, '银行卡对应多个Uid', '==', '\'1\'', '', 'platform_platformBusinessInfoIdn_bankCardRelatedMultipleUserIds', 0, 1, '2020-07-21 09:00:27', '2020-07-21 09:00:27');
INSERT INTO `config_rule_condition` VALUES (39, 39, '紧急联系人号码同时出现在其他用户的紧急联系人中大于2次', '>', '2', '', 'order_orderBusinessInfoIdn_hasSameEmergencyContactWithOthers', 1, 1, '2020-07-21 09:00:27', '2020-07-21 09:00:27');
INSERT INTO `config_rule_condition` VALUES (40, 40, '命中同盾多头3天内去重多头较上次申请新增', '>=', '2', '', 'platform_multiplatformLoanInfoIdn_tdMultiplatformApplyComparedWithLastCount3D', 1, 1, '2020-07-21 09:00:27', '2020-07-21 09:00:27');
INSERT INTO `config_rule_condition` VALUES (41, 41, '命中同盾多头7天内去重多头较上次申请', '>=', '3', '', 'platform_multiplatformLoanInfoIdn_tdMultiplatformApplyComparedWithLastCount7D', 1, 1, '2020-07-21 09:00:27', '2020-07-21 09:00:27');
INSERT INTO `config_rule_condition` VALUES (42, 42, '命中同盾多头14天内去重多头较上次申请', '>=', '5', '', 'platform_multiplatformLoanInfoIdn_tdMultiplatformApplyComparedWithLastCount14D', 1, 1, '2020-07-21 09:00:27', '2020-07-21 09:00:27');
INSERT INTO `config_rule_condition` VALUES (43, 43, '命中同盾多头3天内去重多头较上次申请新增', '>=', '5', '', 'platform_multiplatformLoanInfoIdn_tdMultiplatformApplyComparedWithLastCount3D', 1, 1, '2020-07-21 09:00:27', '2020-07-21 09:00:27');
INSERT INTO `config_rule_condition` VALUES (44, 44, '命中同盾多头7天内去重多头较上次申请', '>=', '6', '', 'platform_multiplatformLoanInfoIdn_tdMultiplatformApplyComparedWithLastCount7D', 1, 1, '2020-07-21 09:00:27', '2020-07-21 09:00:27');
INSERT INTO `config_rule_condition` VALUES (45, 45, '近两次申请所使用的设备是否有更换', '==', '\'1\'', '', 'order_deviceInfoIdn_lastTwoApplyDeviceFingerPrintIsIdentical', 0, 1, '2020-07-21 09:00:27', '2020-07-21 09:00:27');
INSERT INTO `config_rule_condition` VALUES (46, 46, '用户设备指纹关联身份证ID', '>', '2', '', 'order_deviceInfoIdn_deviceFingerPrintRelatedUserIdCount', 1, 1, '2020-07-21 09:00:27', '2020-07-21 09:00:27');
INSERT INTO `config_rule_condition` VALUES (47, 47, '两次申请时GPS定位距离差距', '>=', '80', '', 'order_deviceInfoIdn_distanceCurrentApplyFromLastApply', 2, 1, '2020-07-21 09:00:27', '2020-07-21 09:00:27');
INSERT INTO `config_rule_condition` VALUES (48, 48, 'GPS地址获取为空', '==', '\'1\'', '', 'order_deviceInfoIdn_gpsIsIncomplete', 0, 1, '2020-07-21 09:00:27', '2020-07-21 09:00:27');
INSERT INTO `config_rule_condition` VALUES (49, 49, '用户身份证是否有关联订单未结清状态', '==', '\'1\'', '', 'platform_platformBusinessInfoIdn_idCardRelatedUnfinishedOrder', 0, 1, '2020-07-21 09:00:27', '2020-07-21 09:00:27');
INSERT INTO `config_rule_condition` VALUES (50, 50, '用户设备号是否有关联订单未结清状态', '==', '\'1\'', '', 'platform_platformBusinessInfoIdn_deviceFingerPrintRelatedUnfinishedOrder', 0, 1, '2020-07-21 09:00:27', '2020-07-21 09:00:27');
INSERT INTO `config_rule_condition` VALUES (51, 51, '用户手机号是否有关联订单未结清状态', '==', '\'1\'', '', 'platform_platformBusinessInfoIdn_mobileRelatedUnfinishedOrder', 0, 1, '2020-07-21 09:00:27', '2020-07-21 09:00:27');
INSERT INTO `config_rule_condition` VALUES (52, 52, '复借人最近一笔还款逾期', '>', '3', '', 'platform_platformBusinessInfoIdn_lastOverdueDays', 1, 1, '2020-07-21 09:00:27', '2020-07-21 09:00:27');
INSERT INTO `config_rule_condition` VALUES (53, 53, '复借人最近一笔借款未全额还款', '==', '\'1\'', '', 'platform_platformBusinessInfoIdn_lastLoanIsFullRepayment', 0, 1, '2020-07-21 09:00:27', '2020-07-21 09:00:27');
INSERT INTO `config_rule_condition` VALUES (54, 54, '用户手机号码匹配灰名单', '==', '\'1\'', '', 'platform_greyListInfoIdn_mobileHitsGreyList', 0, 1, '2020-07-21 09:00:27', '2020-07-21 09:00:27');
INSERT INTO `config_rule_condition` VALUES (55, 55, '用户设备指纹匹配灰名单', '==', '\'1\'', '', 'platform_greyListInfoIdn_deviceFingerPrintHitsGreyList', 0, 1, '2020-07-21 09:00:27', '2020-07-21 09:00:27');
INSERT INTO `config_rule_condition` VALUES (56, 56, '用户身份证号匹配灰名单', '==', '\'1\'', '', 'platform_greyListInfoIdn_idCardHitsGreyList', 0, 1, '2020-07-21 09:00:27', '2020-07-21 09:00:27');
INSERT INTO `config_rule_condition` VALUES (57, 57, '订单总逾期天数', '>', '30', '', 'platform_platformBusinessInfoIdn_orderOverdueDays', 1, 1, '2020-07-21 09:00:27', '2020-07-21 09:00:27');
INSERT INTO `config_rule_condition` VALUES (58, 58, '订单总逾期天数', '>', '15', '', 'platform_platformBusinessInfoIdn_orderOverdueDays', 1, 1, '2020-07-21 09:00:27', '2020-07-21 09:00:27');
INSERT INTO `config_rule_condition` VALUES (59, 59, '单笔订单最大逾期天数', '>', '3', '', 'platform_platformBusinessInfoIdn_singleOrderOverdueMaxDays', 1, 1, '2020-07-21 09:00:27', '2020-07-21 09:00:27');
INSERT INTO `config_rule_condition` VALUES (60, 60, '单笔订单最大逾期天数', '>', '15', '', 'platform_platformBusinessInfoIdn_singleOrderOverdueMaxDays', 1, 1, '2020-07-21 09:00:27', '2020-07-21 09:00:27');
INSERT INTO `config_rule_condition` VALUES (61, 1, '人像对比相似度大于等于0', '>=', '0', '', 'order_personalInfoIdn_portraitContrastSimilarity', 2, 1, '2020-07-20 15:15:00', '2020-07-20 15:15:04');
INSERT INTO `config_rule_condition` VALUES (62, 4, '用户年龄大于等于18', '>=', '0', '', 'order_personalInfoIdn_age', 1, 1, '2020-07-20 15:15:00', '2020-07-20 15:15:04');
INSERT INTO `config_rule_condition` VALUES (63, 23, '设备使用时长大于等于3小时', '>=', '0', '', 'order_deviceInfoIdn_deviceUsedTime', 1, 1, '2020-07-21 09:00:27', '2020-07-21 09:00:27');
INSERT INTO `config_rule_condition` VALUES (64, 27, '客户注册到申请时间大于等于120s', '>=', '0', '', 'order_orderBusinessInfoIdn_registeredToAppliedTime', 1, 1, '2020-07-21 09:00:27', '2020-07-21 09:00:27');

-- ----------------------------
-- Table structure for config_rule_package
-- ----------------------------
DROP TABLE IF EXISTS `config_rule_package`;
CREATE TABLE `config_rule_package`  (
  `id` int(16) NOT NULL AUTO_INCREMENT,
  `tenant_code` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '商户编码',
  `code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '包编号',
  `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '包名称',
  `operator_id` int(11) NOT NULL DEFAULT 0 COMMENT '操作人id',
  `gmt_created` datetime(0) NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime(0) NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_name`(`name`) USING BTREE,
  UNIQUE INDEX `uk_code`(`code`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 18 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of config_rule_package
-- ----------------------------
INSERT INTO `config_rule_package` VALUES (1, 'ps', 'IDNAR', '印尼新用户', 0, '2020-08-10 15:11:56', '2020-08-10 15:12:00');
INSERT INTO `config_rule_package` VALUES (2, 'ps', 'IDNCBR', '印尼复借用户', 0, '2020-08-10 15:11:56', '2020-08-10 15:12:00');

-- ----------------------------
-- Table structure for config_rule_package_version
-- ----------------------------
DROP TABLE IF EXISTS `config_rule_package_version`;
CREATE TABLE `config_rule_package_version`  (
  `id` int(16) NOT NULL AUTO_INCREMENT,
  `rule_package_id` int(16) NOT NULL DEFAULT 0 COMMENT '规则包id',
  `version` int(16) NOT NULL DEFAULT 0 COMMENT '包版本',
  `status` tinyint(4) NOT NULL DEFAULT 0 COMMENT '0未启用，1启用',
  `gmt_created` datetime(0) NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime(0) NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_rule_package`(`rule_package_id`, `version`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of config_rule_package_version
-- ----------------------------
INSERT INTO `config_rule_package_version` VALUES (1, 1, 1, 1, '2020-08-10 17:54:18', '2020-08-10 17:54:21');
INSERT INTO `config_rule_package_version` VALUES (2, 2, 1, 1, '2020-08-10 17:54:18', '2020-08-10 17:54:21');

-- ----------------------------
-- Table structure for config_rule_package_version_sequence
-- ----------------------------
DROP TABLE IF EXISTS `config_rule_package_version_sequence`;
CREATE TABLE `config_rule_package_version_sequence`  (
  `id` int(16) NOT NULL AUTO_INCREMENT,
  `package_id` int(16) NOT NULL DEFAULT 0 COMMENT '包名称',
  `current_version` int(16) NOT NULL DEFAULT 0 COMMENT '包版本',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_rule_package`(`current_version`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for config_rule_variable
-- ----------------------------
DROP TABLE IF EXISTS `config_rule_variable`;
CREATE TABLE `config_rule_variable`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `level` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `group` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `type` tinyint(4) NOT NULL COMMENT '0 String 1 Number',
  `gmt_created` datetime(0) NOT NULL,
  `gmt_modified` datetime(0) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
