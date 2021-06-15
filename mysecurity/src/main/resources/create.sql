create database if not exists udb;
use udb;

create table if not exists user
(
    id          bigint auto_increment comment '用户id' primary key,
    username    varchar(64)          null comment '用户名',
    password    varchar(256)          null comment '用户密码',
    enabled     tinyint(1) default 1 null comment '0:无效用户，1:有效用户',
    phone       varchar(16)          null comment '用户手机号',
    email       varchar(32)          null comment '用户邮箱',
    create_time datetime             null comment '创建时间'
) comment '用户表';
create table if not exists role
(
    id        int auto_increment comment '角色id' primary key,
    role_name varchar(32)  null comment '角色中文名',
    role_code varchar(32)  null comment '角色英文名',
    role_desc varchar(128) null comment '角色描述'
) comment '角色表';
create table if not exists user_role
(
    user_id int null comment '用户表id',
    role_id int null comment '角色表id'
) comment '用户角色表';

#注意下面两个密码需要替换成加密后的值
#按照RbacApplicationTests中方法生成加密后的密码，
# 然后替换123456，再插入数据库，如果使用123456的话会出现越界错误！！！
insert into user(username, password, enabled)
values ('zhangsan', '123456', '1');
insert into user(username, password, enabled)
values ('lisi', '123456', '1');
insert into role(role_name, role_code)
values ('管理员', 'admin');
insert into role(role_name, role_code)
values ('用户', 'user');
# 注意这里表示zhangsan是管理员角色，lisi是用户角色
insert into user_role(user_id, role_id)
values ('0', '0');
insert into user_role(user_id, role_id)
values ('1', '1');