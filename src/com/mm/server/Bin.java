package com.mm.server;

import java.io.InputStream;
import java.io.OutputStream;

public interface Bin {
	
	public void run(InputStream is,OutputStream os,String cmd);
	
	public String getName();
}