package com.mm.data.multithreading;

import java.io.File;
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
import com.mm.util.SystemUtil;

public class ProductDoImpl implements Doable<String> {

	protected Selector selector = null;

	protected ISpider spider = null;
	protected SpiderFactory factory = null;
	protected IMul<String> imul = null;
	
	protected Document doc = null;
	protected Elements elist = null;
	protected String html, line ,url;
	protected Set<String> error;
	protected Set<String> checknew = null;
	
	
	public ProductDoImpl(Selector selector,Set<String> error,Set<String> checknew,IMul<String> imul) {
		this.error = error;
		this.selector = selector;
		this.imul = imul;
	}

	public void x(String s) throws Exception{
		if (!check(Idata.FIRST))
			return;
		// 查看是否需要加入type文件
		boolean isType = isTypes();
		String typetemp = "";
		do {
			html = spider.spider(url);
			if (null == html) {
				error.add(url);
				continue;
			}
			doc = Jsoup.parse(html);
			elist = doc.select(selector.getProducts());
			if (isType) {
				typetemp = getType(doc, isType);
			}

			for (Element e : elist) {
				if (!selector.getPbase().equals("#"))
					// 可以获取id 形成链接
					line = selector.getPbase() + getId(e.attr("href"));
				else
					line = e.attr("href");
				SystemUtil.appendFile(selector.getSavepath() + Idata.uname,
						line, false);
				//有新的一行的时候，就把这个链接压入下一个动作的stack
				if(checknew.add(line))
					imul.push(line);
				if (isType) {
					SystemUtil.appendFile(selector.getSavepath() + Idata.tname,
							new Type(typetemp, line).toString(), false);
				}
			}
		} while ((url = getNextLink(html, selector.getNbase().equals("#") ? ""
				: selector.getNbase(), selector.getNext())) != null);
		imul.shoot();
	}

	protected String getId(String url) {
		return "";
	}

	protected final boolean isTypes() {
		return !selector.getClassify().equals("#");
	}

	protected String getType(Document doc, boolean isType) {
		if (!isType)
			return "";
		Elements elist = doc.select(selector.getClassify());
		StringBuffer bs = new StringBuffer();
		String temp = null;
		for (Element e : elist) {
			temp = e.text();
			// 这个地方应该搞成替换队列
			temp = temp.replaceAll("[(\\d)]", "");
			if (temp.indexOf(">") != -1 || temp.indexOf("<") != -1
					|| temp.equals(""))
				continue;
			bs.append(temp + "က");
		}
		return bs.toString();
	}

	protected String getNextLink(String html, String basepath, String cssQuery) {
		doc = Jsoup.parse(html);
		elist = doc.select(cssQuery);

		String nextlink = elist.attr("href");
		if ("".equals(nextlink))
			return null;
		return basepath + nextlink;
	}

	private boolean check(String part) {
		if (part.equals(Idata.PRODUCT))
			return check0(selector.getSavepath() + Idata.fname);
		else if (part.equals(Idata.DOWNLOAD))
			return check0(selector.getSavepath() + Idata.uname);
		return false;
	}

	private boolean check0(String name) {
		File file = new File(name);
		if (file.exists() && SystemUtil.readLine(name).size() > 0)
			return true;
		return false;
	}
}