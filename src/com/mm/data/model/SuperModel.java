package com.mm.data.model;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import com.mm.mul.Doable;
import com.mm.spider.ISpider;
import com.mm.spider.SpiderFactory;
import com.mm.spider.SpiderFactoryImpl;
import com.mm.stop.BreakPoint;
import com.mm.util.SystemUtil;

public class SuperModel extends SpiderFactoryImpl implements IFirstModel,IProductModel{

	private Selector selector;
	private BreakPoint breakpoint;
	private Document doc;
	private Elements elist;
	private Set<String> error = null;
	private String html,line;
	private ISpider spider = null;
	
	public ISpider getSpider(String url){
		return super.getSpider();
	}

	public SuperModel(){}
	
	public void init(Selector selector,BreakPoint breakpoint,Set<String> error){
		this.selector = selector;
		spider = this.getSpider();
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

	public Set<String> first0() {
		breakpoint.setPname(Idata.FIRST);
		
		List<String> temp2 = new ArrayList<String>(); // 把根网页中 含有selects的信息提哪家到需要爬取的网页中
		List<List<String>> temp = new ArrayList<List<String>>(); // 获取循环爬取的网页
		temp.add(selector.getRootpath());
		List<String> add = new ArrayList<String>();
		for (int i = 0; i < selector.getFselects().length; i++) {   
			// 当有第一级为根节点时，加入add列表中，再第二级加入爬取下一级子节点，如果不存再，则继续加入下一节点
			temp2 = new ArrayList<String>();
			breakpoint.setTotla(selector.getFselects().length);
			for (String url : temp.get(i)) {
				// i use this to stop this thread
				breakpoint.recover(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()).toString(), 
						breakpoint.getWname(), Idata.FIRST, String.valueOf(i));
				
				html = spider.spider(url);
				if (html == null) {
					continue;
				}
				doc = Jsoup.parse(html);
				elist = doc.select(selector.getFselects()[i]);
				if (elist.size() == 0) {
					add.add("first"+url);
					continue;
				} else {
					for (Element e : elist) {
						if (!selector.getFbase().equals("#")) 
							line = selector.getFbase()+e.attr("href");
						else
							line = e.attr("href");
						temp2.add(line);
					}
				}
			}
			temp2.addAll(add);
			if (temp2.size() != 0) {
				temp.add(temp2);
				add = new ArrayList<String>();
			}
		}
		return (Set<String>)SystemUtil.readCollectionFromCollection(temp.get(temp.size()-1),Set.class);
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
