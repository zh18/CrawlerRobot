package com.mm.stop;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

import com.mm.db.DataBase;
import com.mm.db.dao.ProtectionDao;
import com.mm.logger.Log;

/**
 * {@link Protection} 默认实现类 每一个scheme都有一个root，把保护现场的文件写入root文件夹中
 * 待执行完毕，删除bk文件
 * @author public
 *
 */

public class ProtectionImpl implements Protection {
	
	private static Protection protection;
	
	private ProtectionImpl(){}
	
	public static Protection getInstance(){
		if (null == protection) {
			synchronized (ProtectionImpl.class) {
				protection = new ProtectionImpl();
			}
		}
		return protection;
	}
	
	
	@Override
	public void save(BreakPoint breakpoint){
		try {
			ProtectionDao.save(breakpoint);
		}catch(Exception e){
			Log.logger.error("break point file create error",e);
		}
	}
	
	@Override 
	public void delete(BreakPoint breakpoint){
		try {
			ProtectionDao.delete(breakpoint.getWname());
		}catch(Exception e){
			Log.logger.warn("delete bk error", e);
		}
	}
	
	@Override
	public BreakPoint recover(String name){
		try {
			return ProtectionDao.readBreak(name);
		}catch(Exception e){
			Log.logger.error("scheme read protection "+name+" failed", e);
		}
		return null;
	}
}
