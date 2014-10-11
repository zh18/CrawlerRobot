package com.mm.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.mm.db.ConnSQL;
import com.mm.logger.Log;
import com.mm.stop.BreakPoint;


public class ProtectionDao {
	
	public static BreakPoint readBreak(String schemename){
		Connection conn = ConnSQL.getInstance("root", "root");
		PreparedStatement prep = null;
		try {
			prep = conn.prepareStatement("select * from bp where wname=?");
			prep.setString(1, schemename);
			ResultSet rs = prep.executeQuery();
			if (rs.next()){
				return new BreakPoint(rs.getString("breakreason"), rs.getString("time"), rs.getString("wname"), 
						rs.getString("pname"), rs.getString("rate"));
			}
		}catch(Exception e){
			Log.logger.warn("get break point error", e);
			return null;
		}
		return null;
	}
	
	public static void save(BreakPoint breakpoint){
		Connection conn = ConnSQL.getInstance("root", "root");
		PreparedStatement prep = null;
		try {
			if (check(breakpoint.getWname())){
				update(breakpoint);
			}
			else {
				prep = conn.prepareStatement("insert into bp (breakreason,time,wname,pname,rate) values (?,?,?,?,?)");
				prep.setString(1, breakpoint.getBreakreason());
				prep.setString(2, breakpoint.getTime());
				prep.setString(3, breakpoint.getWname());
				prep.setString(4, breakpoint.getPname());
				prep.setString(5, breakpoint.getRate());
				prep.executeUpdate();
			}
		}catch(Exception e){
			Log.logger.warn("save break point error", e);
		}
	}
	
	public static void delete(String schemname){
		Connection conn = ConnSQL.getInstance("root", "root");
		PreparedStatement prep = null;
		try {
			prep = conn.prepareStatement("delete from bp where wname=?");
			prep.setString(1,schemname);
			prep.executeUpdate();
		}catch(Exception e){
			Log.logger.warn("save break point error", e);
		}
	}
	
	public static void update(BreakPoint breakpoint){
		Connection conn = ConnSQL.getInstance("root", "root");
		PreparedStatement prep = null;
		try {
			prep = conn.prepareStatement("update bp set breakreason=?,time=?,pname=?,rate=?,done=? where wname=?");
			prep.setString(1, breakpoint.getBreakreason());
			prep.setString(2, breakpoint.getTime());
			prep.setString(3, breakpoint.getPname());
			prep.setString(4, breakpoint.getRate());
			prep.setString(5, breakpoint.getDone());
			prep.setString(6, breakpoint.getWname());
			prep.executeUpdate();
		}catch(Exception e){
			Log.logger.warn("save break point error", e);
		}
	}
	
	public static boolean check(String name){
		Connection conn = ConnSQL.getInstance("root", "root");
		PreparedStatement prep = null;
		try {
			prep = conn.prepareStatement("select * from bp where wname=?");
			prep.setString(1, name);
			if(prep.executeQuery().next()){
				return true;
			}
		}catch(Exception e){
			Log.logger.warn("save break point error", e);
		}
		return false;
	}
	
}
