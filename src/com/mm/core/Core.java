/**
 * 本类是爬虫机器人所有自动控制的逻辑，自动调控的中心
 * 
 * @author zh
 * @version 0.1
 */
package com.mm.core;

import java.io.File;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import com.mm.datagetter.IDG;
import com.mm.logger.Log;
import com.mm.util.SYS;
import com.mm.util.SystemUtil;

public class Core {
	//dependency inject
	private int cycle;   //min
	private Date laststart;
	private long runtime;
	private static List<String> alltask = null;
	/**
	 * task 任务列表。里面存储着任务，每一个任务都是一个线程，
	 * 任务抛出异常，捕获异常后，stopper存储异常原因与断点
	 * 该thread yeild等待修补后，notify该thread
	 */
	private static Set<Task> taskset;
	
	public static final String STOP = "Core stop";
	public static final String YEILD = "Core yeild";
	
	public static void startAll(){
		for(Task t:taskset){
			if (!t.getRunning().equals(Task.TASK_RUNNING)) {
				Thread temp = new Thread(t);
				temp.setName(t.getId());
				temp.start();
				
			}
		}
	}
	
	public static boolean start(String wname){
		for(Task t:taskset){
			if (t.getName().equals(wname) && (t.getRunning().equals(Task.TASK_STOP))) { 
				Thread temp = new Thread(t);
				temp.setName(t.getId());
				temp.start();
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 启动一些线程
	 * 1) 读取操作参数
	 * 2) init datagetter init infomatter
	 * 2）读取未完成任务，并开始他们   dbreader?
	 * 3) dg set SpiderFactory
	 */
	public static void init(){
		taskset = new HashSet<Task>();
		String url = "/home/public/javaproject/CrawlerRobot/bin/";
		Task.loadTask(url);
		alltask = getSchemes(url);
	}
	
	private static List getSchemes(String url){
		File file [] = new File(url+SYS.SYS_DG_SCHEME_FLODER).listFiles();
		List<String> result = new ArrayList<String>();
		for(File f:file){
			if (f.getName().indexOf(".class") != -1)
				result.add(f.getName().substring(0, f.getName().indexOf(".")));
		}
		return result;
	}
	
	
	/**
	 * 输出所有的任务列表
	 * task_id | scheme_name  | process_name | rate | running
	 */
	public static void showList(OutputStream os){
		PrintStream ps = new PrintStream(os);
		if (taskset.size()==0) {
			ps.println("we don't have running task here ~   :-) \n"+
										  "please use 'task -a' or '-tl -a' see all ready tasks \n"+
										  "and task -new scheme name to insert into taskflow ");
			return ;
		}
		ps.println(String.format("%5s", "ID")+" | "+String.format("%6s", "name")+" | "+String.format("%15s","Process_name")+" | "+
				String.format("%25s", "Rate")+" | "+String.format("%10s", "Running"));
		ps.println("--------------------------------------------------------------------------");
		for(Task t:taskset){
			ps.println(t.toString());
		}
	}
	
	public static boolean stopTask(String id){
		boolean result = false;
		for(Task t:taskset){
			System.out.println(t.getId());
			System.out.println(id);
			if (String.valueOf(t.getId()).equals(id)) {
				/**
				 * 处理异步的问题
				 */
				synchronized(t){
					t.stopTask();
					t.setRunning(Task.TASK_STOP);
					t.protection(STOP);
					result = true;
				}
			}
		}
		return result;
	}
	
	public static void yeildTask(String id){
		for(Task t:taskset){
			if (t.getId().equals(id)){
				synchronized (t) {
					t.yeildTask();
					t.setRunning(Task.TASK_YEILD);
					t.protection(YEILD);
				}
			}
		}
	}
	

	public static boolean addTask(Task task){
		
		taskset.add(task);
//		start();
		return true;
	}
	
	
	/**
	 * 启动该线程
	 * @param id
	 * @return
	 */
	public static boolean GOON(String id){
		for(Task t:taskset){
			if (t.getId().equals(id)){
				t.GOON();
				return true;
			}
		}
		return false;
	}
	
	
	/**
	 * 
	 * Remove all running = false 's thread
	 * 
	 * @return <tt>true</tt> success
	 * @return <tt>false</tt> flase
	 */
	public static boolean removeTask(){
		Iterator<Task> it = taskset.iterator();  
        while(it.hasNext()){  
            Task temp = it.next();  
            if(!temp.getRunning().equals(Task.TASK_STOP)){  
                it.remove();  
                temp.protection(STOP);
            }  
        }  
		return true;
	}
	
	public static boolean removeTask(String id){
		id = id.trim();
		Iterator<Task> it = taskset.iterator(); 
		while(it.hasNext()){  
            Task temp = it.next();  
            if(temp.getRunning().equals(Task.TASK_STOP) && temp.getId().equals(id)){  
                it.remove();  
                temp.protection(STOP);
            }  
        }  
		return true;
	}
	
	public static int taskSize(){
		return taskset.size();
	}

	public static int runningTaskSize(){
		int i = 0;
		for(Task t:taskset){
			if(t.getRunning().equals(Task.TASK_RUNNING)) ++i;
		}
		return i;
	}
	
	//#########################################
	public int getCycle() {
		return cycle;
	}

	public void setCycle(int cycle) {
		this.cycle = cycle;
	}

	public Date getLaststart() {
		return laststart;
	}

	public void setLaststart(Date laststart) {
		this.laststart = laststart;
	}

	public long getRuntime() {
		return runtime;
	}

	public void setRuntime(long runtime) {
		this.runtime = runtime;
	}
	
	public static List<String> getAllTaskWeHave(){
		if (null == alltask || alltask.size() == 0)
			return null;
		return alltask;
	}
	
	public static String getTaskName(String id){
		for(Task t:taskset){
			if (t.getId().equals(id)){
				return t.getName();
			}
		}
		return "";
	}
}