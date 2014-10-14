package com.mm.server.bins;

import java.io.InputStream;
import java.io.PrintStream;

import com.mm.core.Core;
import com.mm.server.Bin;

/**
 * 杀死task并把这个task移出队列
 * @author zh
 * @version 0.1
 * @since Oct 13,2014
 */
public class Kill implements Bin {

	public String getName() {
		return "kill : kill task";
	}

	public void run(InputStream is, PrintStream os, String cmd) {
		String name=cmd.substring(cmd.lastIndexOf(" ")).trim();
		if(name.trim().equals("")) {
			os.println("There doesn't have a task number");
		}
		if(!Core.stopThread(name) || !Core.removeThread(name))
			os.println("delete error");
	}
}
