package com.mm.data.multithreading;

import java.util.List;

import com.mm.data.Idata;
import com.mm.util.SystemUtil;

public class Download implements Runnable{
	
	private Idata idata = null;
	private List<String> urls = null;
	
	public Download(Idata idata){
		this.idata = idata;
		//if we have this file , and everything is go well
		urls = SystemUtil.readLine(idata.getSelector().getSavepath()+idata.uname);
		
	}
	
	public void run(){
		
	}
}
