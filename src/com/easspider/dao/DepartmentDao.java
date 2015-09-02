package com.easspider.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import sun.applet.Main;

import com.easspider.bean.Department;
import com.easspider.bean.Student;
import com.easspider.database.DatabaseAccess;

public class DepartmentDao {
	public void insert(List<Department> departmentList){
		StringBuilder sb=new StringBuilder();
		String sql="insert into Department (name,code) " +
				"values ";
		sb.append(sql);
		int length=departmentList.size();
		for(int i=0;i<length;i++){
			Department department =departmentList.get(i);
			sb.append("(");
			sb.append("'"+department.getName()+"',");
			sb.append("'"+department.getCode()+"'");
			if(i<length-1){
				sb.append("),");
			}else{
				sb.append(")");
			}
		}
		System.out.println(sb.toString());
		DatabaseAccess da=new DatabaseAccess();
		da.update(sb.toString());
		da.close();
	}
	
	public List<Department> getList(){
		DatabaseAccess da=new DatabaseAccess();
		ResultSet rs= da.query("select * from department");
		List<Department> departmentList=new ArrayList<Department>();
        try {
        	while (rs.next()) {
        		Department department=new Department();
        		department.setName(rs.getString("name"));
        		department.setCode(rs.getString("code"));    
        		departmentList.add(department);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        da.close();
        return departmentList;
	}
}
