package com.mm.server;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.mm.server.bin.Help;
import com.mm.server.bin.InitCore;
import com.mm.server.bin.Kill;
import com.mm.server.bin.Quit;
import com.mm.server.bin.Scheme;
import com.mm.server.bin.TaskList;

public class BinCaller {
	
	static List<Bin> bins = null;
	
	public static List<Bin> getInstance(){
		if (null == bins){
			synchronized (BinCaller.class) {
				bins = new ArrayList<Bin>();
			}
		}
		return bins;
	}

	public static void call(InputStream is,OutputStream os,String cmd){
		boolean dobin = false;
		String maincmd = cmd;
		try {
			maincmd = cmd.substring(0,cmd.indexOf(" "));
		}catch(Exception e){}
		for(Bin b:bins){
			if (b.getName().indexOf(maincmd)!= -1) {
				b.run(is, os, cmd);
				dobin = true;
				break;
			}
		}
		if (!dobin){
			new PrintStream(os).println("no cmd names like "+cmd+" , please try help or -h");
		}
	}
	
	
	
	public static void addBin(Bin bin){
		bins.add(bin);
	}
	
	public static void removeBin(Bin bin){
		bins.remove(bin);
	}
	
	public static int binSize(){
		return bins.size();
	}
	
	public static List<Bin> getBinList(){
		return bins;
	}
}
