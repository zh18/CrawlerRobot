package com.mm.server.bins;

import java.io.InputStream;
import java.io.PrintStream;

import com.mm.core.Core;
import com.mm.core.Task;
import com.mm.data.Idata;
import com.mm.server.Bin;

public class MulDown implements Bin {

	public String getName() {
		return "md : multithreading download ";
	}

	public void run(InputStream is, PrintStream os, String cmd) {
		// scheme name
		String name = null;
		String rate = "0";
		String threadnumbers = null;
		int nums = -1;
		if(cmd.substring(2).trim().equals("")) {
			os.println("md scheme_name -r rate -t thread numbers");
			return ;
		}
		else {
			name = cmd.substring(2).trim();
			try {
				name = name.substring(0, name.indexOf(" ")).trim();
			}catch(Exception e){
			}
		}
		if(cmd.indexOf("-r") != -1){
			rate = cmd.substring(cmd.indexOf("-r")+2);
			try {
				rate = rate.substring(0, rate.indexOf(" "));
			}catch(Exception e){
			}
		}
		else if(cmd.indexOf("-t") != -1){
			threadnumbers = cmd.substring(cmd.indexOf("-t")+2).trim();
			try {
				nums = Integer.parseInt(threadnumbers);
			}catch(Exception e){}
		}
		
		Core.addMul(name, Idata.DOWNLOAD, rate,nums);
	}

}
