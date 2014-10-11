/**
 * @author public
 *
 */
package com.mm.core;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.mm.data.Idata;
import com.mm.db.DataBase;
import com.mm.logger.Log;
import com.mm.spider.SpiderFactory;
import com.mm.spider.SpiderFactoryImpl;
import com.mm.stop.Protection;
import com.mm.util.ReadSelector;
import com.mm.util.SYS;

public final class Task implements Runnable {
	
	public final static String START = "never_run",
							   STOP = "stop",
							   RUNNING = "running";
	
	private String id;
	private static int TASK_ID = 0;
	private String name;
	private SpiderFactory factory;
	
	private Idata data;
	
	
	private String running;
	
	/**
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
		return String.format("%5s", id)+" | "+String.format("%15s", data.getBreakPoint().getWname())+" | "+String.format("%15s",data.getBreakPoint().getPname())+" | "+
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
	
	
	public void setSpiderFactory(SpiderFactory factory){
		this.factory = factory;
	}
	
}