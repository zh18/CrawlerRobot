/**
 * @author public
 *
 */
package com.mm.core;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.mm.data.Idata;
import com.mm.data.multithreading.MulDown;
import com.mm.data.struct.Selector;
import com.mm.db.DataBase;
import com.mm.logger.Log;
import com.mm.spider.SpiderFactory;
import com.mm.spider.SpiderFactoryImpl;
import com.mm.stop.BreakPoint;
import com.mm.stop.Protection;
import com.mm.util.ReadSelector;
import com.mm.util.SYS;
import com.mm.util.SystemUtil;

public final class Task implements Runnable {
	
	public final static String START = "never_run",
							   STOP = "stop",
							   RUNNING = "running";
	
//	protected final static String fname = DataBase.getString("fname"),
//			  uname = DataBase.getString("uname"),
//			  hfname = DataBase.getString("hname"),
//			  tname = DataBase.getString("tname"),
//			  iname = DataBase.getString("iname"),
//			  mark = "ကကက";
	protected final static String fname = "first.txt",
	  uname = "url.txt",
	  hfname = "html",
	  tname = "type.txt",
	  iname = "info.txt",
	  mark = "ကကက";
	
	
	private String id;
	private static int TASK_ID = 0;
	private String name;
	private SpiderFactory factory;
	
	private Idata data;
	
	
	private String running;
	
	/**
	 * 
	 * 直接读取数据库中的断点。
	 * 
	 * @param name DD_ghhz
	 * @param bp
	 */				
	public Task(String name,boolean bp){
		// 设置id
		id = String.valueOf(TASK_ID++);
		// 设置名字
		this.name = name;
		// 设置爬虫工厂
		factory = new SpiderFactoryImpl();
		// 实例化data
		Class clazz = null;
		try {
			clazz = Class.forName(SYS.SYS_DG_SCHEME+"."+name.split("_")[0].trim());
			data = (Idata)clazz.newInstance();
			// 设置名字
			data.setName(name);
		} catch(ClassNotFoundException e){
			Log.logger.error("Data class not found error",e);
		} catch (Exception e) {
			Log.logger.error("Task init error",e);
		} 
		// 设置断点信息
		data.setBreakPoint(bp?DataBase.getProtection(name):null);
		// 设置蜘蛛   data下载时候的data由task设置
		data.setFactory(factory);
		// 设置选择器 selector
		data.setSelector(ReadSelector.getSelector(name));
		// 设置初始状态
		running = START;
	}
	
	/**
	 * 重载的构造方法，对于人工建立的步骤其作用
	 * @param name scheme_name
	 * @param breakpoint 断点
	 */
	public Task(String name,BreakPoint breakpoint){
		// 设置id
				id = String.valueOf(TASK_ID++);
				// 设置名字
				this.name = name;
				// 设置爬虫工厂
				factory = new SpiderFactoryImpl();
				// 实例化data
				Class clazz = null;
				try {
					clazz = Class.forName(SYS.SYS_DG_SCHEME+"."+name.split("_")[0].trim());
					data = (Idata)clazz.newInstance();
					// 设置名字
					data.setName(name);
				} catch(ClassNotFoundException e){
					Log.logger.error("Data class not found error",e);
				} catch (Exception e) {
					Log.logger.error("Task init error",e);
				} 
				// 设置断点信息
				data.setBreakPoint(breakpoint);
				// 设置蜘蛛   data下载时候的data由task设置
				data.setFactory(factory);
				// 设置选择器 selector
				data.setSelector(ReadSelector.getSelector(name));
				// 设置初始状态
				running = START;
	}
	
	public Task(String name,BreakPoint breakpoint,boolean mul){
		// 设置id
				id = String.valueOf(TASK_ID++);
				// 设置名字
				this.name = name;
				// 设置爬虫工厂
				factory = new SpiderFactoryImpl();
				// 实例化data
				Class clazz = null;
//				try {
//					clazz = Class.forName(SYS.SYS_DG_SCHEME+"."+name.split("_")[0].trim());
//					data = (Idata)clazz.newInstance();
					data = new MulDown();   // 只适用于当当
					// 设置名字
					data.setName(name);
//				} catch(ClassNotFoundException e){
//					Log.logger.error("Data class not found error",e);
//				} catch (Exception e) {
//					Log.logger.error("Task init error",e);
//				} 
				// 设置断点信息
				data.setBreakPoint(breakpoint);
				// 设置蜘蛛   data下载时候的data由task设置
				data.setFactory(factory);
				// 设置选择器 selector
				data.setSelector(ReadSelector.getSelector(name));
				// 设置初始状态
				running = START;
	}
	
	public void run(){
		try {
			running = RUNNING;
			data.data();
			running = STOP;
		}catch(Exception e){
			running = STOP;
			Protection.save(data, "Oops");
			Log.logger.error("get data error", e);
		}
	}
	
	
	public void stop(String message){
		Protection.save(data, message);
	}
	
	
	public String toString(){
		return String.format("%5s", id)+" | "+String.format("%30s", data.getBreakPoint().getWname())+" | "+String.format("%15s",data.getBreakPoint().getPname())+" | "+
				String.format("%25s", getRateString())+" | "+String.format("%10s", running);
	}
	
	public final String getRateString(){
		return String.format("%.2f", (Integer.parseInt(data.getBreakPoint().getRate()) * 100 * 1.0 / data.getBreakPoint().getTotal()))+"% "+"N@"+
				data.getBreakPoint().getRate()+"/T@"+data.getBreakPoint().getTotal();
	}
	
	public String getName(){
		return name;
	}
	
	public String getProcess(){
		return data.getBreakPoint().getPname();
	}
	
	public String getId(){
		return id;
	}
	
	public static List<String> getSchemeName(){
		File [] files = new File(DataBase.getString("syspath")+SYS.SYS_DG_SCHEME_FLODER).listFiles();
		List<String> result = new ArrayList<String>();
		for(File f:files){
			if (f.getName().indexOf("impl") == -1 && f.getName().indexOf(".class") != -1) {
				result.add(f.getName().substring(0, f.getName().indexOf(".")));
			}
		}
		return result;
	}
	
	public static boolean fileCheck(String name,String process){
		Selector s = ReadSelector.getSelector(name);
		if(process.equals(Idata.FIRST)) return true;
		else if (process.equals(Idata.PRODUCT)) return SystemUtil.filecheck(s.getSavepath()+fname);
		else if (process.equals(Idata.DOWNLOAD)) return SystemUtil.filecheck(s.getSavepath()+uname);
		else if (process.equals(Idata.INFO)) return SystemUtil.filecheck(s.getSavepath()+hfname+File.separator+"0~10000");
		else return false;
	}
	
	public void setSpiderFactory(SpiderFactory factory){
		this.factory = factory;
	}
	
}