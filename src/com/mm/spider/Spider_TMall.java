package com.mm.spider;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class Spider_TMall implements ISpider {

	
	public String spider(String url) {
		String content = null;
		try {
			Connection conn = Jsoup.connect(url).timeout(5000);
			// 模拟浏览器请求
			// conn.header("Request-Line",
			// "GET /article/JobInfo/144413?_uid=guest&_uid=guest HTTP/1.1");
			conn.header("User-Agent",
					"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:27.0) Gecko/20100101 Firefox/27.0");
			conn.header("X-Requested-With", "XMLHttpRequest");// 重要,必须添加
			conn.header("Referer", "http://www.tmall.com/");
			conn.header("Accept-Encoding", "gzip, deflate");
			Document doc = conn.timeout(50000).get();
			content = doc.toString();
		} catch (Exception ex) {
			return null;
		}
		return content;
	}
}
