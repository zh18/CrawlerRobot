package com.mm.server.bins;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Properties;

import com.mm.logger.Log;
import com.mm.server.Bin;
import com.mm.util.SystemUtil;

public class SeeLog implements Bin {

	public String getName() {
		return "sl : see logs";
	}

	public void run(InputStream is, PrintStream os, String cmd) {
		String name = cmd.substring(cmd.indexOf(" "), cmd.length());
		if(name.equals("")) { 
			os.println("please input a days");
			return;
		}
		try {
			String path = SystemUtil.readProperties("log4j.properties", "log4j.appender.R.File");
			System.out.println(path);
			List<String> temp = SystemUtil.readLine(path);
			for(String s:temp){
				os.println(s);
			}
		}catch(Exception e){
			Log.logger.warn("Read properties error",e);
		}
	}
}
