package com.mm.server;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import com.mm.db.DataBase;
import com.mm.logger.Log;

public class Shell{
	
	
	public void shell(Socket s) throws Exception{
		InputStream is = s.getInputStream();
		PrintStream ps = new PrintStream(s.getOutputStream());
		String name = null;
		Scanner scan = new Scanner(is);
		String cmd = null;
		if ((name=demandLogin(scan,ps))!=null) {
			ps.println("\n Welcome to Crawler Robot ! ");
			for(;;){
				try {
					ps.print(name+"@"+InetAddress.getLocalHost().getHostName().toString()+":~$ ");
				} catch (UnknownHostException e) {
					e.printStackTrace();
				}
				cmd = scan.nextLine();
				if (cmd.equals("")) continue;
				if (cmd.equals("quit") || cmd.equals("-q")) 
					break;
//				new Thread(new BinCaller(is,ps,cmd)).start();
				new BinCaller(is, ps, cmd).run();
//				ps.flush();
			}
		}
	}
	
	public String demandLogin(Scanner scan,PrintStream ps){
		ps.println("Please input UserName , Password");
		ps.print("Account :");
		String name = scan.nextLine();
		ps.print("Password:");
		String pwd = scan.nextLine();
		if (login(name.trim(), pwd.trim())){
			return name.trim();
		}
		return null;
	}
	
	public boolean login(String name,String pwd){
		if (DataBase.login(name, pwd)) {
			return true;
		}
		return false;
	}
}
