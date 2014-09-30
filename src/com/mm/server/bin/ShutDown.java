package com.mm.server.bin;

import java.io.InputStream;
import java.io.OutputStream;

import com.mm.server.Bin;

public class ShutDown implements Bin{
	
	private String name = String.format("%15S", "SHUTDONW -sd ")+": shutdown server";
	
	public String getName(){
		return name;
	}
	
	@Override
	public void run(InputStream is, OutputStream os, String cmd) {
		System.exit(0);
	}
}
