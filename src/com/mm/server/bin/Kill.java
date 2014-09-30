package com.mm.server.bin;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import com.mm.core.Core;
import com.mm.core.Task;
import com.mm.logger.Log;
import com.mm.server.Bin;

public class Kill implements Bin{

	
	// little stupid
	private String name = String.format("%15s","kill ")+" : kill scheme | -y yeild | -k stop | -rm remove | + task_id\n";
	
	@Override
	public void run(InputStream is, OutputStream os, String cmd) {
		PrintStream ps = new PrintStream(os);
		if (cmd.indexOf("-y") != -1) {
			try {
				String id = cmd.substring(cmd.indexOf("-y")+2, cmd.length());
				id = id.trim();
				Core.yeildTask(id);
				ps.println("We had yeild the running task id "+id+" ,named "+Core.getTaskName(id));
			}catch(Exception e){
				ps.println("Please input a running task number so that we can yeild");
			}
		}
		else if (cmd.indexOf("-k") != -1){
			try {
				String id = cmd.substring(cmd.indexOf("-k")+2, cmd.length());
				id = id.trim();
				if (Core.stopTask(id)) {
					ps.println("We had stop the running task id "+id+" ,named "+Core.getTaskName(id));
				}
				else  {
					ps.println("the running task stop error");
					throw new RuntimeException();
				}
			}catch(Exception e){
				ps.println("Please input a running task number so that we can stop");
			}
		}
		else if (cmd.indexOf("-rm") != -1){
			try {
				
				String id = cmd.substring(cmd.indexOf("-rm")+3, cmd.length());
				id = id.trim();
				if (Core.removeTask(id.trim())) {
					ps.println("We had remove the running task id "+id+" ,named "+Core.getTaskName(id));
				}
				else  {
					ps.println("the running task remove error");
					throw new RuntimeException();
				}
			}catch(Exception e){
				ps.println("Please input a running task number so that we can remove");
			}
		}
		else if (cmd.indexOf("-h") != -1) {
			ps.println(getName());
		}
	}

	@Override
	public String getName() {
		return name;
	}

}
