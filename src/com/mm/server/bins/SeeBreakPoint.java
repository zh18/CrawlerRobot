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
		String name = null;
		try {
			name = cmd.substring(cmd.lastIndexOf("sbp")+3,cmd.length()).trim();
		}catch(Exception e){
			return ;
		}
		if(name.trim().equals("")) {
			os.println("    BREAK POINT NAMES    ");
			os.println("-------------------------");
			os.println(Protection.seeAllBreakPoint());
		}
		else {
			os.println();
			os.println(Protection.read(name));
		}
	}
	
}
