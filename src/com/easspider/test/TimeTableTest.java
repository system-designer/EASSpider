package com.easspider.test;

import com.easspider.spider.LoginHandler;
import com.easspider.spider.TimeTableHandler;

public class TimeTableTest {
	public static void main(String[] args) throws Exception {
		getTimeTable();
	}

	private static void getTimeTable() {
		LoginHandler login = new LoginHandler();
		if (login.login()) {
			System.out.println("登录成功");
			TimeTableHandler timeTableHandler = new TimeTableHandler(login);
			timeTableHandler.setTimeTable();
		} else {
			System.out.println("登录失败,请检测用户名和密码!!!");
		}
	}
}
