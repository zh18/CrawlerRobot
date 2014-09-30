package com.mm.server.bin;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import com.mm.core.Core;
import com.mm.server.Bin;

public class InitCore implements Bin{

	private String name=String.format("%15s", "ic -ic")+" : init core or reinit core";
	
	@Override
	public void run(InputStream is, OutputStream os, String cmd) {
		PrintStream ps = new PrintStream(os);
		if (cmd.indexOf("-h") != -1) {
			ps.println(getName());
			return;
		}
		Core.init();
		ps.println(" Init success ! :-) ");

	}

	@Override
	public String getName() {
		return name;
	}

	
	
}
