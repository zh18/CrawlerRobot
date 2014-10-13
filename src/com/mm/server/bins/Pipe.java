package com.mm.server.bins;

import java.io.InputStream;
import java.io.PrintStream;

import com.mm.server.Bin;

/**
 * 命令队列，可以把命令插入到队列中，一个一个的执行
 * 
 * @author zh
 * @version 0.1
 * @since Oct 13,2014
 */
public class Pipe implements Bin {

	public String getName() {
		return "pp : here is a pipe , so you can add task into this pipe";
	}

	public void run(InputStream is, PrintStream os, String cmd) {
		// TODO Auto-generated method stub

	}

}
