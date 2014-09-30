package com.mm.server.bin;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import com.mm.server.Bin;

/**
 * 对scheme进行热加载，热删除，暂停，重启等操作
 * @author public
 *
 */
public class Scheme implements Bin{

	private String name = String.format("%15s","scheme -s")+" : Add , Delete , Stop , Restart Scheme Task , \n";  // 具体说明
	
	@Override
	public void run(InputStream is, OutputStream os, String cmd) {
		PrintStream ps = new PrintStream(os);
		if(cmd.indexOf("-h") != -1)
			ps.println(getName());
	}

	public String getName() {
		return name;
	}
	
}
