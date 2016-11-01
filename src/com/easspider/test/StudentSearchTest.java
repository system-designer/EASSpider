package com.easspider.test;

import com.easspider.bean.Student;
import com.easspider.spider.LoginHandler;
import com.easspider.spider.StudentHandler;

import java.util.List;

public class StudentSearchTest {

	public static void main(String[] args) throws Exception {
		getStudentPicture();
	}

	/**
	 * 得到学生列表
	 */
	private static void getStudentList() {
		LoginHandler login = new LoginHandler();
		if (login.login()) {
			System.out.println("登录成功");
			StudentHandler student = new StudentHandler(login);
			student.getStudentInfoList("2016");
		} else {
			System.out.println("登录失败,请检测用户名和密码!!!");
		}
	}

	/**
	 * 得到学生照片
	 */
	private static void getStudentPicture() {
		LoginHandler login = new LoginHandler();
		if (login.login()) {
			System.out.println("登录成功");
			StudentHandler student = new StudentHandler(login);
			List<Student> students = student.getStudentList("文学院");
			student.getStudentPicture(students);
		} else {
			System.out.println("登录失败,请检测用户名和密码!!!");
		}
	}
}
