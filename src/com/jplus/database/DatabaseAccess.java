package com.jplus.database;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 
 * @author evance
 */
public class DatabaseAccess {

	/**
	 * 数据库的驱动
	 */
	private static final String driver = "com.mysql.jdbc.Driver";
	private static final String url = "jdbc:mysql://localhost/hbnudata?useUnicode=true&characterEncoding=UTF-8";
	// * 数据库连接对象
	// */
	private Connection conn = null;
	/**
	 * 数据库操作对象
	 */
	private Statement stm = null;
	/**
	 * 数据库查询集合
	 */
	private ResultSet rs = null;

	/**
	 * 构造方法
	 */
	public DatabaseAccess() {
		try {
			Class.forName(driver);// 调用（初始化）数据库驱动
			conn = DriverManager.getConnection(url, "root", "root");// 创建连接
			stm = conn.createStatement();// 创建数据库操作对象
		} catch (Exception ex) {
			Logger.getLogger(DatabaseAccess.class.getName()).log(Level.SEVERE,
					null, ex);
		}
	}

	/**
	 * 获得查询结果集
	 * 
	 * @param sql
	 *            SQL语句
	 * @return 查询结果集
	 */
	public ResultSet query(String sql) {
		try {
			rs = stm.executeQuery(sql);
		} catch (SQLException ex) {
			Logger.getLogger(DatabaseAccess.class.getName()).log(Level.SEVERE,
					null, ex);
		}
		return rs;
	}

	public boolean update(String sql) {
		boolean b = false;
		try {
			stm.execute(sql);
			b = true;
		} catch (SQLException ex) {
			Logger.getLogger(DatabaseAccess.class.getName()).log(Level.SEVERE,
					null, ex);
		}
		return b;
	}

	public void close() {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException ex) {
				Logger.getLogger(DatabaseAccess.class.getName()).log(
						Level.SEVERE, null, ex);
			}
		}
		if (stm != null) {
			try {
				stm.close();
			} catch (SQLException ex) {
				Logger.getLogger(DatabaseAccess.class.getName()).log(
						Level.SEVERE, null, ex);
			}
		}
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException ex) {
				Logger.getLogger(DatabaseAccess.class.getName()).log(
						Level.SEVERE, null, ex);
			}
		}
	}
}
