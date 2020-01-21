CREATE DATABASE `test` CHARACTER SET utf8 COLLATE utf8_bin;

USE `test`;

DROP table if exists `tbl_student`;
CREATE TABLE `tbl_student` (
  `ID` BIGINT(10) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(100) NOT NULL,
  `PASSWORD` varchar(50) NOT NULL,
  `BRANCH` varchar(255) DEFAULT NULL,
  `PERCENTAGE` varchar(3) DEFAULT NULL,
  `PHONE` varchar(10) DEFAULT NULL,
  `EMAIL` varchar(255) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

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
  `Class` varchar(255) DEFAULT NULL,
  `Method` varchar(255) DEFAULT NULL,
  `Thread` varchar(255) DEFAULT NULL,
  `CreateTime` varchar(255) DEFAULT NULL,
  `LogLevel` varchar(20) DEFAULT NULL,
  `LogLine` varchar(255) DEFAULT NULL,
  `MSG` varchar(555) DEFAULT NULL,
  PRIMARY KEY (`id`)
  ) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `tbl_book`;
CREATE TABLE `tbl_book` (
      `ID` BIGINT(10) NOT NULL AUTO_INCREMENT,
      `ISBN` varchar(20) DEFAULT NULL,
      `Category` varchar(20) DEFAULT NULL,
      `Author` varchar(100) DEFAULT NULL,
      `Name` varchar(50) DEFAULT NULL,
      `Price` varchar(20) DEFAULT NULL,
      `Desc` varchar(100) DEFAULT NULL,
      `Publisher` varchar(50) DEFAULT NULL,
      `PublishDate` varchar(10) DEFAULT NULL,
      `Available`  int(1) DEFAULT NULL,
      PRIMARY KEY (`ID`)
      ) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

INSERT INTO `tbl_book` (`ID`, `ISBN`,  `Category`, `Author`, `Name`, `Price`, `Desc`, `Publisher`, `PublishDate`, `Available`)
VALUES (1, '978-0-9845888-0-2',  'Computer Science', 'Alex', 'Java Program Introduce', '18.62', 'This is a old Java book', 'China East Book Group', '2019-06-10', 1);

INSERT INTO `tbl_book` (`ID`, `ISBN`,  `Category`, `Author`, `Name`, `Price`, `Desc`, `Publisher`, `PublishDate`, `Available`)
VALUES (2, '347-0-9845888-1-3',  'Machinery', 'Dan Tom', 'Vehicle System Design', '53.20', 'This is a machinery book', 'East Education Company', '2009-06-28', 1);

INSERT INTO `tbl_book` (`ID`, `ISBN`,  `Category`, `Author`, `Name`, `Price`, `Desc`, `Publisher`, `PublishDate`, `Available`)
VALUES (3, '685-0-9846658-9-1',  'Medicine', 'Jed Jiang', 'Medicine Cases Research', '43.98', 'This is a research book introduced medicine data', 'Book of USA', '2015-08-16', 1);

INSERT INTO `tbl_book` (`ID`, `ISBN`,  `Category`, `Author`, `Name`, `Price`, `Desc`, `Publisher`, `PublishDate`, `Available`)
VALUES (4, '138-0-9845888-2-5',  'Computer Science', 'Anne', 'Thinking in Java', '1866.28', 'This is a senior level book of Java', 'China North Group', '2010-09-02', 1);

INSERT INTO `tbl_book` (`ID`, `ISBN`,  `Category`, `Author`, `Name`, `Price`, `Desc`, `Publisher`, `PublishDate`, `Available`)
VALUES (5, '221-0-9845888-3-8',  'Computer Science', 'Tomas', 'Hand First', '1.68', 'This is a hand book of Java', 'World Book Group', '2012-01-01', 1);



DROP TABLE IF EXISTS `tbl_student_book`;
CREATE TABLE `tbl_student_book` (
      `id` BIGINT(10) NOT NULL AUTO_INCREMENT,
      `Uid` BIGINT(10) DEFAULT NULL,
      `Bid` BIGINT(10) DEFAULT NULL,
      `ExtendTimes` int(1) DEFAULT NULL,
      `ReturnDate` varchar(20) DEFAULT NULL,
      `IsReturned` int(1) DEFAULT NULL,
      PRIMARY KEY (`ID`)
      )  ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

INSERT INTO `tbl_student_book` (`id`, `Uid`,  `Bid`, `ExtendTimes`, `ReturnDate`, `IsReturned`)
VALUES (1, 2,  5, 1, '2019-06-26', 0);

INSERT INTO `tbl_student_book` (`id`, `Uid`,  `Bid`, `ExtendTimes`, `ReturnDate`, `IsReturned`)
VALUES (2, 3,  4, 1, '2019-06-30', 0);

INSERT INTO `tbl_student_book` (`id`, `Uid`,  `Bid`, `ExtendTimes`, `ReturnDate`, `IsReturned`)
VALUES (3, 10,  3, 1, '2019-07-06', 0);


create table `product_info`(
	`product_id` varchar(32) not null,
	`product_name` varchar(64) not null comment '商品名称',
	`product_price` decimal(8,2) not null comment '单价',
	`product_stock` int not null comment '库存',
	`product_description` varchar(64) comment '描述',
	`product_icon` varchar(512) comment '小图',
	`category_type` int not null comment '类目编号',
	`create_time` timestamp not null default current_timestamp comment '创建时间',
	`update_time` timestamp not null default current_timestamp on update current_timestamp comment '修改时间',
	primary key (`product_id`)
) comment '商品表';

create table `product_category`(
	`category_id` int not null auto_increment,
	`category_name` varchar(64) not null comment '类目名字',
	`category_type` int not null comment '类目编号',
	`create_time` timestamp not null default current_timestamp comment '创建时间',
	`update_time` timestamp not null default current_timestamp on update current_timestamp comment '修改时间',
	primary key (`category_id`),
	unique key `uqe_category_type` (`category_type`)
) comment '类目表';

create table `order_master`(
	`order_id` varchar(32) not null,
	`buyer_name` varchar(32) not null comment '买家名字',
	`buyer_phone` varchar(32) not null comment '买家电话',
	`buyer_address` varchar(128) not null comment '买家地址',
	`buyer_openid` varchar(64) not null comment '买家微信openid',
	`order_amount` decimal(8,2) not null comment '订单总金额',
	`order_status` tinyint(3) not null default '0' comment '订单状态，默认0新下单',
	`pay_status` tinyint(3) not null default '0'comment '支付状态，默认0未支付',
	`create_time` timestamp not null default current_timestamp comment '创建时间',
	`update_time` timestamp not null default current_timestamp on update current_timestamp comment '修改时间',
	primary key (`order_id`),
	key `idx_buyer_openid` (`buyer_openid`)
) comment '订单主表';

create table `order_detail` (
	`detail_id` varchar(32) not null,
	`order_id` varchar(32) not null,
	`product_id` varchar(32) not null,
	`product_name` varchar(64) not null comment '商品名称',
	`product_price` decimal(8,2) not null comment '商品价格',
	`product_quantity` int not null comment '商品数量',
	`product_icon` varchar(512) not null comment '商品小图',
	`create_time` timestamp not null default current_timestamp comment '创建时间',
	`update_time` timestamp not null default current_timestamp on update current_timestamp comment '修改时间',
	primary key (`detail_id`),
	key `idx_order_id` (`order_id`)
) comment '订单详情表';






