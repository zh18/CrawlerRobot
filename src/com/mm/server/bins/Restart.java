package com.mm.server.bins;

import java.io.InputStream;
import java.io.PrintStream;

import com.mm.server.Bin;
import com.mm.server.StartServer;

public class Restart implements Bin {

	public String getName() {
		return "re : restart";
	}

	public void run(InputStream is, PrintStream os, String cmd) {
		os.println("we are restarting");
		StartServer.reboot();
		os.println("reboot ok!");
	}

}
