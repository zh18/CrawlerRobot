package com.mm.infomaker.scheme;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.mm.infomaker.base.Element;
import com.mm.infomaker.base.FBase;



public class WMFImpl extends FBase {


	public Element getMessage(String html) {

		return null;
	}


	public Element getMessage(String content, String path) {
		String html = getHtml(content);
		Document doc = Jsoup.parse(html);
		String url = getUrl(content);
		String murl = getMurl(url);
		String name = getName(doc,"h1.productTitle");
		String price = getPrice(html);
		String id = url.substring(url.lastIndexOf("/")+1, url.length());
		String type = getType(id,path+"type.txt");
		List<String> imgs = getImgs(doc, "img#mainImage");
		if (type.equals("")) return null;
		return new Element(url,murl,price,name,imgs,type);
	}
	//http://mobile.walmart/#ip/ip
	public String getMurl(String url){
		String id = url.substring(url.lastIndexOf("/")+1, url.length());
		return "http://mobile.walmart/#ip/"+id;
	}
	
	public String getUrl(String content){
		String url = super.getUrl(content);
		String id = null;
		try {
			id = url.substring(url.lastIndexOf("/")+1, url.length());
		}catch(Exception e){}
		return "http://www.walmart.com/ip/"+id;
	}
	
	
	public String getPrice(String html){
		String temp = "0";
		try {
			temp = html.substring(html.indexOf("item_price")+13, html.length());
			temp = temp.substring(0, temp.indexOf("]")-2);
		}catch(Exception e){}
		return temp;
	}
	
	public String getType(String id,String path){
		BufferedReader br = null;
		String line = "";
		String temp = "";
		try {
			br = new BufferedReader(new FileReader(path));
			while ((line = br.readLine()) != null){
				if (line.indexOf(id) != -1){
					temp = line;
					break;
				}
			}
			br.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		if ("".equals(temp)) return "";
		temp = temp.substring(temp.indexOf("ကက")+2);
		try {
			temp = temp.substring(0,temp.indexOf("ကက"));
		}catch(Exception e){}
		return temp;
	}
	
}
