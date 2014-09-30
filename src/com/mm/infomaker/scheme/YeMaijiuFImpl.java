package com.mm.infomaker.scheme;

import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.mm.infomaker.base.Element;
import com.mm.infomaker.base.FBase;



public class YeMaijiuFImpl extends FBase{

	Document doc = null;
	Elements elist = null;
	
	public Element getMessage(String content) {
		String html = getHtml(content);
		Document doc = Jsoup.parse(html);
		String url = getUrl(content);
		String murl = getMurl(url);
		String price = getPrice(doc,"b.myPrice em");
		String type = getType(doc,"div.crumb a");
		String name = getName(doc,"div.promotionMiddleTop h1");
		List<String> imgs = getImgs(doc, "ul#image_list li img");
		return new Element(url,murl,price,name,imgs,type);
	}

	@Override
	public Element getMessage(String html, String path) {
		// TODO Auto-generated method stub
		return null;
	}

	// http://m.yesmywine.com/viewGoods.jspa?goodsid=
	public String getMurl(String url){
		String id = url.substring(url.lastIndexOf("/")+1, url.lastIndexOf(".html"));
		return "http://m.yesmywine.com/viewGoods.jspa?goodsid="+id;
	}
	
	public String getName(String type){
		String temp = type.substring(0, type.lastIndexOf("က"));
		temp = temp.substring(temp.lastIndexOf("က"), temp.length());
		return temp;
	}
	
}
