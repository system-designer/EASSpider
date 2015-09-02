package com.easspider.dao;

import java.util.List;

import com.easspider.bean.TimeTable;
import com.easspider.database.DatabaseAccess;

public class TimeTableDao {
	public void insert(List<TimeTable> timeTableList){
		StringBuilder sb=new StringBuilder();
		String sql="insert into student (schoolyear,schoolperiod,grade" +
				",depName,major,className,path) " +
				"values ";
		sb.append(sql);
		int length=timeTableList.size();
		for(int i=0;i<length;i++){
			TimeTable timeTable =timeTableList.get(i);
			sb.append("(");
			sb.append("'"+timeTable.getSchoolYear()+"',");
			sb.append("'"+timeTable.getSchoolPeriod()+"',");
			sb.append("'"+timeTable.getGrade()+"',");
			
			sb.append("'"+timeTable.getDepName()+"',");
			sb.append("'"+timeTable.getMajor()+"',");
			sb.append("'"+timeTable.getClassName()+"',");
			sb.append("'"+timeTable.getPath()+"',");
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
}
