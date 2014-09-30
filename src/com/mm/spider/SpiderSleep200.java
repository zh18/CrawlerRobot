package com.mm.spider;

import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.SocketAddress;
import java.net.URL;
import java.util.Scanner;

import com.mm.logger.Log;

public class SpiderSleep200 implements ISpider {

	public String spider(String url) {
		HttpURLConnection conn = null;
		StringBuffer sb = new StringBuffer();
		try {
			conn = (HttpURLConnection) new URL(url).openConnection();
			try {
				Thread.sleep(200);
			}catch(Exception e){
				Log.logger.error("spider sleep 200 error", e);
			}
			conn.connect();
			String charset = conn.getContentType();
			if(charset == null || charset.indexOf('=') < 0)
				charset = "gbk";
			else 
				charset = charset.substring(charset.indexOf('=') + 1).trim();
			Scanner in = new Scanner(conn.getInputStream(), charset);
			while (in.hasNextLine()) {
				sb.append(in.nextLine() + "\n");
			}
			in.close();
			conn.disconnect();
			if (!"".equals(sb.toString()))
				return sb.toString();
			return null;
		}catch(Exception e){
			Log.logger.warn("Spider with sleep was error", e);
			return null;
		}
	}
}
