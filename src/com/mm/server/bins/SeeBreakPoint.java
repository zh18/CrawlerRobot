package com.mm.server.bins;

import java.io.InputStream;
import java.io.PrintStream;

import com.mm.server.Bin;
import com.mm.stop.Protection;

public class SeeBreakPoint implements Bin {

	public String getName(){
		return "sbp : see break point ";
	}
	
	public void run(InputStream is, PrintStream os, String cmd) {
		String name = cmd.substring(cmd.lastIndexOf(" "),cmd.length()).trim();
		if(cmd.equals("")) {
			for(String s:Protection.seeAllBreakPoint())	os.println(s);
		}
		else {
			os.println(Protection.read(name));
		}
	}
	
}
