package com.mm.server.bins;

import java.io.InputStream;
import java.io.PrintStream;

import com.mm.server.Bin;

public class Pipe implements Bin {

	public String getName() {
		return "pp : here is a pipe , so you can add task into this pipe";
	}

	public void run(InputStream is, PrintStream os, String cmd) {
		// TODO Auto-generated method stub

	}

}