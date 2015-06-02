package com.jplus.test;

import com.jplus.spider.LoginPage;
import com.jplus.spider.TimeTableHandler;

public class TestSearchTimeTable {
	 public static void main(String[] args) throws Exception {
		 getTimeTable();
	    }
	    
		private static void getTimeTable() {
			LoginPage login = new LoginPage();
	        if (login.login()) {
	            System.out.println("登陆成功");
	            TimeTableHandler timeTableHandler = new TimeTableHandler(login);
	            timeTableHandler.searchTimeTable();
	        } else {
	            System.out.println("登录失败,请检测用户名和密码!!!");
	        }
		}
}
