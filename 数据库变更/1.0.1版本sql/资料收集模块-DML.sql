/*
 Navicat Premium Data Transfer

 Source Server         : 本地
 Source Server Type    : MySQL
 Source Server Version : 50723
 Source Host           : localhost:3306
 Source Schema         : claim

 Target Server Type    : MySQL
 Target Server Version : 50723
 File Encoding         : 65001

 Date: 15/09/2021 16:30:11
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for b_data_collection
-- ----------------------------
DROP TABLE IF EXISTS `b_data_collection`;
CREATE TABLE `b_data_collection`  (
  `Id` int(12) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `ReportNo` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '报案号',
  `CertifyCode` varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '单证类型代码',
  `CertifyName` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '单证类型名称',
  `CollectTime` datetime(0) NULL DEFAULT NULL COMMENT '收集时间',
  `UploadTime` datetime(0) NULL DEFAULT NULL COMMENT '上传时间',
  `UploaderCode` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '上传者代码',
  `UploaderName` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '上传者名称',
  `CreateTime` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `UpdateTime` datetime(0) NOT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`Id`) USING BTREE,
  INDEX `idx_bdatacollection_reportno`(`ReportNo`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '资料收集表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for b_file_list
-- ----------------------------
DROP TABLE IF EXISTS `b_file_list`;
CREATE TABLE `b_file_list`  (
  `Id` int(12) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `CollectionId` int(12) NULL DEFAULT NULL COMMENT '资料收集表Id',
  `Name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文件名',
  `Url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文件链接',
  `CreateTime` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `UpdateTime` datetime(0) NOT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`Id`) USING BTREE,
  INDEX `idx_bfilelist_collectionid`(`CollectionId`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '资料收集文件表' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
