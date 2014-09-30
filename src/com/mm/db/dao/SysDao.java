package com.mm.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.mm.db.ConnSQL;
import com.mm.db.pojo.Sys;
import com.mm.logger.Log;


public class SysDao {
	
	public static Sys getSys(){
		Connection conn = ConnSQL.getInstance("root", "root");
		PreparedStatement prep = null;
		try {
			prep = conn.prepareStatement("select * from sys where id=1");
			ResultSet rs = prep.executeQuery();
			if(rs.next()){
				return new Sys(rs.getInt("id"), rs.getLong("rotine"), rs.getLong("last"),rs.getDate("recent"), rs.getString("root"), 
						rs.getString("fname"), rs.getString("pname"), rs.getString("hfname"));
			}
		}catch(Exception e){
			Log.logger.warn("Sys data get error ",e);
		}
		return null;
	}
	
	
	public static String getString(String name){
		Connection conn = ConnSQL.getInstance("root", "root");
		PreparedStatement prep = null;
		try {
			prep = conn.prepareStatement("select "+name+" from sys where id=1");
			ResultSet rs = prep.executeQuery();
			if (rs.next()){
				return rs.getString(name);
			}
		}catch(Exception e){
			Log.logger.warn("Sys data get error ",e);
		}
		return null;
	}
	
}
