package com.mm.server.bins;

import java.io.InputStream;
import java.io.PrintStream;

import com.mm.core.Core;
import com.mm.core.Task;
import com.mm.data.Idata;
import com.mm.server.Bin;

/**
 * 直接开始info环节 当然首先会检查html中是否含有文件
 * 
 * @author zh
 * @version 0.1
 * @since Oct 13,2014
 */
public class Info implements Bin {

	public String getName() {
		return "info : get info.txt from htmls";
	}

	public void run(InputStream is, PrintStream os, String cmd) {
		String name = null;
		String rate = null;
		try {
			name = cmd.substring(cmd.indexOf("-n") + 2, cmd.indexOf("-r"))
					.trim();
			rate = cmd.substring(cmd.indexOf("-r") + 2).trim();
		} catch (Exception e) {
			name = cmd.substring(cmd.indexOf("-n")).trim();
		}
		if(!Task.fileCheck(name, Idata.INFO)) {
			os.print("There is no htmls files");
			return;
		}
		if (null == rate)
			rate = "0";
		Core.add(name, Idata.INFO, rate);
	}
}
