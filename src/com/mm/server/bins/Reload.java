package com.mm.server.bins;

import java.io.InputStream;
import java.io.PrintStream;

import com.mm.server.Bin;
import com.mm.server.StartServer;

public class Reload implements Bin {

	public String getName() {
		return "rl : reload the schemes , include commands";
	}

	public void run(InputStream is, PrintStream os, String cmd) {
		StartServer.registerBins();
		StartServer.loadScheme();
		os.println("reload success");
	}

}
