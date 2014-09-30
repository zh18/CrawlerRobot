package com.mm.infomaker.base;

import java.util.List;



/*
 * # URLကက手机版URLကက价格ကက名称ကက图片URL1က……图片URLnကက所属类别1က……所属类别nက（换行）
 */
public class Element implements IElement{
	
	private String url;
	private String murl;
	private String price;
	private String name;
	private List<String> imgurls;
	private String type;
	
	public Element(String url, String murl, String price, String name,
			List<String> imgurls, String type) {
		super();
		this.url = url;
		this.murl = murl;
		this.price = price;
		this.name = name;
		this.imgurls = imgurls;
		this.type = type;
	}



	// URLကက手机版URLကက价格ကက名称ကက图片URL1က……图片URLnကက所属类别1က……所属类别nက（换行）
	public String toString(){
		StringBuilder base =  new StringBuilder(url+"ကက"+murl+"ကက"+price+"ကက");
		base.append(name+"ကက");
		for(String s:imgurls)
			base.append(s+"က");   //img end have one က  , so get type mean - 1 က
		base.append("က"+type+"\n");
		return base.toString();
	}

	public boolean isNull(){
		return null == url || null == name || null == type || null == murl ||
				null == murl || null == price;
	}
	

	public String getUrl() {
		return url;
	}



	public void setUrl(String url) {
		this.url = url;
	}



	public String getMurl() {
		return murl;
	}



	public void setMurl(String murl) {
		this.murl = murl;
	}



	public String getPrice() {
		return price;
	}



	public void setPrice(String price) {
		this.price = price;
	}



	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	public List<String> getImgurls() {
		return imgurls;
	}



	public void setImgurls(List<String> imgurls) {
		this.imgurls = imgurls;
	}



	public String getType() {
		return type;
	}



	public void setType(String type) {
		this.type = type;
	}
	
	
	
	
}
