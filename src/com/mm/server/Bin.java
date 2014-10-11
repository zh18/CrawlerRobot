package com.mm.server;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;

public interface Bin {
	
	public String getName();
	
	public void run(InputStream is,PrintStream os,String cmd);
}
