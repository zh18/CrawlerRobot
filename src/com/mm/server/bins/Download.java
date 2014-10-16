package com.mm.server.bins;

import java.io.InputStream;
import java.io.PrintStream;

import com.mm.core.Core;
import com.mm.core.Task;
import com.mm.data.Idata;
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

	/**
	 * 每一个step of 3 都应该式一个独立的任务，独立的名字，独立的再数据库中
	 * 
	 * 格式： download -n scheme_name -r rate
	 */
	public void run(InputStream is, PrintStream os, String cmd) {
		//scheme name
		String name = null;
		String rate = null;
		if(cmd.substring(cmd.indexOf("download")+8).trim().equals("")) {
			os.println("download -n scheme name | -r rate");
			return;
		}
		try {
			// little bug here 
			if(cmd.indexOf("-n") != -1 && cmd.indexOf("-r") != -1) {
				name = cmd.substring(cmd.indexOf("-n")+2, cmd.indexOf("-r")).trim();
				rate = cmd.substring(cmd.indexOf("-r")+2).trim();
			}
		}catch(Exception e){
			if(cmd.indexOf("-n") != -1)
				name = cmd.substring(cmd.indexOf("-n")+2).trim();
			else {
				os.println("download -n scheme name | -r rate");
				return;
			}
		}
		if(!Task.fileCheck(name, Idata.DOWNLOAD)) {
			os.print("There is no have url files , please use first or tk -n scheme first");
			return;
		}
		if(null == rate) rate = "0";
		Core.add(name,Idata.DOWNLOAD,rate);
	}

}
