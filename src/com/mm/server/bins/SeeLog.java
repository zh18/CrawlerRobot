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
		String name = cmd.substring(cmd.indexOf("sl")+2, cmd.length());
		if(name.trim().equals("")) { 
			os.println("please input a days");
			return;
		}
		try {
			String path = SystemUtil.readProperties("log4j.properties", "log4j.appender.R.File");
			File [] files = new File(new File(path).getParent()).listFiles();
			if(name.trim().equals("-s")) {
				for(File f:files){
					if(f.getName().startsWith("log")) {
						os.println(f.getName());
					}
				}
				return;
			}
			for(File s:files){
				System.out.println(s.getName()+" "+name.trim());
				if(s.getName().indexOf(name) != -1){
					os.println(s.getName());
					os.println("---------------------");
					for(String temp:SystemUtil.readLine(s.getAbsolutePath())) os.println(temp);
				}
			}
		}catch(Exception e){
			Log.logger.warn("Read properties error",e);
		}
	}
}
