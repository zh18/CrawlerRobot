package com.mm.data.multithreading;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.util.Set;

import com.mm.data.Idata;
import com.mm.spider.ISpider;
import com.mm.spider.SpiderFactory;

public class DownloadDoImpl implements Doable<String> {

	protected BufferedReader br = null;
	protected BufferedWriter bw = null;
	
	protected ISpider spider = null;
	protected SpiderFactory factory = null;
	protected String html, line, url;
	protected Set<String> error;

	public DownloadDoImpl(BufferedReader br,BufferedWriter bw,SpiderFactory factory,Set<String> error) {
		this.br = br;
		this.bw = bw;
		this.factory = factory;
		this.spider = factory.getSpider();
	}
	
	public synchronized void x(String t) throws Exception {
		html = spider.spider(t);
		if(null == html) {
			error.add(t);
			return;
		}
		bw.write(Idata.mark+t+Idata.mark);
		bw.write(html);
	}
}
