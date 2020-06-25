package com.faceRecognition.ServletUtil;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.faceRecognition.comDataHelper.dealdata;

import net.sf.json.JSONArray;

/**
 * Servlet implementation class TeacherGetAllData
 */
public class TeacherDealData_Servlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private PrintWriter out;// 输出流
	private dealdata deal;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public TeacherDealData_Servlet() {
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
		String allid = request.getParameter("_allid");// 获取请求参数
		// 老师获取学生签到数据并转为json数据
		JSONArray json = JSONArray.fromObject(deal.preselect(allid)); 
		out.print(json.toString());// 传回app客户端
		out.flush();
		out.close();
		try {
			if (deal.preinsertSigned(allid))// 将每个同学的签到情况插入签到表
				deal.afterupdateShouldSigned(allid);// 将状态重置
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
