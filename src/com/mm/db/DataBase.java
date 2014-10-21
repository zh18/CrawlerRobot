package com.mm.db;

import java.util.List;

import com.mm.db.dao.ProtectionDao;
import com.mm.db.dao.SysDao;
import com.mm.db.dao.UserDao;
import com.mm.stop.BreakPoint;

public class DataBase {
	
	public static boolean login(String username,String pwd){
		return UserDao.login(username, pwd);
	}
	
	
	
	public static void saveProtection(BreakPoint breakpoint){
		ProtectionDao.save(breakpoint);
	}
	
	public static BreakPoint getProtection(String name){
		return ProtectionDao.readBreak(name);
	}
	
	public static List<BreakPoint> seeAll(){
		return ProtectionDao.seeAll();
	}
	
	public static void deleteProtection(String name){
		ProtectionDao.delete(name);
	}
	
	
	//----------------  path uname fname htname;
	
	public static String getString(String name){
		return SysDao.getString(name);
	}
	
}
