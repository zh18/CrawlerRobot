package com.mm.server;

import java.io.File;
import java.net.ServerSocket;
import java.net.Socket;

import com.mm.core.Core;
import com.mm.logger.Log;
import com.mm.util.SYS;
import com.mm.util.SystemUtil;

public class StartServer {

	private final static int PORT = 9998;
	private static ServerSocket ss = null;
	private static Socket s = null;
	public static long times = 0L;
	
	// 启动监听程序
	public static void startListener() {
		try {
			ss = new ServerSocket(PORT);
			while(true){  
				s=ss.accept();  
		        new Thread(new Listener(s)).start();
		    }
		}catch(Exception e){
			Log.logger.error("listener start failed",e);
		}
	}
	
	public static void startCore(){
		Core.init();
	}
	
	public static void registerBins(){
		String file = "/home/public/javaproject/CrawlerRobot/bin/";
		File files[] = new File(file+SYS.SYS_BIN_FLODER).listFiles();
		SystemUtil.loadClass(file+SYS.SYS_BIN_FLODER, file, SYS.SYS_BIN);
		for(File f:files){
			try {
				//       .addBin("com.server.bin.");
				Object temp = Class.forName(SYS.SYS_BIN+"."+(f.getName().substring(0, f.getName().indexOf(".")))).newInstance();
				if (temp instanceof Bin)
					BinCaller.registerBin((Bin) temp);
			}catch(Exception e){
				Log.logger.error("Bin initliazed error", e);
			}
		}
	}
	
	public static void reboot(){
		try {
			if (null != ss && !ss.isClosed() || null != s && !ss.isClosed()){
				s.close();
				ss.close();
			}
			registerBins();
			startCore();
			startListener();
		}catch(Exception e){
			Log.logger.error("Reboot error", e);
		}
	}
	
	public static void main(String[] args) {
		times = System.currentTimeMillis();
		reboot();
	}
	
}
