/*
Navicat MariaDB Data Transfer

Source Server         : root
Source Server Version : 100021
Source Host           : localhost:3306
Source Database       : lntuonline

Target Server Type    : MariaDB
Target Server Version : 100021
File Encoding         : 65001

Date: 2016-01-22 17:48:51
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for admin
-- ----------------------------
DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin` (
  `user_id` varchar(255) NOT NULL,
  `email` varchar(255) DEFAULT NULL,
  `subscribe` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for advice
-- ----------------------------
DROP TABLE IF EXISTS `advice`;
CREATE TABLE `advice` (
  `id` varchar(255) NOT NULL,
  `user_id` varchar(255) DEFAULT NULL,
  `create_at` datetime DEFAULT NULL,
  `user_agent` varchar(255) DEFAULT NULL,
  `content` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for crash_log
-- ----------------------------
DROP TABLE IF EXISTS `crash_log`;
CREATE TABLE `crash_log` (
  `id` varchar(255) NOT NULL,
  `user_id` varchar(255) DEFAULT NULL,
  `create_at` datetime DEFAULT NULL,
  `user_agent` varchar(255) DEFAULT NULL,
  `content` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` varchar(255) NOT NULL,
  `login_token` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `update_at` datetime DEFAULT NULL,
  `expires_at` datetime DEFAULT NULL,
  `create_at` datetime DEFAULT NULL,
  `user_agent` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for user_detail
-- ----------------------------
DROP TABLE IF EXISTS `user_detail`;
CREATE TABLE `user_detail` (
  `id` varchar(255) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `class_info` varchar(255) DEFAULT NULL,
  `sex` varchar(255) DEFAULT NULL,
  `college` varchar(255) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `update_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
