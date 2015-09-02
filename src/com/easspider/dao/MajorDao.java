package com.easspider.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import sun.applet.Main;

import com.easspider.bean.Department;
import com.easspider.bean.Major;
import com.easspider.database.DatabaseAccess;

public class MajorDao {
	public void insert(List<Major> majorList){
		StringBuilder sb=new StringBuilder();
		String sql="insert into major (name,code,department) " +
				"values ";
		sb.append(sql);
		int length=majorList.size();
		for(int i=0;i<length;i++){
			Major major =majorList.get(i);
			sb.append("(");
			sb.append("'"+major.getName()+"',");
			sb.append("'"+major.getCode()+"',");
			sb.append("'"+major.getDepartment()+"'");
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
	
	public List<Major> getList(){
		DatabaseAccess da=new DatabaseAccess();
		ResultSet rs= da.query("select * from Major ");
		List<Major> majorList=new ArrayList<Major>();
        try {
        	while (rs.next()) {
        		Major major=new Major();
        		 major.setName(rs.getString("name"));
        		 major.setCode(rs.getString("code"));    
        		 major.setDepartment(rs.getString("department"));    
        		 majorList.add(major);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        da.close();
        return majorList;
	}
}
