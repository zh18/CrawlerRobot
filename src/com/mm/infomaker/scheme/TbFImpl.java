package com.mm.infomaker.scheme;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.mm.infomaker.base.Element;
import com.mm.infomaker.base.FormatterBase;



public class TbFImpl implements FormatterBase{

	Document doc = null;
	Elements elist = null;
	String url = "";
	//က
	
	public String getName(Document doc) {
		elist = doc.select("h3.tb-main-title");
		if (elist.size() == 0) return "";
		String name = null;
		try {
			name = elist.get(0).text();
		}catch(Exception e){return null;}
		return name;
	}


	public String getUrl(String html) {
		String [] lines = html.split("\n");
		String temp = "";
		for(String s:lines){
			if (s.indexOf("ကကက") != -1){
				temp = s;
				break;
			}
		}
		if (null == temp || "".equals(temp) ) return null;
		temp = temp.substring(3, temp.length()-3);
		url = temp;
		return temp;
	}

	//http://item.taobao.com/item.htm?spm=a217f.7283197.1997524881-0.1.RbOflb&id=38697817746
	public String getMurl(String url) {
		if (null == url || "".equals(url)) return "";
		String temp = url.substring(url.indexOf("id=")+3, url.length());
		return "http://a.m.taobao.com/i"+temp+".htm";
	}


	public List<String> getImgs(String json) {
		if (null == json || "".equals(json) || json.indexOf("ERRCODE_QUERY_DETAIL_FAIL") != -1) return null;
		String temp = null;
		try{
			temp = json.substring(json.indexOf("picsPath"), json.length());
		}catch(Exception e){return null;}
				
		temp = temp.substring(temp.indexOf("[")+1, temp.indexOf("]"));
		List<String> result = new ArrayList<String>();
		String lines[] = temp.split(",|\"");
		for(String s:lines){
			if(s.startsWith("http://")){
				result.add(s);
			}
		}
		return result;
	}


	public String getType(String html,String path) {
		if ("".equals(url)) this.getUrl(html);
		BufferedReader br = null;
		String line = "";
		String temp = "";
		String id = url.substring(url.indexOf("id=")+3, url.length());
		try {
			 br = new BufferedReader(new FileReader(path+"type.txt"));
			 while ((line = br.readLine()) != null) {
				 if (line.indexOf(id) != -1){
					 temp = line;
					 break;
				 }
			 }
			 br.close();
		}catch(Exception e){e.printStackTrace();}
		if (null == temp || "".equals(temp)) return "";
		temp = temp.substring(temp.indexOf("ကက")+2, temp.length());
		return temp+"က";
	}


	public String getPrice(Document doc) {
		elist = doc.select("div.J_PromoHd em.tb-rmb-num");
		if (elist.size() == 0) return "0";
		return elist.get(0).text();
	}

	
	public String getPrice(String json){
		String temp = "0";
		try {
			temp = json.substring(json.indexOf("price")+8, json.length());
			temp = temp.substring(temp.indexOf("price")+10, temp.length());
			temp = temp.substring(0, temp.indexOf("\\\""));
//			System.out.println(temp);
		}catch(Exception e){
//			e.printStackTrace();
		}
		return temp;
	}
	
	
	public synchronized Element getMessage(String content,String path) {
		String html = DDFImpl.getHtml(content);
		doc = Jsoup.parse(html);
		String json = DDFImpl.getJson(content);
		String url = this.getUrl(content);
		String murl = this.getMurl(url);
		String name = this.getName(doc);
		String type = this.getType(content,path);
		String price = this.getPrice(json);
		List<String> imgs = this.getImgs(json);
		if (null == name || null == url || null == type || "".equals(type)) return null;
		return new Element(url,murl,price,name,imgs,type);
	}
	
	public Element getMessage(String content){return null;}
	
}
