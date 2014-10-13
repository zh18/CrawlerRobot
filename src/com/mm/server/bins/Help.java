package com.mm.server.bins;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;

import com.mm.server.Bin;
import com.mm.server.BinCaller;

/**
 * 显示帮助
 * @author zh
 * @version 0.1
 * @since Oct 13,2014
 */
public class Help implements Bin {

	public String getName() {
		return "help : list our cmds ";
	}

	public void run(InputStream is, PrintStream os, String cmd) {
		for(Bin b:BinCaller.getAllBins()){
			os.println(b.getName());
		}
	}
}