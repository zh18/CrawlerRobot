package com.mm.spider;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URI;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.mm.util.SystemUtil;
import com.sun.swing.internal.plaf.synth.resources.synth;

public class Spider implements ISpider {

	private HttpURLConnection conn = null;
	private String cookie = null;
	public Spider() {
	}

	public String getCookie(String url){
		HttpURLConnection conn = null;
		int code = 0;
		String keyname = null;
		try {
			conn = (HttpURLConnection) new URL(url).openConnection();
			conn.setUseCaches(false);
			conn.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:31.0) Gecko/20100101 Firefox/31.0");
			conn.setRequestProperty("Cache-Control", "no-cache");  
//			connection.setInstanceFollowRedirects(false);
			conn.setConnectTimeout(1000); //timeout 3s
			conn.setReadTimeout(1200);//read timeout 5s
			code = conn.getResponseCode();
			if(code == HttpURLConnection.HTTP_OK){
				Map<String,List<String>> map = conn.getHeaderFields(); 
				for(String s:map.keySet()){
					if(s == null) continue;
					if(s.toLowerCase().indexOf("cookie") !=-1){
						keyname = s;
						break;
					}
				}
				if(keyname == null) return null;
				Iterator<String> it = map.get(keyname).iterator();
				StringBuffer sbu = new StringBuffer();  
			    sbu.append("eos_style_cookie=default; ");  
			    while(it.hasNext()){  
			        sbu.append(it.next());  
			    }
			    return sbu.toString();
			}
			conn.disconnect();
			return null;
		}catch(Exception e){
			return null;
		}
	}

	public synchronized String spider(Proxy proxy, String url) {
		StringBuffer content = new StringBuffer();
		if(cookie == null){
			cookie = getCookie(url);
		}
		try {
			conn = getConnection(proxy, url);

			conn.setRequestProperty("User-Agent",
					"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:27.0) Gecko/20100101 Firefox/27.0");
			conn.setRequestProperty("Cache-Control", "no-cache");
			// conn.setRequestProperty("Connection", "keep-alive");
			// conn.setRequestProperty("Cookie", cookie);
			if(cookie != null){
				conn.setRequestProperty("Cookie", cookie);
			}
			String charset = null;
			try {
				conn.connect();
				charset = conn.getContentType();
			} catch (Exception e) {
				return null;
			}
			if (charset == null || charset.indexOf('=') < 0)
				charset = "gbk";
			else if (charset.equalsIgnoreCase("gbk"))
				charset = "gbk";
			else
				charset = charset.substring(charset.indexOf('=') + 1).trim();
			Scanner in = new Scanner(conn.getInputStream(), charset);
			while (in.hasNextLine()) {
				content.append(in.nextLine() + "\n");
			}
			in.close();
			conn.disconnect();
		} catch (IOException e) {
			// 添加日志处理内容
		}
		return content.toString();
	}

	public String spider(String url) {
		String content = spider(null, url);
		if(null != content) return content;
		try {
			int i = 0;
			while (content != null) {
				content = spider(null, url);
				i++;
				if (content != null)
					return content;
				if (i > REMAX)
					return null;
			}
		} catch (Exception e) {
		}
		return null;
	}

	public String spider(String url, int retries) {
		String content = spider(url);
		int c = 0;
		while (content == null && c < retries) {
			try {
				Thread.sleep(500);
				c++;
			} catch (Exception e) {
			}
			content = spider(url);
		}
		return content;
	}

	protected HttpURLConnection getConnection(Proxy proxy, String url)
			throws IOException {
		if (null != proxy)
			conn = (HttpURLConnection) new URL(url).openConnection(proxy);
		else
			conn = (HttpURLConnection) new URL(url).openConnection();
		// conn default setting .
		conn.setUseCaches(false);
		conn.setConnectTimeout(3000); // timeout 3s
		conn.setReadTimeout(5000);// read timeout 5s
		// setting headers
		return conn;
	}

	
	
	public static void main(String[] args) throws IOException {
		SystemUtil
				.write("/home/public/Desktop/test.html",
						(new Spider()
								.spider("http://www.amazon.com/s/ref=lp_27_nr_n_13/177-4014461-6913731?rh=n%3A283155%2Cn%3A!1000%2Cn%3A27%2Cn%3A17148&bbn=27&ie=UTF8&qid=1413428086&rnid=27")));
	}
}
