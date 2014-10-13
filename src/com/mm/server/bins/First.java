package com.mm.server.bins;

import java.io.InputStream;
import java.io.PrintStream;

import com.mm.server.Bin;

/**
 * 直接开始下载product url的环节
 * 
 * 当然前提是有first文件
 * @author zh
 * @version 0.1
 * @since Oct 13,2014
 */
public class First implements Bin {

	public String getName() {
		return "first : series cmd of data getter one step - get categrey url";
	}

	@Override
	public void run(InputStream is, PrintStream os, String cmd) {
		// TODO Auto-generated method stub

	}

}
