package com.easspider.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


import com.easspider.bean.ClassInfo;
import com.easspider.bean.Major;
import com.easspider.database.DatabaseAccess;

public class ClassInfoDao {
	public void insert(List<ClassInfo> classInfoList){
		StringBuilder sb=new StringBuilder();
		String sql="insert into ClassInfo (name,code,grade,department,major) " +
				"values ";
		sb.append(sql);
		int length=classInfoList.size();
		for(int i=0;i<length;i++){
			ClassInfo classInfo =classInfoList.get(i);
			sb.append("(");
			sb.append("'"+classInfo.getName()+"',");
			sb.append("'"+classInfo.getCode()+"',");
			sb.append("'"+classInfo.getGrade()+"',");
			sb.append("'"+classInfo.getDepartment()+"',");
			sb.append("'"+classInfo.getMajor()+"'");
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
	
	public void update(ClassInfo classInfo){
		String sql="update ClassInfo set timetable='" +classInfo.getTimeTable()+
				"' where id="+classInfo.getId();
		DatabaseAccess da=new DatabaseAccess();
		da.update(sql);
		da.close();
	}
	
	public List<ClassInfo> getList(){
		DatabaseAccess da=new DatabaseAccess();
		ResultSet rs= da.query("select * from classInfo where timetable='' or timetable is null");
		List<ClassInfo> classInfoList=new ArrayList<ClassInfo>();
        try {
        	while (rs.next()) {
        		ClassInfo classInfo=new ClassInfo();
        		classInfo.setId(rs.getInt("id"));
        		classInfo.setName(rs.getString("name"));
        		classInfo.setCode(rs.getString("code"));    
        		classInfo.setGrade(rs.getString("grade")); 
        		classInfo.setDepartment(rs.getString("department"));    
        		classInfo.setMajor(rs.getString("major")); 
        		 classInfoList.add(classInfo);
            }
        } catch (SQLException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
        da.close();
        return classInfoList;
	}
}
