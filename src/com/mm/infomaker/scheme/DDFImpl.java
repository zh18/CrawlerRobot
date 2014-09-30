package com.mm.infomaker.scheme;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.mm.infomaker.base.Element;
import com.mm.infomaker.base.FormatterBase;

public class DDFImpl implements FormatterBase {

	String json = "";
	String html = "";
	String lines[] = null;
	Document doc = null;
	Elements elist = null;
	/**
	 * 通过传进来的一个html页面和一个json页面，获得一个element
	 */
	public synchronized Element getMessage(String html){
		this.html = getHtml(html);
		this.json = getJson(html);
		doc = Jsoup.parse(html);
		String name = this.getName(doc);
		String url = this.getUrl(html);
		String murl = this.getMurl(url);
		List<String> imgs = this.getImgs(doc);
		String type = this.getType(doc);
		String price = this.getPrice(doc);
		return new Element(url,murl,price,name,imgs,type);
	}
	
	private List<String> getImgs(Document doc){
		List<String> result = new ArrayList<String>();
		elist = doc.select("#mainimg_pic img");
		if (elist.size() == 0) return result;
		for(org.jsoup.nodes.Element e:elist){
			if (e.attr("src").indexOf("http://") != -1)
				result.add(e.attr("src"));
		}
		return result;
	}
	
	private String getPrice(Document doc) {
		Elements elist = doc.select("span#originalPriceTag");
		if (elist.size()==0) return "0";
		return elist.get(0).text();
	}
	
	private String getMurl(String url){
		//http://m.dangdang.com/product.php?pid=1171933512
		//http://product.dangdang.com/410251118.html?ref=t-136039-3031_3-857419-1
		if (null == url || "".equals(url)) return "";
		String result = url.substring(url.lastIndexOf("/")+1, url.lastIndexOf(".html"));
		return "http://m.dangdang.com/product.php?pid="+result;
	}
	
	private String getType(Document doc){
		elist = doc.select("div.breadcrumb a");
		if (elist.size() == 0) return "";
		StringBuilder sb = new StringBuilder();
		for(org.jsoup.nodes.Element e:elist){
			if (!e.text().equals(">"))
				sb.append(e.text()+"က");
		}
		return sb.toString();
	}
	
	public String getName(Document doc) {
		doc = Jsoup.parse(html);
		elist = doc.select("div[name=Title_pub]>h1");
		if (elist.size()==0) return "";
		return elist.get(0).text();
	}

	public String getUrl(String html) {
		String lines [] = html.split("\n");
		String temp = "";
		for(int i=0;i<lines.length;i++){
			if (lines[i].indexOf("ကကက") != -1){
				temp =  lines[i].substring(3, lines[i].length()-3);
				break;
			}
		}
		return temp;
	}
	
	/**
	 * First line is  |  ### url ###     |
	 * then           |    content       |
	 * then           |   ## jsonurl ## |
	 * then           |    json          |
	 */
	public static String getHtml(String content){
		String temp[] = content.split("\n");
		StringBuilder sb = new StringBuilder("");
		for(int i=0;i<temp.length;i++){
			if (temp[i].indexOf("ကကက") != -1)  // have this line 
				continue;
			if (temp[i].indexOf("ကက") != -1)    // have this json line 
				return sb.toString();
			sb.append(temp[i]+"\n");
		}
		return sb.toString();
	}

	public static String getJson(String content){
		String temp[] = content.split("\n");
		StringBuilder sb = new StringBuilder();
		int i = 0;
		for(;i<temp.length;i++) {
			if (temp[i].startsWith("ကကh"))   // don't have this line ကကhttp://
				break;
		}
		for(i++;i<temp.length;i++){
			sb.append(temp[i]);
		}
		return sb.toString();
	}

	@Override
	public Element getMessage(String html, String path) {
		// TODO Auto-generated method stub
		return null;
	}
}
