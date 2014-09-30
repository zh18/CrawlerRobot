package com.mm.server;


import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

import com.mm.db.DataBase;

/**
 * I don't what i should say
 * 
 * @author zh
 * @version 0.1
 * @since Sep 26 , 2014
 */
public class Shell {
	
	private String gclass,iclass;
	private DataBase db;
	
	public void shell(InputStream is,OutputStream os){
		String name = "";
		Scanner scan = new Scanner(is);
		PrintStream ps = new PrintStream(os);
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
				if (cmd.equals("quit") || cmd.equals("-q")) 
					break;
				BinCaller.call(is,os,cmd);
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
	
	public String getGclass() {
		return gclass;
	}

	public void setGclass(String gclass) {
		this.gclass = gclass;
	}

	public String getIclass() {
		return iclass;
	}

	public void setIclass(String iclass) {
		this.iclass = iclass;
	}
}
