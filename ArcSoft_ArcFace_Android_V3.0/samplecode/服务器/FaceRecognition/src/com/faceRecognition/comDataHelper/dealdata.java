package com.faceRecognition.comDataHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.faceRecognition.DbConfig.dbCreate;
import com.faceRecognition.modelHelper.GetOwnInfo;
import com.faceRecognition.modelHelper.LoginInfo;
import com.faceRecognition.modelHelper.NeedInfo;

import net.sf.json.JSONArray;

public class dealdata {
	dbCreate db = new dbCreate();

	public dealdata() {
	}

	public String login(String uid, String upass) {// 登录
		List<LoginInfo> list = new ArrayList<>();
		Connection con = db.createCon();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		LoginInfo logininfo = new LoginInfo();
		logininfo.setState(Constants.fail);
		String sql = "select * from info_list where _id='" + uid + "' and _pass='" + upass + "';";
		try {
			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();
			while (rs.next()) {
				logininfo.setState(Constants.success);
				logininfo.setStudentid(rs.getString("_id"));
				logininfo.setRole(rs.getString("_role"));
				logininfo.setShouldSigned(rs.getString("_shouldSigned"));
				logininfo.setFaceInfo(rs.getString("_faceInfo"));
				list.add(logininfo);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stmt != null)
					stmt.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (logininfo.getState() == Constants.fail)
			list.add(logininfo);
		JSONArray json = JSONArray.fromObject(list);
		return json.toString();
	}

	public int updatePass(String uid, String info) {// 更新密码
		String sql = "update info_list set _pass='" + info + "' where _id='" + uid + "';";
		int is_Success = 0;
		Connection con = db.createCon();
		PreparedStatement stmt = null;
		try {
			con.setAutoCommit(false);
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		try {
			stmt = con.prepareStatement(sql);
			is_Success = stmt.executeUpdate();
			con.commit();
		} catch (SQLException e) {
			try {
				con.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} finally {
			try {
				stmt.close();
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return is_Success;
	}

	public int uploadfaceinfo(String uid, String info) {// 更新人脸特征数据
		String sql = "update info_list set _faceInfo='" + info + "' where _id='" + uid + "';";
		int is_Success = 0;
		Connection con = db.createCon();
		PreparedStatement stmt = null;
		try {
			con.setAutoCommit(false);
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		try {
			stmt = con.prepareStatement(sql);
			is_Success = stmt.executeUpdate();
			con.commit();
		} catch (SQLException e) {
			try {
				con.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} finally {
			try {
				stmt.close();
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return is_Success;
	}

	public int updateSignedinfo(String uid, String location) {// 更新自己签到状态为已签到,签到总数加一,修改地理位置
		String sql = "update info_list set _isSigned='1',_sumSigned=_sumSigned+1,_time=now(),_location='" + location
				+ "' where _id='" + uid + "';";
		int is_Success = 0;
		Connection con = db.createCon();
		PreparedStatement stmt = null;
		try {
			stmt = con.prepareStatement(sql);
			is_Success = stmt.executeUpdate();
		} catch (SQLException e) {
			try {
				con.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} finally {
			try {
				stmt.close();
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return is_Success;
	}

	public String getOwninfo(String uid) {// 查询自己的签到信息
		String sql = "select * from Signed_info where _Sid='" + uid + "';";
		List<GetOwnInfo> list = new ArrayList<>();
		Connection con = db.createCon();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();
			while (rs.next()) {
				GetOwnInfo getowninfo = new GetOwnInfo();
				getowninfo.setId(rs.getString("_Sid"));
				getowninfo.setDept(rs.getString("_SdeptId"));
				getowninfo.setName(rs.getString("_Sname"));
				getowninfo.setState(rs.getString("_SisSigned"));
				getowninfo.setTime(rs.getString("_StimeSigned"));
				getowninfo.setLocation(rs.getString("_location"));
				list.add(getowninfo);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stmt != null)
					stmt.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (list.size() == 0) {
			GetOwnInfo getowninfo = new GetOwnInfo();
			getowninfo.setId("-1");
			list.add(getowninfo);
		}
		JSONArray json = JSONArray.fromObject(list);
		return json.toString();
	}

	public int preupdateShouldSigned(String alluid) {// 老师将需要签到的同学标志位设为1,更新数据
		String arr[] = alluid.split(",");
		StringBuilder sb = new StringBuilder();
		sb.append(
				"update info_list set _shouldSigned='1',_isSigned='0',_time=now(),_shouldSumSigned=_shouldSumSigned+1 where _deptId in (");
		for (int i = 0; i < arr.length; i++) {
			if (i < arr.length - 1)
				sb.append("'" + arr[i] + "',");
			else
				sb.append("'" + arr[i] + "');");
		}
		String sql = sb.toString();
		int is_Success = 0;
		Connection con = db.createCon();
		PreparedStatement stmt = null;
		try {
			stmt = con.prepareStatement(sql);
			is_Success = stmt.executeUpdate();
		} catch (SQLException e) {
			try {
				con.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} finally {
			try {
				stmt.close();
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return is_Success;
	}

	public int afterupdateShouldSigned(String alluid) {// 重新将签到的同学是否已经签到和需要签到设为0,位置和时间重置
		String arr[] = alluid.split(",");
		StringBuilder sb = new StringBuilder();
		sb.append(
				"update info_list set _shouldSigned='0',_isSigned='0',_time='NOTSigned',_location='UNKNOW' where _deptId in (");
		for (int i = 0; i < arr.length; i++) {
			if (i < arr.length - 1)
				sb.append("'" + arr[i] + "',");
			else
				sb.append("'" + arr[i] + "');");
		}
		String sql = sb.toString();
		int is_Success = 0;
		Connection con = db.createCon();
		PreparedStatement stmt = null;
		try {
			stmt = con.prepareStatement(sql);
			is_Success = stmt.executeUpdate();
		} catch (SQLException e) {
			try {
				con.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} finally {
			try {
				stmt.close();
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return is_Success;
	}

	public List<GetOwnInfo> preselect(String alluid) {// 查询签到的班级的同学信息
		String arr[] = alluid.split(",");
		StringBuilder sb = new StringBuilder();
		sb.append("select * from info_list where _deptId in (");
		for (int i = 0; i < arr.length; i++) {
			if (i < arr.length - 1)
				sb.append("'" + arr[i] + "',");
			else
				sb.append("'" + arr[i] + "');");
		}
		String sql = sb.toString();
		List<GetOwnInfo> list = new ArrayList<>();
		Connection con = db.createCon();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();
			while (rs.next()) {
				GetOwnInfo needinfo = new GetOwnInfo();
				needinfo.setId(rs.getString("_id"));
				needinfo.setDept(rs.getString("_deptId"));
				needinfo.setName(rs.getString("_name"));
				needinfo.setState(rs.getString("_isSigned"));
				needinfo.setTime(rs.getString("_time"));
				needinfo.setLocation(rs.getString("_location"));
				list.add(needinfo);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stmt != null)
					stmt.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return list;
	}

	public boolean preinsertSigned(String alluid) {// 向签到表中插入签到班级的数据
		List<GetOwnInfo> pregetOwninfo = preselect(alluid);
		int is_Success = 0, allsuccess = 0;
		Connection con = db.createCon();
		PreparedStatement stmt = null;
		try {
			con.setAutoCommit(false);
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		List<String> sql_list = new ArrayList<>();
		for (int i = 0; i < pregetOwninfo.size(); i++) {
			String sql = "insert into Signed_info values('" + pregetOwninfo.get(i).getId() + "','"
					+ pregetOwninfo.get(i).getDept() + "','" + pregetOwninfo.get(i).getName() + "','"
					+ pregetOwninfo.get(i).getState() + "','" + pregetOwninfo.get(i).getTime() + "','"
					+ pregetOwninfo.get(i).getLocation() + "');";
			sql_list.add(sql);
		}

		try {
			for (int i = 0; i < sql_list.size(); i++) {
				stmt = con.prepareStatement(sql_list.get(i));
				is_Success = stmt.executeUpdate();
				con.commit();
				allsuccess += is_Success;
			}
		} catch (SQLException e) {
			try {
				con.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} finally {
			try {
				stmt.close();
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return allsuccess != sql_list.size() ? false : true;
	}

	public String adminSelectInfo() {// 管理员查询
		String sql = "select * from info_list where _role='0' or _role='1';";
		List<NeedInfo> list = new ArrayList<>();
		Connection con = db.createCon();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();
			while (rs.next()) {
				NeedInfo getowninfo = new NeedInfo();
				getowninfo.setId(rs.getString("_id"));
				getowninfo.setDept(rs.getString("_deptId"));
				getowninfo.setName(rs.getString("_name"));
				if (rs.getString("_role").equals("0")) {
					getowninfo.setRole("学生");
				} else if (rs.getString("_role").equals("1")) {
					getowninfo.setRole("老师");
				}
				getowninfo.setTimes(rs.getString("_sumSigned"));
				getowninfo.setShouldtimes(rs.getString("_shouldSumSigned"));
				list.add(getowninfo);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stmt != null)
					stmt.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (list.size() == 0) {
			NeedInfo getowninfo = new NeedInfo();
			getowninfo.setId("-1");
			list.add(getowninfo);
		}
		JSONArray json = JSONArray.fromObject(list);
		return json.toString();
	}
}
