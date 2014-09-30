package com.mm.server.bin;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Scanner;
import java.util.List;

import com.mm.core.Core;
import com.mm.core.Task;
import com.mm.datagetter.IDG;
import com.mm.logger.Log;
import com.mm.server.Bin;

public class TaskList implements Bin{
	
	private String name = String.format("%15s","task -tl")+" : show task list "
			+ "-a show ready task list,-new add a new task in tasklist,-start task_id";
	
	public String getName(){
		return name;
	}

	@Override
	public void run(InputStream is, OutputStream os, String cmd) {
		PrintStream ps = new PrintStream(os);
		if (cmd.trim().equals("-tl"))
			Core.showList(os);
		else if (cmd.trim().indexOf("-a") != -1) { 
			List<String> temp = Core.getAllTaskWeHave();
			if (temp == null) {
				ps.println("we don't have one task , please add a new one");
				return ;
			}
			ps.println("-- TASK LIST --");
			for(String s:temp)
				ps.println(s);
		}
		else if (cmd.trim().indexOf("-new") != -1){
			String name = cmd.substring(cmd.indexOf("-new")+4, cmd.length()).trim();
			ps.print(" start from breakpoint y/n ? default yes  : ");
			String temp = new Scanner(is).nextLine();
			if (temp.indexOf("y") != -1 || "".equals(temp)) 
				Core.addTask(new Task(name,true));
			else 
				Core.addTask(new Task(name,false));
		}
		else if (cmd.trim().indexOf("-start") != -1){
			try {
				cmd = cmd.substring(cmd.indexOf("-start")+6, cmd.length()).trim();
				if(!Core.start(cmd)) 
					ps.println("wrong scheme name");
			}catch(Exception e){
				Core.startAll();
			}
		}
		else if (cmd.trim().indexOf("-g") != -1){
			try {
				cmd = cmd.substring(cmd.indexOf("-g")+2).trim();
				String id = cmd.trim();
				if (Core.GOON(id)){
					ps.println("Thread "+id+" was goon");
				}
			}catch(Exception e){
				Log.logger.error("thread notify  error ", e);
			}
		}
		else if (cmd.indexOf("-h") != -1) {
			ps.println(getName());
		}
	}
}
