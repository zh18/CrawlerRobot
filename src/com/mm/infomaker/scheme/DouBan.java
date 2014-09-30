package com.mm.infomaker.scheme;

import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.mm.infomaker.base.Element;
import com.mm.infomaker.base.FBase;

public class DouBan extends FBase{

	Elements elist = null;
	Document doc = null;
	
	
	public Element getMessage(String content) {
		return null;
	}

	
	public Element getMessage(String content, String path) {
		String html = getHtml(content);
		String url = getUrl(content);
		doc = Jsoup.parse(html);
		String name= getName(doc,"#wrapper h1 span");
		String type= getType(getId(url),path);
		List<String> imgs = getImgs(doc,"#mainpic img");
		String murl = url;
		String price = getPrice(doc,"ul.more-after li a");
		return new Element(url,murl,price,name,imgs,type);
	}
	// ul.more-after li a
	// _#_name#link#price_#_  type
	public String getId(String url){
		String temp = url.substring(url.indexOf("subject/")+8, 0);
		temp = temp.substring(0, url.indexOf("/"));
		return temp;
	}
	
	
	public String getPrice(Document doc,String cssQuery){
		StringBuilder result = new StringBuilder();
		elist = doc.select(cssQuery);
		for(org.jsoup.nodes.Element e:elist){
			result.append("_က_"+e.select("div.basic-info > span").text()+"က"+e.attr("href")
					+"က"+e.select("span.buylink-price")
					+"_က_");
		}
		return result.toString();
	}
	
}
