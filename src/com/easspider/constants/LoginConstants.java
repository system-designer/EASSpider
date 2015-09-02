package com.easspider.constants;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class LoginConstants {

	private static CloseableHttpClient client = null;
	public final static String user = "lwh";// 教务系统管理员的账号和密码
	public final static String password = "poiu1234";
	// 登录地址
	public static String loginPostUrl = "http://www.jwgl.hbnu.edu.cn/{0}/default2.aspx";
	// 验证码地址
	public static String checkCodeUrl = "http://www.jwgl.hbnu.edu.cn/{0}/CheckCode.aspx";
	// 查询学生信息的地址
	public static String searchUrl = "http://www.jwgl.hbnu.edu.cn/{0}/xsxx.aspx?xh="
			+ user + "&xm=&gnmkdm=N120306";
	public static String contentRef = "http://www.jwgl.hbnu.edu.cn/{0}/bm_main.aspx?xh="
			+ user;
	// 学生照片地址
	public static String imageUrl = "http://www.jwgl.hbnu.edu.cn/{0}/readimagexs.aspx?xh={1}";
	public static String contentUrl = "http://www.jwgl.hbnu.edu.cn/{0}/content.aspx";
	// 查询课表地址
	public static String timeTableUrl = "http://www.jwgl.hbnu.edu.cn/{0}/tjkbcx.aspx?xh="
			+ user + "&xm=&gnmkdm=N120310";

	private LoginConstants() {
	}

	public static CloseableHttpClient getCustomCloseableHttpClient() {
		return client == null ? HttpClients.custom().build() : client;
	}
}
