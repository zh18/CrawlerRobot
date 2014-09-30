package com.mm.server.bin;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import com.mm.server.Bin;
import com.mm.server.BinCaller;

public class Help implements Bin{

	private String name = String.format("%15s","help -h")+" : help you know what cmd did we have ";
	
	public String getName() {
		return name;
	}
	
	@Override
	public void run(InputStream is, OutputStream os, String cmd) {
		PrintStream ps = new PrintStream(os);
		for(Bin b:BinCaller.getBinList()){
			ps.println(b.getName());
		}
	}
}
