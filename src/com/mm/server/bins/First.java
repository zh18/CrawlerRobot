package com.mm.server.bins;

import java.io.InputStream;
import java.io.PrintStream;

import com.mm.core.Core;
import com.mm.core.Task;
import com.mm.data.Idata;
import com.mm.server.Bin;

/**
 * 直接开始下载product url的环节
 * 
 * 当然前提是有first文件
 * 
 * @author zh
 * @version 0.1
 * @since Oct 13,2014
 */
public class First implements Bin {

	public String getName() {
		return "first : series cmd of data getter one step - get categrey url";
	}

	/**
	 * first don't have rate , begin from zero
	 */
	public void run(InputStream is, PrintStream os, String cmd) {
		// scheme name
		String name = null;
		String rate = null;
		if(cmd.substring(cmd.indexOf("first")+5).trim().equals("")) {
			os.println("first -n scheme name");
			return;
		}
		else if (cmd.indexOf("-n") != -1) {
			name = cmd.substring(cmd.indexOf("-n") + 2).trim();
		}
		if(name == null)  {
			os.println("first -n scheme name");
			return;
		}
		rate = "0";
		Core.add(name, Idata.ONLY_FIRST, rate);
	}

}
