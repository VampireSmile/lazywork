CREATE DATABASE base_ssm_crud;

USE base_ssm_crud;

CREATE TABLE `tbl_emp` (
  `emp_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '员工id',
  `emp_name` varchar(20) DEFAULT NULL COMMENT '员工姓名',
  `gender` char(1) DEFAULT NULL COMMENT '员工性别',
  `email` varchar(50) DEFAULT NULL COMMENT '员工邮箱',
  `d_id` int(11) DEFAULT NULL COMMENT '部门id',
  PRIMARY KEY (`emp_id`),
  KEY `fk_emp_dept` (`d_id`),
  CONSTRAINT `fk_emp_dept` FOREIGN KEY (`d_id`) REFERENCES `tbl_dept` (`dept_id`)
) ENGINE=InnoDB AUTO_INCREMENT=56 DEFAULT CHARSET=utf8


CREATE TABLE `tbl_dept` (
  `dept_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '部门id',
  `dept_name` varchar(50) NOT NULL COMMENT '部门名字',
  PRIMARY KEY (`dept_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8
