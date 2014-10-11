package com.mm.server.bins;

import java.io.InputStream;
import java.io.PrintStream;

import com.mm.core.Core;
import com.mm.server.Bin;

public class Kill implements Bin {

	public String getName() {
		return "kill : kill task";
	}

	public void run(InputStream is, PrintStream os, String cmd) {
		String name=cmd.substring(cmd.lastIndexOf(" "));
		if(name.trim().equals("")) {
			os.println("There doesn't have a task number");
		}
		if(Core.stopThread(name))
			os.println("delete success");
		else
			os.println("delete error");
	}

}
