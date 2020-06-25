package com.faceRecognition.ServletUtil;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.faceRecognition.comDataHelper.Constants;
import com.faceRecognition.comDataHelper.dealdata;

/**
 * Servlet implementation class UpdateOwnState_Servlet
 */
public class UpdateOwnState_Servlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private PrintWriter out;// 输出流
	private dealdata deal;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UpdateOwnState_Servlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setCharacterEncoding("utf-8");// 设置服务器输出流编码格式
		out = response.getWriter();// 获取输出流
		deal = new dealdata();// 获取处理数据类
		String uid = request.getParameter("userid");
		String location = request.getParameter("info");
		int result = deal.updateSignedinfo(uid, location);
		if (result > 0)
			out.print(Constants.success + "");
		else
			out.print(Constants.fail + "");
		out.flush();
		out.close();
	}

}
