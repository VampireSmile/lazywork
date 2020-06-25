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
	private PrintWriter out;// �����
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
		response.setCharacterEncoding("utf-8");// ���÷���������������ʽ
		out = response.getWriter();// ��ȡ�����
		deal = new dealdata();// ��ȡ����������
		String allid = request.getParameter("_allid");// ��ȡ�������
		// ��ʦ��ȡѧ��ǩ�����ݲ�תΪjson����
		JSONArray json = JSONArray.fromObject(deal.preselect(allid)); 
		out.print(json.toString());// ����app�ͻ���
		out.flush();
		out.close();
		try {
			if (deal.preinsertSigned(allid))// ��ÿ��ͬѧ��ǩ���������ǩ����
				deal.afterupdateShouldSigned(allid);// ��״̬����
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
