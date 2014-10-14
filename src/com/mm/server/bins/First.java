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
		try {
			name = cmd.substring(cmd.indexOf("-n") + 2, cmd.indexOf("-r"))
					.trim();
			rate = cmd.substring(cmd.indexOf("-r") + 2).trim();
		} catch (Exception e) {
			name = cmd.substring(cmd.indexOf("-n")).trim();
		}
		
		rate = "0";
		Core.add(name, Idata.FIRST, rate);
	}

}
