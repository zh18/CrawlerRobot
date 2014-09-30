package com.mm.db;

import java.sql.Connection;
import java.sql.DriverManager;

import com.mm.logger.Log;

/**
 * 获取sql数据库的链接(直接mysql实现)
 * 
 * @author zh
 * @version 0.1
 */

public class ConnSQL {

	private static Connection conn;
	
	private ConnSQL(){}
	
	public static Connection getInstance(String name,String pwd){
		if (null == conn) {
			synchronized (ConnSQL.class) {
				try {
					Class.forName("com.mysql.jdbc.Driver");
					conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/cr",name,pwd);
				}catch(Exception e){
					Log.logger.error("db error",e);
				}
			}
		}
		return conn;
	}
}
