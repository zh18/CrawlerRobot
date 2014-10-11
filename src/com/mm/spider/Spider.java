package com.mm.spider;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.util.Scanner;

public class Spider implements ISpider{
	
	private HttpURLConnection conn = null;
	
	public Spider(){}
	
	public String spider(Proxy proxy,String url) {
		StringBuffer content = new StringBuffer();
		try {
			conn = getConnection(proxy, url);
			
			conn.setRequestProperty("User-Agent",
					"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:27.0) Gecko/20100101 Firefox/27.0");
//			conn.setRequestProperty("X-Requested-With", "XMLHttpRequest");
//			conn.setRequestProperty("Referer", "http://www.suning.com/");
//			conn.setRequestProperty("Accept-Encoding", "gzip, deflate");
			conn.connect();
			String charset = conn.getContentType();
			if(charset == null || charset.indexOf('=') < 0)
				charset = "gbk";
			else 
				charset = charset.substring(charset.indexOf('=') + 1).trim();
			Scanner in = new Scanner(conn.getInputStream(), charset);
			while (in.hasNextLine()) {
				content.append(in.nextLine() + "\n");
			}
			in.close();
			conn.disconnect();
		}catch(IOException e){
			//添加日志处理内容
		}
		return content.toString();
	}
	
	public String spider(String url){
		return spider(null,url);
	}
	
	public String spider(String url,int retries){
		String content = spider(url);
		int c = 0;
		while(content == null && c < retries) {
			try {
				Thread.sleep(500);
				c++;
			}catch(Exception e){}
			content = spider(url);
		}
		return content;
	}
	
	
	protected HttpURLConnection getConnection(Proxy proxy,String url) throws IOException {
		if (null != proxy)
			conn = (HttpURLConnection) new URL(url).openConnection(proxy);
		else 
			conn = (HttpURLConnection) new URL(url).openConnection();
		// conn default setting .
		conn.setUseCaches(false);
		conn.setConnectTimeout(3000); //timeout 3s
		conn.setReadTimeout(5000);//read timeout 5s
		// setting headers
		return conn;
	}
	
	
	public static void main(String[] args) {
		System.out.println(new Spider().spider("http://list.suning.com/0-161664-25-0-0-9017.html"));
	}
}
