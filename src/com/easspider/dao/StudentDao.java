package com.easspider.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import sun.applet.Main;

import com.easspider.bean.Student;
import com.easspider.database.DatabaseAccess;

public class StudentDao {
	/**
	 * 批量插入学生数据
	 * 
	 * @param studentList
	 *            学生列表
	 */
	public void insert(List<Student> studentList) {
		StringBuilder sb = new StringBuilder();
		String sql = "insert into student (studentNo,name,birthDate,politicalStatus"
				+ ",nation,depName,majorName,className,schoolSystem,enrollmentStatus"
				+ ",yearOfAdmission,majorDirection,admissionDate,idCard) "
				+ "values ";
		sb.append(sql);
		int length = studentList.size();
		for (int i = 0; i < length; i++) {
			Student student = studentList.get(i);
			sb.append("(");
			sb.append("'" + student.getStudentNo() + "',");
			sb.append("'" + student.getName() + "',");
			sb.append("'" + student.getBirthDate() + "',");
			sb.append("'" + student.getPoliticalStatus() + "',");

			sb.append("'" + student.getNation() + "',");
			sb.append("'" + student.getDepName() + "',");
			sb.append("'" + student.getMajorName() + "',");
			sb.append("'" + student.getClassName() + "',");
			sb.append("'" + student.getSchoolSystem() + "',");
			sb.append("'" + student.getEnrollmentStatus() + "',");

			sb.append("'" + student.getYearOfAdmission() + "',");
			sb.append("'" + student.getMajorDirection() + "',");
			sb.append("'" + student.getAdmissionDate() + "',");
			sb.append("'" + student.getIdCard() + "'");
			if (i < length - 1) {
				sb.append("),");
			} else {
				sb.append(")");
			}
		}
		System.out.println(sb.toString());
		DatabaseAccess da = new DatabaseAccess();
		da.update(sb.toString());
		da.close();
	}

	public List<Student> getList(String birthDay) {
		DatabaseAccess da = new DatabaseAccess();
		ResultSet rs = da
				.query("select studentNo,name,substr(idCard,11,4) as birthDay "
						+ "from student having birthDay= '"+birthDay+"' order by studentNo");
		List<Student> stuList = new ArrayList<Student>();
		try {
			while (rs.next()) {
				Student student = new Student();
				student.setStudentNo(rs.getString("studentNo"));
				student.setName(rs.getString("name"));
				stuList.add(student);
			}
		} catch (SQLException ex) {
			Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
		}
		da.close();
		return stuList;
	}
}
