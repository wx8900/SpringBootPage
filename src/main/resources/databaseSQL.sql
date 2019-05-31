CREATE DATABASE `test` CHARACTER SET utf8 COLLATE utf8_bin;

USE `test`;

DROP table if exists `tbl_student`;
CREATE TABLE `tbl_student` (
  `ID` int(10) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(100) NOT NULL,
  `PASSWORD` varchar(50) NOT NULL,
  `BRANCH` varchar(255) NOT NULL,
  `PERCENTAGE` int(3) NOT NULL,
  `PHONE` int(10) NOT NULL,
  `EMAIL` varchar(255) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

INSERT INTO `tbl_student` (`ID`, `NAME`,  `PASSWORD`, `BRANCH`, `PERCENTAGE`, `PHONE`, `EMAIL`)
VALUES
    (1, 'jack', '123', 'it', 20, 1211232, 'adaf@test.com'),
    (2, 'mark', '345', 'dev', 30, 333, 'ddd@test.com'),
    (3, 'tony',  '123', 'dev', 2, 444, 'tony@test.com'),
    (4, 'nancy', '123', 'dev', 23, 788, 'nancy@test.com'),
    (5, 'tommy', '001', 'it', 32, 2423, 'tommy@test.com'),
    (6, 'tom', '111', 'it', 23, 11, 'tom@test.com'),
    (7, 'Aka', '222', 'dev', 1, 2, 'aka@test.com'),
    (8, 'al', '333', 'dev', 2, 3, 'al@test.com'),
    (9, 'ben', '123456789', 'dev', 3, 4, 'ben@test.com'),
    (10, 'Mike', '001test', 'it', 5, 6, 'mike@test.com'),
    (11, 'Cow', '777', 'it', 3, 23, 'cow@test.com');