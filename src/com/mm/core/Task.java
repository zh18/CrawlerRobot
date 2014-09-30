package com.mm.core;

import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import com.mm.datagetter.IDG;
import com.mm.db.DataBase;
import com.mm.logger.Log;
import com.mm.spider.ISpider;
import com.mm.spider.SpiderFactory;
import com.mm.spider.SpiderFactoryImpl;
import com.mm.stop.BreakPoint;
import com.mm.stop.Protection;
import com.mm.stop.ProtectionImpl;
import com.mm.util.SYS;
import com.mm.util.SystemUtil;

public class Task implements Runnable {

	private String id;
	private String running;
	private IDG gt;
	private ISpider spider = null;
	private static Protection protect;
	public static int TASK_ID = 0;
	public final static String DGCLASSPATH="com.mm.datagetter.scheme";
	public final static String INFOCLASSPATH="com.mm.infomarker.scheme";
	public final static String TASK_STOP="Stop",
							   TASK_RUNNING="Running",
							   TASK_YEILD="Yeild",
							   TASK_DONE="Done";
	
	/**
	 * use this var two to choose our task start from where
	 */
	private String part;
	private boolean frombp;
	
	/**
	 * 创建任务  初始化参数
	 * 输入taskname利用反射，到制定文件夹中找到制定的文件，然后进行加载，载入task
	 * @param taskname
	 */
	public Task(String c,boolean breakpoint){
		//加载gt
		Class<IDG> clazz = null;
		SpiderFactory spiderfactory = new SpiderFactoryImpl();
		//init protection 
		if (null == protect)
			protect = ProtectionImpl.getInstance();
		try {
			clazz = (Class<IDG>) Class.forName(Task.DGCLASSPATH+"."+c);  
			gt = (IDG) clazz.newInstance();
			//设置参数
			gt.init(SystemUtil.getSelectors(gt.getWname()), 
					spiderfactory, breakpoint?DataBase.getProtection(gt.getWname()):null);
			
			id = String.valueOf(TASK_ID++);   //获取id
			running = TASK_STOP;
		}catch(Exception e){
			Log.logger.error(clazz.getClass().getName()+" init ERROR",e);
		}
	}
	
	public void run(){
		try {
			/**
			 * Read breakpoint from database , and set breakpoint in gt
			 * And our bin caller will get the argument name part,it will set in this task class;
			 */
			running = TASK_RUNNING;
			gt.setBreakPoint(DataBase.getProtection(gt.getWname()));
			gt.go(part,frombp);
			running = TASK_DONE;
		}catch(Exception e){
			Log.logger.error(gt.loggerhelper("stoped"), e);
			running = TASK_STOP;
			Thread.interrupted();
			protection(e.getMessage());  //much better
		}
	}
	
	
	/**
	 * 启动本线程
	 */
	public void GOON(){
		notify();
		running = TASK_RUNNING;
		Log.logger.info(gt.loggerhelper("notifyed"));
	}

	
	public boolean stopTask(){
		if (running.equals(TASK_RUNNING)){
			running = TASK_STOP;
			Thread.interrupted();
			Log.logger.info(gt.loggerhelper("stopped"));
		}
		return true;
	}
	
	public boolean yeildTask(){
		if (running.equals(TASK_RUNNING)){
			running = TASK_YEILD;
			Thread.yield();
			Log.logger.info(gt.loggerhelper("yielded"));
		}
		return true;
	}
	/**       10       10               10         5      10
	 *   task_id | scheme_name  | process_name | rate | running
	 *   --------+--------------+--------------+------+----------
	 *         1 |   dangdang   |   get first  |  100%|  running  
	 */
	public String toString(){
		return String.format("%5s", id)+" | "+String.format("%6s", getName())+" | "+String.format("%15s",getPname())+" | "+
							String.format("%25s", getRate())+" | "+String.format("%10s", running);
	}
	
	
	public static void loadTask(String url){
		SystemUtil.loadClass(url+SYS.SYS_DG_SCHEME_FLODER, url, SYS.SYS_DG_SCHEME);
	}
	
	public void protection(String message){
		protect.save(new BreakPoint(message, new SimpleDateFormat("yyyy-MM-dd hh:mm").format(new Date()).toString(),
				gt.getWname(), gt.getPname(), gt.getRate(), gt.getDone()));
	}
	//##########################################
	public String getName() {
		return gt.getWname();
	}

	public String getId() {
		return id;
	}

	public String getPname() {
		return gt.getPname();
	}

	public String getRate() {
		return gt.getRateString();
	}

	public String getRunning() {
		return running;
	}

	public void setRunning(String running) {
		this.running = running;
	}

	public IDG getGt() {
		return gt;
	}

	public void setGt(IDG gt) {
		this.gt = gt;
	}
	

	public void setPart(String part){
		if(IDG.FIRST.indexOf(part) != -1)
			part = IDG.FIRST;
		else if (IDG.PRO.indexOf(part) != -1)
			part = IDG.PRO;
		else if (IDG.DOWN.indexOf(part) != -1)
			part = IDG.DOWN;
	}
	
	public void setFrombp(boolean bp){
		this.frombp = bp;
	}
	public String getPart(){
		return part;
	}
	public boolean getFrombp(){
		return frombp;
	}
	
	
}
