package com.mm.spider;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.util.Scanner;

import com.mm.util.SystemUtil;
import com.sun.swing.internal.plaf.synth.resources.synth;

public class Spider implements ISpider{
	
	private HttpURLConnection conn = null;
	
	public Spider(){}
	
	public synchronized String spider(Proxy proxy,String url) {
		StringBuffer content = new StringBuffer();
		try {
			conn = getConnection(proxy, url);
			
			conn.setRequestProperty("User-Agent",
					"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:27.0) Gecko/20100101 Firefox/27.0");
			conn.setRequestProperty("X-Requested-With", "XMLHttpRequest");
//			conn.setRequestProperty("Referer", "http://www.tmall.com");
//			conn.setRequestProperty("Accept-Encoding", "gzip, deflate");
			String charset = null;
			try {
				conn.connect();
				charset = conn.getContentType();
			}catch(Exception e){
				return null;
			}
			if(charset == null || charset.indexOf('=') < 0)
				charset = "gbk";
			else if(charset.equalsIgnoreCase("gbk"))
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
		String content =  spider(null,url);
		try {
			int i=0;
			while(content != null){
				content = spider(null,url);
				i ++;
				if(content != null) return content;
				if(i>REMAX) return null;
			}
		}catch(Exception e){
		}
		return null;
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
	
	
	public static void main(String[] args) throws IOException {
		SystemUtil.write("/home/public/Desktop/test.html",
				(new Spider().spider("http://list.tmall.com/search_product.htm?spm=a220m.1000858.0.0.HlegBj&cat=50026506&sort=s&style=g&search_condition=7&from=sn_1_rightnav&active=1&industryCatId=50026506#J_crumbs")));
	}
}
