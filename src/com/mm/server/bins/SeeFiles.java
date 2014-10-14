package com.mm.server.bins;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.PrintStream;

import com.mm.data.struct.Selector;
import com.mm.logger.Log;
import com.mm.server.Bin;
import com.mm.util.ReadSelector;

public class SeeFiles implements Bin {

	public String getName() {
		return "sf : see files";
	}

	public void run(InputStream is, PrintStream os, String cmd) {
		String name = cmd.substring(cmd.indexOf("sf")+2).trim();
		if(name.equals("")) {
			StringBuffer sb = new StringBuffer();
			for(String s:ReadSelector.getAllNames()) sb.append(s+"  ");
			os.println(sb.toString());
		}
		else {
			Selector s = ReadSelector.getSelector(name);
			String savepath = s.getSavepath();
			try {
				pickfiles(new File(savepath),os);
			}catch(FileNotFoundException e){
				os.println("we dont find this path ");
				Log.logger.warn("See Files : don't find this file"+name, e);
			}
		}
	}
	
	private void pickfiles(File f,PrintStream ps) throws FileNotFoundException {
		File [] file = f.listFiles();
		String space = "";
		for(File f1:file){
			if(f1.isFile()){
				ps.println(space+f1.getName()+" "+f1.getTotalSpace());
			}
			else if(f1.isDirectory()){
				ps.println("-"+f1.getName());
				space += " ";
			}
		}
	}
}
