package com.mm.data.struct;

import java.util.ArrayList;
import java.util.List;

import com.mm.util.SystemUtil;

public class Selector {

	public final static String ROOT="rootpath",
			FS="fselects",PS="products",NS="next",SP="savepath",
			FB="fbase",NB="nbase",PB="pbase",NAME="name",CS="classify",BD="brand";
	public final static String TS="title",
			PRICES="price",
			IMGS = "imgs",
			TYPE = "type",
			MURL = "murl";
	
	
	private String name;
	private List<String> rootpath;
	private String [] fselects;
	private String products;
	private String next;
	private String savepath;
	private String classify;
	private String brand;
	
	
	private String fbase;
	private String pbase;
	private String nbase;
	
	private String title;
	private String price;
	private String imgs;
	private String type;
	private String murl;
	
	
	
	public Selector(String name, List<String> rootpath, String[] fselects,
			String products, String next, String savepath, String classify,
			String brand, String fbase, String pbase, String nbase) {
		super();
		this.name = name;
		this.rootpath = rootpath;
		this.fselects = fselects;
		this.products = products;
		this.next = next;
		this.savepath = savepath;
		this.classify = classify;
		this.brand = brand;
		this.fbase = fbase;
		this.pbase = pbase;
		this.nbase = nbase;
	}




	

	public Selector(String name, String[] rootpath, String[] fselects,
			String products, String next, String savepath, String classify,
			String brand, String fbase, String pbase, String nbase) {
		this.name = name;
		this.rootpath = new ArrayList<String>();
		for(String s:rootpath){
			this.rootpath.add(s.trim());
		}
		this.fselects = new String[fselects.length];
		for(int i=0;i<fselects.length;i++){
			this.fselects[i] = fselects[i].trim();
		}
		this.products = products;
		this.next = next;
		this.savepath = savepath;
		this.classify = classify;
		this.brand = brand;
		this.fbase = fbase;
		this.pbase = pbase;
		this.nbase = nbase;
	}






	public String getName() {
		return name;
	}






	public void setName(String name) {
		this.name = name;
	}






	public List<String> getRootpath() {
		return rootpath;
	}






	public void setRootpath(List<String> rootpath) {
		this.rootpath = rootpath;
	}






	public String[] getFselects() {
		return fselects;
	}






	public void setFselects(String[] fselects) {
		this.fselects = fselects;
	}






	public String getProducts() {
		return products;
	}






	public void setProducts(String products) {
		this.products = products;
	}






	public String getNext() {
		return next;
	}






	public void setNext(String next) {
		this.next = next;
	}






	public String getSavepath() {
		return savepath;
	}






	public void setSavepath(String savepath) {
		this.savepath = savepath;
	}






	public String getClassify() {
		return classify;
	}






	public void setClassify(String classify) {
		this.classify = classify;
	}






	public String getBrand() {
		return brand;
	}






	public void setBrand(String brand) {
		this.brand = brand;
	}






	public String getFbase() {
		return fbase;
	}






	public void setFbase(String fbase) {
		this.fbase = fbase;
	}






	public String getPbase() {
		return pbase;
	}






	public void setPbase(String pbase) {
		this.pbase = pbase;
	}






	public String getNbase() {
		return nbase;
	}






	public void setNbase(String nbase) {
		this.nbase = nbase;
	}


	



	public Selector(String name, String [] rootpath, String[] fselects,
			String products, String next, String savepath, String classify,
			String brand, String fbase, String pbase, String nbase,
			String title, String price, String imgs, String type, String murl) {
		super();
		this.name = name;
		this.rootpath = new ArrayList<String>();
		for(String s:rootpath) this.rootpath.add(s);
		this.fselects = fselects;
		this.products = products;
		this.next = next;
		this.savepath = savepath;
		this.classify = classify;
		this.brand = brand;
		this.fbase = fbase;
		this.pbase = pbase;
		this.nbase = nbase;
		this.title = title;
		this.price = price;
		this.imgs = imgs;
		this.type = type;
		this.murl = murl;
	}

	
	
	public String getTitle() {
		return title;
	}






	public void setTitle(String title) {
		this.title = title;
	}






	public String getPrice() {
		return price;
	}






	public void setPrice(String price) {
		this.price = price;
	}






	public String getImgs() {
		return imgs;
	}






	public void setImgs(String imgs) {
		this.imgs = imgs;
	}






	public String getType() {
		return type;
	}






	public void setType(String type) {
		this.type = type;
	}






	public String getMurl() {
		return murl;
	}






	public void setMurl(String murl) {
		this.murl = murl;
	}






	public String toString(){
		return name+"\n"+
				"--------------------\n"+
				"rootpath         : "+SystemUtil.printAL(rootpath, ",")+"\n"+
				"savepath         : "+savepath+"\n"+
				"\n"+
				"first cssQuery   : "+SystemUtil.printAL(fselects, ",")+"\n"+
				"product cssQuery : "+products+"\n"+
				"next cssQuery    : "+next+"\n"+
				"\n"+
				"first base       : "+fbase+"\n"+
				"product base     : "+pbase+"\n"+
				"next  base       : "+nbase+
				"\n"+
				"classify         : "+classify+"\n"+
				"brand            : "+brand;
	}
}
