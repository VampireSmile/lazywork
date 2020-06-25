package com.faceRecognition.DbConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class dbCreate {
	private String driver = "com.mysql.cj.jdbc.Driver";// mysql数据库驱动
	private String url = "jdbc:mysql://192.168.74.128:3306/SignedDB";// 数据库url
	private String db_user = "myuser";// 数据库用户名
	private String db_password = "mypassword";// 数据库密码

	public Connection createCon() {// 获取数据库连接
		Connection con = null;
		try {
			Class.forName(driver).newInstance();//注册驱动程序
			con = DriverManager.getConnection(url, db_user, db_password);//进行连接
		} catch (SQLException esql) {
			esql.printStackTrace();
		} catch (ClassNotFoundException eno) {
			eno.printStackTrace();
		} catch (IllegalAccessException ell) {
			ell.printStackTrace();
		} catch (InstantiationException ein) {
			ein.printStackTrace();
		}
		return con;
	}

}
