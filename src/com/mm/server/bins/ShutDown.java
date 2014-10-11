package com.mm.server.bins;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;

import com.mm.core.Core;
import com.mm.server.Bin;
import com.mm.server.StartServer;
import com.mm.util.Times;

public class ShutDown implements Bin {

	public String getName() {
		return "sd : shutdown";
	}

	public void run(InputStream is, PrintStream os, String cmd) {
		os.println("Server is closing ...");
		for(String s:Core.getThreadIds()){
			Core.stopThread(s);
			os.println("saved "+s);
		}
		os.println("server had run "+Times.getTimes(StartServer.times, System.currentTimeMillis()));
		System.exit(0);
	}

}
