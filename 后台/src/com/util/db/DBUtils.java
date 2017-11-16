package com.util.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBUtils {
	public final static String URL = "jdbc:mysql://localhost:3306/bigtestweek";
	public final static String USERNAME = "root";
	public final static String PASSWORD = "1234";
	public final static String DRIVER = "com.mysql.jdbc.Driver";

	private DBUtils(){
		System.out.println("数据库");
	}
	static{
			try {
				Class.forName(DRIVER);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	public static Connection getConnection(){
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(URL,USERNAME,PASSWORD);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("连接成功");
		}
		return conn;
       }
	public static void close(ResultSet rs,Statement state,Connection conn){
		try {
			if(rs!=null) rs.close();
			if(state!=null) state.close();
			if(conn!=null) conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
    }  


