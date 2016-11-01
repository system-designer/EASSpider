package com.easspider.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.easspider.bean.Department;
import com.easspider.bean.Grade;
import com.easspider.database.DatabaseAccess;

public class GradeDao {
	public void insert(List<Grade> gradeList){
		StringBuilder sb=new StringBuilder();
		String sql="insert into grade (name,code) " +
				"values ";
		sb.append(sql);
		int length=gradeList.size();
		for(int i=0;i<length;i++){
			Grade grade =gradeList.get(i);
			sb.append("(");
			sb.append("'"+grade.getName()+"',");
			sb.append("'"+grade.getCode()+"'");
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
	
	public List<Grade> getList(){
		DatabaseAccess da=new DatabaseAccess();
		ResultSet rs= da.query("select * from grade");
		List<Grade> departmentList=new ArrayList<Grade>();
        try {
        	while (rs.next()) {
        		Grade department=new Grade();
        		department.setName(rs.getString("name"));
        		department.setCode(rs.getString("code"));    
        		departmentList.add(department);
            }
        } catch (SQLException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
        da.close();
        return departmentList;
	}
}
