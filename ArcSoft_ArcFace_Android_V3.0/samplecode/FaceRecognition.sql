/*
 *
 *人脸识别考勤后台数据库设计
 *
 */

CREATE DATABASE SignedDB;/*创建数据库*/

USE SignedDB;/*使用数据库*/

CREATE TABLE info_list(/*学生信息表*/
  /*提前录入表中得数据*/
  _id VARCHAR(15) PRIMARY KEY,/*学号或教工号*/
  _pass VARCHAR(20),/*密码*/
  _deptId VARCHAR(16) DEFAULT '-1',/*专业班级,老师默认-1*/
  _name VARCHAR(10),/*姓名*/
  _role VARCHAR(2),/*角色2为管理员,1为教师,0为学生*/
  /*运行过程中需改变的*/
  _isSigned VARCHAR(2) DEFAULT '0',/*是否已经签到*/
  _shouldSigned VARCHAR(2) DEFAULT '0',/*用于每次考勤老师发起签到*/
  _sumSigned INT DEFAULT 0,/*总签到数,默认为0,成功签到+1*/
  _shouldSumSigned INT DEFAULT 0,/*应该签到数,默认为0,老师发起一次签到+1*/
  _faceInfo VARCHAR(2000) DEFAULT NULL,/*人脸特征信息*/
  _time VARCHAR(30) DEFAULT 'NOTSigned',/*签到时间*/
  _location VARCHAR(50) DEFAULT 'UNKNOW'/*地理位置*/
  );

CREATE TABLE Signed_info(/*签到信息表*/
  _Sid VARCHAR(15),/*学号*/
  _SdeptId VARCHAR(16) DEFAULT '-1',/*专业班级号*/
  _Sname VARCHAR(10),/*姓名*/
  _SisSigned VARCHAR(2) DEFAULT '0',/*用统计签到情况*/
  _StimeSigned VARCHAR(30),/*当次签到时间*/
  _location VARCHAR(50)/*地理位置*/
  );
  
/*测试数据*/
insert into info_list(_id,_pass,_deptId,_name,_role)values('1705020117','123456','-1','汪老师','1');/*老师*/
insert into info_list(_id,_pass,_deptId,_name,_role)values('1705020118','123456','网络一班','yjw','0');
insert into info_list(_id,_pass,_deptId,_name,_role)values('1705020119','123456','网络二班','田涛','0');
insert into info_list(_id,_pass,_deptId,_name,_role)values('1705020120','123456','网络三班','李四','0');
insert into info_list(_id,_pass,_deptId,_name,_role)values('1705020122','123456','-1','刘老师','2');/*管理员*/
    