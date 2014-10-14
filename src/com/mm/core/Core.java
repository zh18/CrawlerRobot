package com.mm.core;

import java.io.File;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.mm.logger.Log;
import com.mm.util.ReadSelector;
import com.mm.util.SYS;

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
		while(it.hasNext()){
			Thread temp = it.next();
			if (temp.isAlive()){
				temp.stop();
			}
			allthread.remove(Integer.parseInt(id));
			return true;
		}
		return false;
	}
	
	public final static boolean stopThread(String id){
		Thread temp = getThread(id);
		if (null == temp) return false;
		Task task = getTask(id);
		if (null == task) return false;
		task.stop("Core stopped");
		temp.stop();
		return true;
	}
	
	private static void initTask(){
		task = new ArrayList<Task>();
//		for(String s:Task.getSchemeName()){
//			task.add(new Task(s, false));
//		}
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
		ps.println(String.format("%5s", "ID")+" | "+String.format("%15s", "name")+" | "+String.format("%15s","Process_name")+" | "+
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
		task.add(new Task(name,bp));
	}
}
