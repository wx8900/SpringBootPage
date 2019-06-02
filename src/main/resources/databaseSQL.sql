CREATE DATABASE `test` CHARACTER SET utf8 COLLATE utf8_bin;

USE `test`;

DROP table if exists `tbl_student`;
CREATE TABLE `tbl_student` (
  `ID` BIGINT(10) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(100) NOT NULL,
  `PASSWORD` varchar(50) NOT NULL,
  `BRANCH` varchar(255) NOT NULL,
  `PERCENTAGE` varchar(3) NOT NULL,
  `PHONE` varchar(10) NOT NULL,
  `EMAIL` varchar(255) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

INSERT INTO `tbl_student` (`ID`, `NAME`,  `PASSWORD`, `BRANCH`, `PERCENTAGE`, `PHONE`, `EMAIL`)
VALUES
    (1, 'jack', '123', 'it', '20', '1211232', 'adaf@test.com'),
    (2, 'mark', '345', 'dev', '30', '333', 'ddd@test.com'),
    (3, 'tony',  '123', 'dev', '2', '444', 'tony@test.com'),
    (4, 'nancy', '123', 'dev', '23', '788', 'nancy@test.com'),
    (5, 'tommy', '001', 'it', '32', '2423', 'tommy@test.com'),
    (6, 'tom', '111', 'it', '23', '11', 'tom@test.com'),
    (7, 'Aka', '222', 'dev', '1', '2', 'aka@test.com'),
    (8, 'al', '333', 'dev', '2', '3', 'al@test.com'),
    (9, 'ben', '123456789', 'dev', '3', '4', 'ben@test.com'),
    (10, 'Mike', '001test', 'it', '5', '6', 'mike@test.com'),
    (11, 'Cow', '777', 'it', '3', '23', 'cow@test.com');

DROP TABLE IF EXISTS `LOG_ERROR`;
CREATE TABLE `LOG_ERROR` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `StudentId` varchar(255) DEFAULT NULL,
  `Name` varchar(255) DEFAULT NULL,
  `Class` varchar(255) DEFAULT NULL,
  `Method` varchar(255) DEFAULT NULL,
  `CreateTime` varchar(255) DEFAULT NULL,
  `LogLevel` varchar(20) DEFAULT NULL,
  `LogLine` varchar(255) DEFAULT NULL,
  `MSG` varchar(555) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;