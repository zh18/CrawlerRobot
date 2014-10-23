package com.mm.server.bins;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

import com.mm.core.Core;
import com.mm.server.Bin;

public class Top{

//	public String getName(){
//		return "top";
//	}
//	
//	public void run(InputStream is, PrintStream os, String cmd) {
//		String q = "";
//		TopIn top = new TopIn(q,os);
//		top.start();
//		Scanner scan = new Scanner(is);
//		while(true){
//			q = scan.nextLine();
//			top.q = q;
//		}
//	}
//	
//	class TopIn extends Thread{
//		String q = "";
//		PrintStream os = null;
//		
//		public TopIn(String q,PrintStream os){
//			this.q = q;
//			this.os = os;
//		}
//		
//		public void run(){
//			while(q.equals("q")){
//				try {
//					Thread.sleep(200);
//				}catch(Exception e){
//					e.printStackTrace();
//				}
//				Core.showList(os);
//			}
//		}
//	}
	
}
