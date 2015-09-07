# MySQL-Front 5.1  (Build 4.2)

/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE */;
/*!40101 SET SQL_MODE='STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES */;
/*!40103 SET SQL_NOTES='ON' */;


# Host: localhost    Database: hbnudata
# ------------------------------------------------------
# Server version 5.5.15

DROP DATABASE IF EXISTS `hbnudata`;
CREATE DATABASE `hbnudata` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `hbnudata`;

#
# Source for table classinfo
#

DROP TABLE IF EXISTS `classinfo`;
CREATE TABLE `classinfo` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `code` varchar(255) DEFAULT NULL,
  `grade` varchar(255) DEFAULT NULL,
  `department` varchar(255) DEFAULT NULL,
  `major` varchar(255) DEFAULT NULL,
  `timetable` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2456 DEFAULT CHARSET=utf8;

#
# Source for table department
#

DROP TABLE IF EXISTS `department`;
CREATE TABLE `department` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `code` varchar(255) DEFAULT NULL COMMENT '编号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=42 DEFAULT CHARSET=utf8;

#
# Source for table grade
#

DROP TABLE IF EXISTS `grade`;
CREATE TABLE `grade` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `code` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

#
# Source for table major
#

DROP TABLE IF EXISTS `major`;
CREATE TABLE `major` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `code` varchar(255) DEFAULT NULL,
  `department` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=109 DEFAULT CHARSET=utf8 COMMENT='专业';

#
# Source for table student
#

DROP TABLE IF EXISTS `student`;
CREATE TABLE `student` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `studentNo` varchar(255) DEFAULT NULL,
  `gender` varchar(255) DEFAULT NULL,
  `nation` varchar(255) DEFAULT NULL,
  `politicalStatus` varchar(255) DEFAULT NULL COMMENT '政治面貌',
  `nativePlace` varchar(255) DEFAULT NULL COMMENT '籍贯',
  `majorName` varchar(255) DEFAULT NULL COMMENT '专业名称',
  `schoolSystem` varchar(255) DEFAULT NULL COMMENT '学制',
  `examineeNo` varchar(255) DEFAULT NULL COMMENT '准考证号',
  `idCard` varchar(255) DEFAULT NULL COMMENT '身份证号',
  `className` varchar(255) DEFAULT NULL COMMENT '行政班名称',
  `classInfoId` int(11) DEFAULT NULL COMMENT '班级id',
  `depName` varchar(255) DEFAULT NULL COMMENT '学院名称',
  `yearOfAdmission` varchar(255) DEFAULT NULL COMMENT '入学年份',
  `enrollmentStatus` varchar(255) DEFAULT NULL COMMENT '学籍状态',
  `majorDirection` varchar(255) DEFAULT NULL COMMENT '专业方向',
  `birthDate` varchar(255) DEFAULT NULL COMMENT '出生日期',
  `admissionDate` varchar(255) DEFAULT NULL COMMENT '入学日期',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=17537 DEFAULT CHARSET=utf8;

#
# Source for table timetable
#

DROP TABLE IF EXISTS `timetable`;
CREATE TABLE `timetable` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `schoolyear` varchar(10) DEFAULT NULL COMMENT '学年',
  `schoolperiod` varchar(10) DEFAULT NULL COMMENT '学期',
  `grade` varchar(10) DEFAULT NULL COMMENT '年级',
  `depname` varchar(50) DEFAULT NULL COMMENT '学院',
  `major` varchar(50) DEFAULT NULL COMMENT '专业',
  `classname` varchar(10) DEFAULT NULL COMMENT '班级',
  `path` text COMMENT '课表位置',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='课表';

#
# Source for procedure proc_CURSOR
#

DROP PROCEDURE IF EXISTS `proc_CURSOR`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `proc_CURSOR`()
BEGIN
    DECLARE b,i INT;    
    DECLARE depNameStr,studentNoStr VARCHAR(255);
    DECLARE cur1 CURSOR FOR select distinct(depName) AS depName,SUBSTRING(studentNo,5,5)as studentNo from student where studentNo like '2011%' and depName!='' and depName is not null order by id;
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET b = 1;
    OPEN cur1;
 
    SET b = 0;
    SET i=0;        

    WHILE b = 0&&i<24 DO
        FETCH cur1 INTO depNameStr,studentNoStr;
        INSERT INTO department (name,code) VALUES (depNameStr,studentNoStr);        
        SET i=i+1;
    END WHILE;
 
    CLOSE cur1;
END;


/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
