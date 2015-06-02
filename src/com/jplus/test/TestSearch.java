package com.jplus.test;

import com.jplus.bean.Student;
import com.jplus.spider.LoginPage;
import com.jplus.spider.StudentPick;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class TestSearch {

	public static void main(String[] args) throws Exception {
		// getStudentPicture();
		System.out.print(URLEncoder.encode("考研查分分数线"));
	}

	private static void getStudentBasicInfo() {
		LoginPage login = new LoginPage();
		if (login.login()) {
			System.out.println("登陆成功");
			StudentPick student = new StudentPick(login);
			student.searchBasicInfo("2014");
		} else {
			System.out.println("登录失败,请检测用户名和密码!!!");
		}
	}

	private static void getStudentPicture() {
		LoginPage login = new LoginPage();
		if (login.login()) {
			System.out.println("登陆成功");
			StudentPick student = new StudentPick(login);
			List<Student> students = student.searchPic("生命科学学院");
			student.getImage(students);
		} else {
			System.out.println("登录失败,请检测用户名和密码!!!");
		}
	}
}
