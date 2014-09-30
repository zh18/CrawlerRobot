package com.mm.server.bin;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import com.mm.server.Bin;

public class FirstStep implements Bin{

	private String name = String.format("%15s", "-f ")+": begin first step";
	
	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public void run(InputStream is, OutputStream os, String cmd) {
		PrintStream ps = new PrintStream(os);
		if (cmd.indexOf("-h") != -1) {
			ps.println(getName());
		}
		else {
			String id = cmd.substring(cmd.indexOf("-f")+2, cmd.length());
			id = id.trim();
		}
	}
}
