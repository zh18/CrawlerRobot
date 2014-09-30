/**
 * 读取数据库中的用户名和密码
 * 
 * @author zh
 * @version 0.1
 */
package com.mm.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.mm.db.ConnSQL;
import com.mm.logger.Log;

public class UserDao {
	public static boolean login(String username,String pwd){
		Connection conn = ConnSQL.getInstance("root", "root");
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement("select * from user where user=? and pwd=?");
			stat.setString(1, username);
			stat.setString(2, pwd);
			if(stat.executeQuery().next()){
				return true;
			}
		}catch(SQLException e){
			Log.logger.warn("check login error", e);
		}
		return false;
	}
	
	public static boolean addUesr(String name,String pwd){
		return false;
	}
	
	public static boolean removeUser(String name) {
		return false;
	}

}