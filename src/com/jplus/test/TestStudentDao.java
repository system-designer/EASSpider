package com.jplus.test;

import java.util.ArrayList;
import java.util.List;

import com.jplus.bean.Student;
import com.jplus.dao.StudentDao;

public class TestStudentDao {
	public static void main(String[] args) {
		getList();
	}

	private static void getList() {
		StudentDao sd = new StudentDao();
		List<Student> list = sd.getList();
		int times = 0;
		for (Student item : list) {
			if (times % 10 == 0) {
				System.out.println();
			} else {
				System.out.print(item.getStudentNo().substring(9) + ":"
						+ item.getName() + ",");
			}
			times++;
		}
	}

	private static void insertMany() {
		List<Student> studentList = new ArrayList<Student>();
		Student student = new Student();
		student.setName("test1");
		student.setGender("nan");
		studentList.add(student);
		StudentDao sd = new StudentDao();
		sd.insert(studentList);
	}
}
