package com.mm.stop;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import com.mm.core.Core;
import com.mm.data.Idata;
import com.mm.db.DataBase;
import com.mm.db.dao.ProtectionDao;
import com.mm.logger.Log;

/**
 * {@link Protection} 默认实现类 每一个scheme都有一个root，把保护现场的文件写入root文件夹中
 * 待执行完毕，删除bk文件
 * @author public
 *
 */

public class Protection {
	
	private static Protection protection;
	private Protection(){}
	
	public static Protection getInstance(){
		if (null == protection) {
			synchronized (Protection.class) {
				protection = new Protection();
			}
		}
		return protection;
	}
	
	public static BreakPoint read(String name){
		return protection.read(name);
	}
	
	public static void save(BreakPoint breakpoint){
		try {
			ProtectionDao.save(breakpoint);
		}catch(Exception e){
			Log.logger.error("break point file create error",e);
		}
	}
	
	public static void save(Idata data,String message){
		save(new BreakPoint(message, new SimpleDateFormat("yyyy-MM-dd hh:mm").format(new Date()).toString(),
				data.getBreakPoint().getWname(), data.getBreakPoint().getPname(), data.getBreakPoint().getRate()));;
	}
	
	
	public static void delete(BreakPoint breakpoint){
		try {
			ProtectionDao.delete(breakpoint.getWname());
		}catch(Exception e){
			Log.logger.warn("delete bk error", e);
		}
	}
	
	public static List<String> seeAllBreakPoint(){
		List<String> result = new ArrayList<String>();
		for(BreakPoint bp:ProtectionDao.seeAll()){
			result.add(bp.toString());
		}
		return result;
	}
	
	public static BreakPoint recover(String name){
		try {
			return ProtectionDao.readBreak(name);
		}catch(Exception e){
			Log.logger.error("scheme read protection "+name+" failed", e);
		}
		return null;
	}
}
