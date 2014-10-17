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
import com.mm.spider.ISpider;
import com.mm.spider.SpiderFactory;
import com.mm.stop.BreakPoint;
import com.mm.util.SystemUtil;

public class SuperFirstModel implements IFirstModel{

	protected BreakPoint bp = null;
	protected Selector selector = null;
	protected ISpider spider = null;
	protected String html;
	protected Document doc;
	protected Elements elist;
	protected String line;
	
	public SuperFirstModel(SpiderFactory sf,BreakPoint bp,Selector selector){
		spider = sf.getSpider();
		this.bp = bp;
		
	}
	
	
	public void first0() {
		bp.setPname(Idata.FIRST);
		
		List<String> temp2 = new ArrayList<String>(); // 把根网页中 含有selects的信息提哪家到需要爬取的网页中
		List<List<String>> temp = new ArrayList<List<String>>(); // 获取循环爬取的网页
		temp.add(selector.getRootpath());
		List<String> add = new ArrayList<String>();
		for (int i = 0; i < selector.getFselects().length; i++) {   
			// 当有第一级为根节点时，加入add列表中，再第二级加入爬取下一级子节点，如果不存再，则继续加入下一节点
			temp2 = new ArrayList<String>();
			bp.setTotla(selector.getFselects().length);
			for (String url : temp.get(i)) {
				// i use this to stop this thread
				bp.recover(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()).toString(), 
						bp.getWname(), Idata.FIRST, String.valueOf(i));
				
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
		SystemUtil.writeColl(temp.get(temp.size()-1), selector.getSavepath()+Idata.fname);
		try {
			SystemUtil.appendFile(selector.getSavepath()+Idata.uname, Idata.ename);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}	
}
