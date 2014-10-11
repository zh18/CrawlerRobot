package com.mm.server.bins;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;

import com.mm.server.Bin;
import com.mm.server.BinCaller;

public class Help implements Bin {

	public String getName() {
		return "hp : list our cmds ";
	}

	public void run(InputStream is, PrintStream os, String cmd) {
		for(Bin b:BinCaller.getAllBins()){
			os.println(b.getName());
		}
	}
}