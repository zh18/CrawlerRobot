package com.mm.server.bins;

import java.io.InputStream;
import java.io.PrintStream;

import com.mm.server.Bin;

/**
 * 直接开始下载环节
 * 
 * 前提条件是有url.txt文件
 * 
 * @author zh
 * @version 0.1
 * @since Oct 13,2014
 */
public class Download implements Bin {

	public String getName() {
		return "download : series cmd of data getter one step - download html pages";
	}

	public void run(InputStream is, PrintStream os, String cmd) {
		// TODO Auto-generated method stub

	}

}
