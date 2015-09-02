package com.easspider.test;

import java.util.ArrayList;
import java.util.List;

import com.easspider.bean.Student;
import com.easspider.dao.StudentDao;

public class StudentDaoTest {
	public static void main(String[] args) {
		getList("0820");
	}

	/**
	 * 得到列表
	 */
	private static void getList(String birthDay) {
		StudentDao sd = new StudentDao();
		List<Student> list = sd.getList(birthDay);
		int times = 0;
		for (Student item : list) {
			if (times % 10 == 0) {
				System.out.println();
			} else {
				System.out.print(item.getStudentNo()+":"
						+ item.getName() + ",");
			}
			times++;
		}
	}

	/**
	 * 插入
	 */
	private static void insertList() {
		List<Student> studentList = new ArrayList<Student>();
		Student student = new Student();
		student.setName("test1");
		student.setGender("nan");
		studentList.add(student);
		StudentDao sd = new StudentDao();
		sd.insert(studentList);
	}
}
