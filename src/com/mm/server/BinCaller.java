package com.mm.server;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class BinCaller implements Runnable{
	
	private static List<Bin> allBins = null;
	
	private InputStream is;
	private PrintStream ps;
	private String cmd;
	
	public BinCaller(InputStream is,PrintStream ps,String cmd){
		this.is = is;
		this.ps = ps;
		this.cmd = cmd;
	}
	
	
	public static void registerBin(Bin bin){
		if (null == allBins) allBins = new ArrayList<Bin>();
		allBins.add(bin);
	}
	
	public static void clearBins(){
		if(null != allBins)
			allBins.clear();
	}
	
	/**
	 * 复写equals方法 与进来的字符串做比对
	 */
	public boolean equals(Object s){
		if(s == this) return true;
		if(s instanceof String) {
			return cmd.equals(s);
		}
		return false;
	}
	
	public void call(InputStream is,PrintStream os,String cmd){
		String temp = cmd.split(" ")[0];
		for(Bin b:allBins){
			if (b.getName().split(" ")[0].startsWith(temp)){
				b.run(is, os, cmd);
				break;
			}
		}
		
	}
	
	public String getCMD(){
		return cmd;
	}
	
	public static List<Bin> getAllBins(){
		return allBins;
	}
	
	public void run(){
		call(is, ps, cmd);
	}
}
