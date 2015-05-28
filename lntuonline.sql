/*
Navicat MariaDB Data Transfer

Source Server         : root
Source Server Version : 100017
Source Host           : localhost:3306
Source Database       : lntuonline

Target Server Type    : MariaDB
Target Server Version : 100017
File Encoding         : 65001

Date: 2015-05-28 10:29:20
*/

SET FOREIGN_KEY_CHECKS=0;

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
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
