package com.mm.server.bin;

import java.io.InputStream;
import java.io.OutputStream;

import com.mm.server.Bin;

public class Quit implements Bin{

	@Override
	public String getName() {
		return  String.format("%15s","quit -q")+" : log out our system";
	}
	@Override
	public void run(InputStream is, OutputStream os, String cmd) {
		// TODO Auto-generated method stub
		
	}
}
