package com.mm.spider;

import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;
import java.net.URL;
import java.util.Scanner;

import com.mm.logger.Log;
import com.mm.util.SystemUtil;

public class SpiderAutoProxy implements ISpider {

	private static final String[][] proxy = SystemUtil.readProxy();

	public String spider(String url) {
		HttpURLConnection conn = null;
		StringBuffer sb = new StringBuffer();
		URL url_ = null;
		SocketAddress addr = null;
		Proxy typeProxy = null;
		try {
			url_ = new URL(url);
			for(int i=0;i<proxy.length;i++){
				addr = new InetSocketAddress(proxy[i][0],Integer.parseInt(proxy[i][1]));
				typeProxy = new Proxy(Proxy.Type.HTTP, addr);
				conn = (HttpURLConnection) url_.openConnection(typeProxy);
				//with a little header  :-)
				conn.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:31.0) Gecko/20100101 Firefox/31.0");
				conn.setConnectTimeout(3000);
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
				if (null != sb.toString() || !"".equals(sb.toString())) 
					return sb.toString();
			}
			Log.logger.warn("proxy list is no of use . ");
		}catch(Exception e){
			Log.logger.warn("Spider with autoproxy was error", e);
			return null;
		}
		return null;
	}
}
