package com.faceRecognition.DbConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class dbCreate {
	private String driver = "com.mysql.cj.jdbc.Driver";// mysql���ݿ�����
	private String url = "jdbc:mysql://192.168.74.128:3306/SignedDB";// ���ݿ�url
	private String db_user = "myuser";// ���ݿ��û���
	private String db_password = "mypassword";// ���ݿ�����

	public Connection createCon() {// ��ȡ���ݿ�����
		Connection con = null;
		try {
			Class.forName(driver).newInstance();//ע����������
			con = DriverManager.getConnection(url, db_user, db_password);//��������
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
