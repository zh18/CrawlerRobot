/**
 * 提供交互界面.
 * 功能：
 * 		1)登录
 * 		2)order list  
 *                 i: ls 查看所有进行中的任务以及进度  
 *                 ii:exit推出client   
 *                 iii:change 更改系统参数   
 *                 iv:热加载
 *                 v :kill 杀死线程   
 * 
 * @author zh
 * @version 0.1
 */
package com.mm.client;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class Client {
	public static void main(String[] args) throws Exception {
		Socket s = new Socket("localhost",9998);
		PrintStream pw = new PrintStream(s.getOutputStream());
		Scanner scan = new Scanner(System.in);
		BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
//		BufferedInputStream bis = new BufferedInputStream(s.getInputStream());
		new ReadServerMessage(br);
		String line = "";
		while(true){
			line = scan.nextLine();
			pw.println(line);
			if(line.equals("quit") || line.equals("-q")){
				break;
			}
		}
		s.close();
	}
}
class ReadServerMessage extends Thread  {//从服务器读取消息     
    BufferedReader bis;
    Socket socket; 
    public ReadServerMessage(BufferedReader bis)  
        {  
            this.bis = bis;
            start();  
        }  
  
    public void run()  
        {  
    		StringBuffer sb = new StringBuffer();
            char buffer[] = new char[1024];
            int len;
            while (true)//一直等待着服务器的消息  
                {  
                    try {
						while((len=bis.read(buffer)) != -1) {  
							sb.append(new String(buffer,0,len));
						}
					} catch (IOException e) {
						e.printStackTrace();
					}   
                    System.out.print(sb.toString());  
                }  
        }  
}  