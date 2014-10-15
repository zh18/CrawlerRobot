package com.mm.data.multithreading;

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
import com.mm.util.SystemUtil;

public class FirstDoImpl implements Doable<String> {

	protected Selector selector = null;
	protected SpiderFactory factory = null;
	protected ISpider spider = null;
	protected IMul<String> imul = null;
	protected Set<String> nextSet = null;
	
	protected Document doc = null;
	protected Elements elist = null;
	protected String html,url,line;
	
	public FirstDoImpl(Selector selector,SpiderFactory factory,Set<String> nextSet,IMul<String> imul){
		this.selector = selector;
		this.factory = factory;
		spider = factory.getSpider();
		this.nextSet = nextSet;
		this.imul = imul;
	}
	
	public void x(String t) throws Exception {
		List<String> temp2 = new ArrayList<String>(); // 把根网页中 含有selects的信息提哪家到需要爬取的网页中
		List<List<String>> temp = new ArrayList<List<String>>(); // 获取循环爬取的网页
		temp.add(selector.getRootpath());
		List<String> add = new ArrayList<String>();
		for (int i = 0; i < selector.getFselects().length; i++) {   
			// 当有第一级为根节点时，加入add列表中，再第二级加入爬取下一级子节点，如果不存再，则继续加入下一节点
			temp2 = new ArrayList<String>();
			for (String url : temp.get(i)) {
				// i use this to stop this thread
				html = spider.spider(url);
				if (html == null) {
					continue;
				}
				doc = Jsoup.parse(html);
				elist = doc.select(selector.getFselects()[i]);
				if (elist.size() == 0) {
					add.add(url);
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
		String [] urls = new String[temp.get(temp.size()-1).size()];
		urls = SystemUtil.listToArray(temp.get(temp.size()-1), urls);
		imul.push(urls);
		imul.shoot();
		SystemUtil.writeColl(temp.get(temp.size()-1), selector.getSavepath()+Idata.fname);
	}
}
