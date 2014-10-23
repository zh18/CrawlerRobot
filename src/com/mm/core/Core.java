package com.mm.core;

import java.io.File;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import com.mm.logger.Log;
import com.mm.stop.BreakPoint;
import com.mm.util.ReadSelector;
import com.mm.util.SYS;
import com.mm.util.SystemUtil;

/**
 * 任务调用的核心方法
 * 
 * @author zh
 * @version 0.1
 * @since Oct 14,2014
 */
public final class Core {

	private static List<Task> task=null;
	private static List<Thread> allthread=null;
	
	public static void init(){
		initTask();
		allthread = new ArrayList<Thread>();
	}
	
	public static void start(String id){
		Task temp = getTask(id);
		if(null == temp) return;
		Thread tmth = new Thread(temp);
		tmth.setName(temp.getId());
		allthread.add(tmth);
		tmth.start();
	}
	
	public static void startAll(){
		for(Task t:task){
			Thread temp = new Thread(t);
			temp.setName(t.getId());
			allthread.add(temp);
			temp.start();
		}
	}
	
	public final static boolean removeThread(String id){
		Iterator<Thread> it = allthread.iterator();
		//删除allthrea中的task
		if(getThread(id)!=null){
			SystemUtil.iteratorDelete(allthread, id, new Comparator() {
				public int compare(Object o1, Object o2) {
					if(o1 == o2) return 1;
					if(o1 instanceof Thread && o2 instanceof String){
						return ((Thread)o1).getName().equals(o2)?1:0;
					}
					return 0;
				}
			});
		}
		//删除task
		if(getTask(id) != null){
			SystemUtil.iteratorDelete(task, id, new Comparator() {
				public int compare(Object o1, Object o2) {
					if(o1 == o2) return 1;
					if(o1 instanceof Task && o2 instanceof String){
						return ((Task)o1).getId().equals(o2)?1:0;
					}
					return 0;
				}
			});
		}
		return true;
	}
	
	public final static boolean stopThread(String id){
		Task task = getTask(id);
		if (null == task) return false;
		Thread temp = getThread(id);
		//说明线程列表中没有此id的进程
		if (null == temp) return true;
		if(temp.isAlive()) {
			task.stop("Core stopped");
		}
		return true;
	}
	
	private static void initTask(){
		task = new ArrayList<Task>();
	}
	
	public static Task getTask(String id){
		for(Task t:task){
			if (t.getId().equals(id.trim())){
				return t;
			}
		}
		return null;
	}
	
	private static Thread getThread(String id){
		for(Thread t:allthread){
			if (t.getName().equals(id.trim()))
				return t;
		}
		return null;
	}


	public static List<String> getThreadIds(){
		List<String> result = new ArrayList<String>();
		for(Thread t:allthread){
			result.add(t.getName());
		}
		return result;
	}
	
	// ---------------  other command
	public final static void showList(OutputStream os){
		PrintStream ps = new PrintStream(os);
		if (task.size()==0) {
			ps.println("we don't have running task here ~   :-) \n");
			return ;
		}
		ps.println(String.format("%5s", "ID")+" | "+String.format("%30s", "name")+" | "+String.format("%15s","Process_name")+" | "+
				String.format("%25s", "Rate")+" | "+String.format("%10s", "Running"));
		ps.println("----------------------------------------------------------------------------------------");
		for(Task t:task){
			ps.println(t.toString());
		}
	}
	
	public final static void showReady(PrintStream os){
		File [] files = null;
		try {
			files = new File(SYS.PATH+SYS.SYS_DG_SCHEME).listFiles();
		}catch(Exception e){
			Log.logger.warn("show ready list error",e);
		}
		os.println("------ READY SCHEME ------");
		try {
			List<String> temp = ReadSelector.getAllNames();
			for(String s:temp){
				os.println(s);
			}
		}catch(Exception e){
			Log.logger.warn("load scheme error", e);
		}
	}
	
	public final static void add(String name,boolean bp){
		if(checkNames(name))
			task.add(new Task(name,bp));
	}
	
	public final static void add(String name,String pname,String rate){
		String iname = name+"."+pname;
		if(checkNames(name))
			task.add(new Task(iname,new BreakPoint(iname,pname,rate)));
	}
	
	public final static void addRefresh(String name,String part){
//		task.add(new Task())
	}
	
	public final static void addMul(String name,String pname,String rate,int nums){
		String iname = name+"."+pname;
		if(checkNames(name)){
			task.add(new Task(iname,new BreakPoint(iname,pname,rate),nums));
		}
	}
	
	private final static boolean checkNames(String name){
		List<String> temp = ReadSelector.getAllNames();
		try {
			if(name.indexOf("_") != -1 && name.indexOf(".") != -1)
				name = name.substring(0,name.indexOf(".")).trim();
		}catch(Exception e){
		}
		for(String s:temp){
			if(s.trim().equals(name.trim())){
				return true;
			}
		}
		return false;
	}
	
	
}
