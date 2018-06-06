/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50721
 Source Host           : localhost
 Source Database       : supplierSys

 Target Server Type    : MySQL
 Target Server Version : 50721
 File Encoding         : utf-8

 Date: 06/06/2018 14:56:39 PM
*/

SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `blog`
-- ----------------------------
DROP TABLE IF EXISTS `blog`;
CREATE TABLE `blog` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(200) NOT NULL,
  `content` mediumtext NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `orderBill`
-- ----------------------------
DROP TABLE IF EXISTS `orderBill`;
CREATE TABLE `orderBill` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `billNo` varchar(50) DEFAULT NULL COMMENT '订单编号',
  `supplierId` varchar(50) DEFAULT NULL COMMENT '供应商编码',
  `supplierName` varchar(100) DEFAULT NULL COMMENT '供应商名称',
  `orderDate` datetime DEFAULT NULL COMMENT '下单时间',
  `buyerId` varchar(50) DEFAULT NULL COMMENT '采购员ID',
  `buyerName` varchar(255) DEFAULT NULL COMMENT '采购员名称',
  `merchandiserId` varchar(255) DEFAULT NULL COMMENT '跟单员ID',
  `merchandiserName` varchar(255) DEFAULT NULL COMMENT '跟单员名称',
  `status` varchar(255) DEFAULT NULL COMMENT '状态',
  `createDate` datetime DEFAULT NULL COMMENT '数据创建时间',
  `lastModified` datetime DEFAULT NULL COMMENT '最后更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2051 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `orderBillProcedures`
-- ----------------------------
DROP TABLE IF EXISTS `orderBillProcedures`;
CREATE TABLE `orderBillProcedures` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `fId` int(11) DEFAULT NULL COMMENT '父ID',
  `status` varchar(255) DEFAULT NULL COMMENT '状态',
  `imgPath` varchar(255) DEFAULT NULL COMMENT '图片路径',
  `uploadTime` datetime DEFAULT NULL COMMENT '创建时间',
  `deadline` datetime DEFAULT NULL COMMENT '截止时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10258 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `user`
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `userName` varchar(50) DEFAULT NULL COMMENT '用户名',
  `name` varchar(255) DEFAULT NULL COMMENT '名称',
  `pwd` varchar(255) DEFAULT NULL COMMENT '密码',
  `crearteDate` datetime DEFAULT NULL COMMENT '创建时间',
  `role` int(11) DEFAULT NULL,
  `tel` varchar(255) DEFAULT NULL COMMENT '联系方式',
  `supplierId` varchar(20) DEFAULT NULL COMMENT '供应商编码/内部人员为空',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `yph_goods`
-- ----------------------------
DROP TABLE IF EXISTS `yph_goods`;
CREATE TABLE `yph_goods` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `title` varchar(100) DEFAULT NULL COMMENT '标题',
  `des` varchar(100) DEFAULT NULL COMMENT '描述',
  `verifyBy` int(11) DEFAULT NULL COMMENT '审核人',
  `replyMsg` int(11) DEFAULT NULL COMMENT '审核信息',
  `verifyTime` datetime DEFAULT NULL COMMENT '审核时间',
  `verifyState` int(10) DEFAULT '0' COMMENT '审核状态 0:无状态 1:未通过 2:通过',
  `createBy` int(11) DEFAULT NULL COMMENT '创建人',
  `createTime` datetime DEFAULT NULL COMMENT '创建时间',
  `updateBy` int(11) DEFAULT NULL COMMENT '修改人',
  `updateTime` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='优品汇货品';

-- ----------------------------
--  Table structure for `yph_goods_img`
-- ----------------------------
DROP TABLE IF EXISTS `yph_goods_img`;
CREATE TABLE `yph_goods_img` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `goodsId` int(11) DEFAULT NULL COMMENT '归属人',
  `img` varchar(100) DEFAULT NULL COMMENT '图片',
  `thumbnail` varchar(100) DEFAULT NULL COMMENT '缩略图',
  `sorting` int(10) DEFAULT NULL COMMENT '排序',
  `createBy` int(11) DEFAULT NULL COMMENT '创建人',
  `createTime` datetime DEFAULT NULL COMMENT '创建时间',
  `updateBy` int(11) DEFAULT NULL COMMENT '修改人',
  `updateTime` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`),
  KEY `index_goodsId` (`goodsId`),
  KEY `index_sorting` (`sorting`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='优品汇图片';

SET FOREIGN_KEY_CHECKS = 1;
