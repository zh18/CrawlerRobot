package com.mm.server;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import com.mm.logger.Log;

public class Listener implements Runnable {
	
	private Socket socket;
	private Shell shell;
	
	
	public Listener(Socket socket){
		this.socket = socket;
		shell = new Shell();
	}
	
	
	public void run(){
		InputStream in = null;
		OutputStream os = null;
		try {
			in = socket.getInputStream();
			os = socket.getOutputStream();
			shell.shell(in, os);
			in.close();
			os.close();
			socket.close();
		}catch(Exception e){
			Log.logger.error("Socket start failed");
			e.printStackTrace();
		}
	}
	
	
	
}
