package com.mm.spider;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

import com.mm.logger.Log;

public class SpiderWithHeader implements ISpider {

	
	private static final String [][] headers = new String[][]{
		{"User-Agent","Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:31.0) Gecko/20100101 Firefox/31.0"},
		{"User-Agent","Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1; Trident/4.0)"},
		{"User-Agent","ozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1; WOW64; Trident/4.0)"},
		{"User-Agent","Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:1.8.1.11) Gecko/20071127 Firefox/2.0.0.11"},
	};
	
	public String spider(String url) {
		HttpURLConnection conn = null;
		StringBuffer sb = new StringBuffer();
		try {
			conn = (HttpURLConnection) new URL(url).openConnection();
			conn.setUseCaches(false);
			int r = (int)Math.random()*10%4;
			conn.setRequestProperty(headers[r][0],headers[r][1]);
			conn.setConnectTimeout(3000); //timeout 3s
			conn.setReadTimeout(5000);
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
			return sb.toString();
		}catch(Exception e){
			Log.logger.error("Spider was not work ",  e);
			return null;
		}
	}

}
