package com.mm.datagetter;

/**
 * 此类用于得到选择器
 * @author zh
 * @version 0.1
 */
public class Selectors {

	private String rootpath;
	private String [] fselects;
	private String products;
	private String next;
	private String basepath;
	/**
	 * 以下三个布尔看需不需要base目录
	 */
	private boolean fbase;
	private boolean pbase;
	private boolean nbase;

	public Selectors(String rootpath,String basepath,String[] fselects, String products, String next) {
		this.rootpath = rootpath;
		this.fselects = fselects;
		this.products = products;
		this.next = next;
		this.basepath = basepath;
		fbase = pbase = nbase = false;
	}
	
	public Selectors(String rootpath,String basepath,String[] fselects, String products, String next,
			boolean fbase, boolean pbase, boolean nbase) {
		this.rootpath = rootpath;
		this.basepath = basepath;
		this.fselects = fselects;
		this.products = products;
		this.next = next;
		this.fbase = fbase;
		this.pbase = pbase;
		this.nbase = nbase;
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

	public String getBasepath() {
		return basepath;
	}

	public void setBasepath(String basepath) {
		this.basepath = basepath;
	}

	public boolean isFbase() {
		return fbase;
	}

	public void setFbase(boolean fbase) {
		this.fbase = fbase;
	}

	public boolean isPbase() {
		return pbase;
	}

	public void setPbase(boolean pbase) {
		this.pbase = pbase;
	}

	public boolean isNbase() {
		return nbase;
	}

	public void setNbase(boolean nbase) {
		this.nbase = nbase;
	}

	public String getRootpath() {
		return rootpath;
	}

	public void setRootpath(String rootpath) {
		this.rootpath = rootpath;
	}
	
	
	
}
