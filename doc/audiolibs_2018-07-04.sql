# ************************************************************
# Sequel Pro SQL dump
# Version 4541
#
# http://www.sequelpro.com/
# https://github.com/sequelpro/sequelpro
#
# Host: 127.0.0.1 (MySQL 5.7.18)
# Database: audiolibs
# Generation Time: 2018-07-04 03:54:30 +0000
# ************************************************************


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


# Dump of table runtime
# ------------------------------------------------------------

DROP TABLE IF EXISTS `runtime`;

CREATE TABLE `runtime` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '已播放节目记录',
  `sequence` bigint(20) NOT NULL,
  `manager_id` bigint(20) DEFAULT NULL COMMENT '所属管理员ID',
  `channel_id` bigint(20) DEFAULT NULL COMMENT '频率ID',
  `clock_id` bigint(20) DEFAULT NULL COMMENT '钟型ID',
  `program_id` bigint(20) DEFAULT NULL COMMENT '节目单ID',
  `typed_id` bigint(20) DEFAULT NULL COMMENT '类型化ID',
  `res_id` bigint(20) DEFAULT NULL COMMENT '资源文件ID',
  `placeholder` tinyint(4) DEFAULT NULL COMMENT '是否是占位',
  `duration` int(8) DEFAULT NULL COMMENT '占位时长(秒)',
  `unitary` tinyint(4) DEFAULT NULL COMMENT '不允许截断',
  `playdate` timestamp NULL DEFAULT NULL COMMENT '播放日期',
  `playtime` varchar(8) DEFAULT NULL COMMENT '播放时间',
  `createtime` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;




/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
