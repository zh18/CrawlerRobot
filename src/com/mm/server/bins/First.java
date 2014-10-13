package com.mm.server.bins;

import java.io.InputStream;
import java.io.PrintStream;

import com.mm.server.Bin;

public class First implements Bin {

	public String getName() {
		return "first : series cmd of data getter one step - get categrey url";
	}

	@Override
	public void run(InputStream is, PrintStream os, String cmd) {
		// TODO Auto-generated method stub

	}

}
