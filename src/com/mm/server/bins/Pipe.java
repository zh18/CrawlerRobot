package com.mm.server.bins;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import com.mm.server.Bin;
import com.mm.server.BinCaller;
import com.mm.util.SystemUtil;

/**
 * 命令队列，可以把命令插入到队列中，一个一个的执行
 * 
 * @author zh
 * @version 0.1
 * @since Oct 13,2014
 */
public class Pipe implements Bin {

	private List<BinCaller> bins = new ArrayList<BinCaller>();
	
	/**
	 *  pp : -a cmd  pp 
	 *       -run run cmd from first
	 *       -rm remove cmd
	 *       -c clear cmd 
	 *       -show show cmd
	 */
	public String getName() {
		return "pp : here is a pipe , so you can add task into this pipe";
	}

	public void run(InputStream is, PrintStream os, String cmd) {
		if(cmd.indexOf("-") == -1) {
			os.println(" pp -[a|c|rm|run|show] scheme | null");
			return;
		}
		String op = cmd.substring(cmd.indexOf("-")+1);
		if(op.startsWith("a")) {
			String cmdc = cmd.substring(cmd.indexOf("-a")+2).trim();
			if(cmdc.trim().equals("")) return;
			bins.add(new BinCaller(is,os,cmdc));
		}
		else if(op.startsWith("c")) {
			bins.clear();
		}
		else if(op.startsWith("rm")) {
			//注，这个方法会删除所有带 cmdc字符串的命令
			String cmdc = cmd.substring(cmd.indexOf("-rm")+3).trim();
			if(cmdc.trim().equals("")) return;
			for(BinCaller b:bins){
				if(b.getCMD().indexOf("cmdc") != -1)
					SystemUtil.iteratorDelete(bins, cmdc);
			}
		}
		else if(op.startsWith("run")){
			if(bins.size() == 0 ) {
				os.println("there is not cmd in pipe");
				return;
			}
			for(BinCaller b:bins){
				b.run();
			}
		}
		else if(op.startsWith("show")) {
			for(BinCaller b:bins){
				os.println(b.getCMD());
			}
		}
	}

}
