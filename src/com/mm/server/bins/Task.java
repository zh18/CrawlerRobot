package com.mm.server.bins;

import java.io.InputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Scanner;

import com.mm.core.Core;
import com.mm.server.Bin;
import com.mm.util.ReadSelector;
import com.mm.util.SYS;
import com.mm.util.SystemUtil;

public class Task implements Bin {

	public String getName() {
		return "tk : task";
	}

	public void run(InputStream is, PrintStream os, String cmd) {
		String name = null;
		Scanner scan = new Scanner(is);
		if(cmd.indexOf("-") != -1){
			cmd = cmd.trim();
			name = cmd.substring(cmd.lastIndexOf(" "), cmd.length()).trim();
			if (cmd.indexOf("-n") != -1) {
				os.print("Go on the breakpoint ? y or n : ");
				Core.add(name, scan.nextLine().indexOf("y") != -1);
			}
			else if (cmd.indexOf("-s") != -1) {
				Core.start(name);
			}
			else if (cmd.indexOf("-a") != -1) {
				Core.showReady(os);
			}
			else if (cmd.indexOf("-p") != -1) {
				if(name.indexOf("_")==-1) {
					for(String s:SystemUtil.getAllKeys(SYS.PATH+SYS.SYS_DG_SCHEME_FLODER+name.trim()+".properties")){
						os.println(s);
					}
				}
				else
					os.println(ReadSelector.getSelector(name.trim()).toString());
			}
			else if (cmd.indexOf("-sl") != -1){
				Core.startAll();
			}
		}
		else { 
			Core.showList(os);
		}
	}

}
