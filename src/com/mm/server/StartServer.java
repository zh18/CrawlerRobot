package com.mm.server;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

import com.mm.core.Core;
import com.mm.logger.Log;
import com.mm.server.bin.Help;
import com.mm.util.SYS;
import com.mm.util.SystemUtil;

/**
 * 
 * 1)开启爬虫机器人服务，  一个线程
 * 2)开放9998端口，等待外部robot client链接发送命令  一个线程
 * 
 * Q:如何通过telnet进行链接
 * Q2:网页管理
 * 
 * @author zh
 * @version 0.1
 */

public class StartServer {
	
	public static final int PORT = 9998;
	
	// 启动监听程序
	public static void startListener() {
		ServerSocket ss = null;
		try {
			ss = new ServerSocket(PORT);
			while(true)  
	        {  
				Socket s=ss.accept();  
	            new Thread(new Listener(s)).start(); 
	        }
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	//启动Core
	public static void startCore(){
		Core.init();
	}
	
	
	
	public static void main(String[] args) {
		registerBins();
		startCore();	
		startListener();
	}
	
	
	public static void registerBins(){
		String file = "/home/public/javaproject/CrawlerRobot/bin/";
		File files[] = new File(file+SYS.SYS_BIN_FLODER).listFiles();
		SystemUtil.loadClass(file+SYS.SYS_BIN_FLODER, file, SYS.SYS_BIN);
		if (null == BinCaller.bins) {
			BinCaller.getInstance();
		}
		for(File f:files){
			try {
				//       .addBin("com.server.bin.");
				Object temp = Class.forName(SYS.SYS_BIN+"."+(f.getName().substring(0, f.getName().indexOf(".")))).newInstance();
				if (temp instanceof Bin)
					BinCaller.addBin((Bin) temp);
			}catch(Exception e){
				Log.logger.error("Bin initliazed error", e);
			}
		}
	}
}
