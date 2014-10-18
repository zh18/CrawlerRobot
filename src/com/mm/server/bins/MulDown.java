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
		String rate = null;
		if (cmd.substring(cmd.indexOf("md") + 2).trim().equals("")) {
			os.println("md -n scheme name | -r rate");
			return;
		}
		// little bug here
		if (cmd.indexOf("-n") != -1 && cmd.indexOf("-r") != -1) {
			name = cmd.substring(cmd.indexOf("-n") + 2, cmd.indexOf("-r"))
					.trim();
			rate = cmd.substring(cmd.indexOf("-r") + 2).trim();
		} else if (cmd.indexOf("-n") != -1)
			name = cmd.substring(cmd.indexOf("-n") + 2).trim();
		else {
			os.println("md -n scheme name | -r rate");
			return;
		}
		if (!Task.fileCheck(name, Idata.DOWNLOAD)) {
			os.print("There is no have url files , please use first or tk -n scheme first");
			return;
		}
		if (null == rate)
			rate = "0";
		Core.addMul(name, Idata.DOWNLOAD, rate);
	}

}
