package com.mm.data.model;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.mm.data.Idata;
import com.mm.data.struct.Selector;
import com.mm.data.struct.Type;
import com.mm.spider.ISpider;
import com.mm.spider.SpiderFactory;
import com.mm.stop.BreakPoint;
import com.mm.util.SystemUtil;

public class SuperProductModel implements IProductModel{

	private SpiderFactory sf = null;
	private Selector selector;
	private BreakPoint breakpoint;
	private Document doc;
	private Elements elist;
	private Set<String> error = null;
	private String html,line;
	private ISpider spider = null;
	
	public SuperProductModel(Selector selector,SpiderFactory sf,BreakPoint breakpoint,Set<String> error){
		this.selector = selector;
		this.sf = sf;
		spider = sf.getSpider();
		this.breakpoint = breakpoint;
		this.error = error;
	}
	
	
	public void getPro0(String url) throws IOException {
		boolean isType = isTypes();
		String typetemp = "";
		error.clear();
		List<String> urls = SystemUtil.readLine(selector.getSavepath()+Idata.fname);
		int rate = Integer.parseInt(breakpoint.getRate());
		if (rate > urls.size()) return;
		String line = "";
		boolean newfile = breakpoint.getRate().equals("0");
		
		for(int i=rate;i<urls.size();i++){
			url = urls.get(i);
			breakpoint.setTotla(urls.size());
			breakpoint.recover(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()).toString(), 
					breakpoint.getWname(), Idata.DOWNLOAD, String.valueOf(i));
			do {
				try {
					html = spider.spider(url);
				}catch(Exception e){
					error.add("product:"+url);
					continue;
				}
				if (null == html) {
					error.add("product:"+url);
					continue;
				}
				doc = Jsoup.parse(html);
				elist = doc.select(selector.getProducts());
				
				if(isType){
					typetemp = getType(doc, isType);
				}
				
				for(Element e:elist){
					if (!selector.getPbase().equals("#"))
						//可以获取id 形成链接
						line = selector.getPbase()+getId(e.attr("href"));
					else 
						line = e.attr("href");
					if(line.startsWith("java")) continue;
					newfile = false;
					SystemUtil.appendFile(selector.getSavepath()+Idata.uname, line,newfile);
					if (isType) {
						SystemUtil.appendFile(selector.getSavepath()+Idata.tname,new Type(typetemp,line).toString(),newfile);
					}
				}
			}while ((url = getNextLink(html,selector.getNbase().equals("#")?"":selector.getNbase(),selector.getNext())) != null);
		}
		SystemUtil.appendFile(selector.getSavepath()+Idata.ename, error);
	}
	
	protected final boolean isTypes(){
		return !selector.getClassify().equals("#");
	}
	
	protected String getId(String url){
		return "";
	}
	
	protected String getType(Document doc,boolean isType){
		if(!isType) return "";
		Elements elist = doc.select(selector.getClassify());
		StringBuffer bs = new StringBuffer();
		String temp = null;
		for(Element e:elist){
			temp = e.text();
			//这个地方应该搞成替换队列
			temp = temp.replaceAll("[(\\d)]", "");
			if(temp.indexOf(">") != -1 || temp.indexOf("<") != -1 || temp.equals("")) continue;
			bs.append(temp+"က");
		}
		return bs.toString();
	}
	
	protected String getNextLink(String html,String basepath,String cssQuery) {
		doc = Jsoup.parse(html);
		elist = doc.select(cssQuery);
		
		String nextlink = elist.attr("href");
		if ("".equals(nextlink)) return null;
		return basepath+nextlink;
	}
}
