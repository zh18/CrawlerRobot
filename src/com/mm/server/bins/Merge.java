package com.mm.server.bins;

import java.io.InputStream;
import java.io.PrintStream;

import com.mm.logger.Log;
import com.mm.server.Bin;
import com.mm.util.SystemUtil;

public class Merge implements Bin {

	@Override
	public String getName() {
		return "mg : merge to folders";
	}

	public void run(InputStream is, PrintStream os, String cmd) {
		String temp = cmd;
		try {
			temp = temp.substring(temp.indexOf("mg")+2).trim();
			String lines[] = temp.split(" ");
			if(lines.length==0 || lines.length==1) return;
			//src //dest
			SystemUtil.merge(lines[0], lines[1]);
		}catch(Exception e){
			Log.logger.error("merge error", e);
		}
	}

	
		
}
