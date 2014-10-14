package com.mm.server.bins;

import java.io.InputStream;
import java.io.PrintStream;

import com.mm.core.Core;
import com.mm.core.Task;
import com.mm.data.Idata;
import com.mm.server.Bin;

public class Product implements Bin {

	public String getName() {
		return "pro : series cmd of data getter one step - get products url";
	}

	public void run(InputStream is, PrintStream os, String cmd) {
		String name = null;
		String rate = null;
		try {
			name = cmd.substring(cmd.indexOf("-n") + 2, cmd.indexOf("-r"))
					.trim();
			rate = cmd.substring(cmd.indexOf("-r") + 2).trim();
		} catch (Exception e) {
			name = cmd.substring(cmd.indexOf("-n")).trim();
		}
		if(!Task.fileCheck(name, Idata.PRODUCT)) {
			os.print("There is no have first files");
			return;
		}
		if (null == rate)
			rate = "0";
		Core.add(name, Idata.PRODUCT, rate);
	}

}
