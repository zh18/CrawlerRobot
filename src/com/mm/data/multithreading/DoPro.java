package com.mm.data.multithreading;

import java.util.Set;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.mm.data.Idata;
import com.mm.data.model.IProductModel;
import com.mm.data.model.SuperProductModel;
import com.mm.data.struct.Selector;
import com.mm.mul.Doable;
import com.mm.spider.ISpider;
import com.mm.spider.SpiderFactory;
import com.mm.stop.BreakPoint;
import com.mm.util.SystemUtil;

public class DoPro implements Doable<String>{

	private ISpider spider = null;
	private Selector selector = null;
	private BreakPoint bp = null;
	private Set<String> error = null;
	protected Document doc = null;
	protected Elements elist = null;
	protected String html,line;
	/*
	 * 自动通过反射加载
	 */
	protected IProductModel pro = null;
	
	public DoPro(Selector selector,BreakPoint bp,SpiderFactory sf,Set<String> error){
		spider = sf.getSpider();
		this.selector = selector;
		this.bp = bp;
		this.error = error;
		Class<IProductModel> clazz = null;
		bp.setPname(Idata.PRODUCT);
		pro = new SuperProductModel(selector, sf, bp, error);
		
		try {
//			clazz = Class.forName(className);
		}catch(Exception e){
			
		}
	}
	
	public void x(String t){
		if(null == bp || null == pro || null == bp.getRate()) {
			System.out.println("null bp");
			return;
		}
		int rate = Integer.parseInt(bp.getRate());
		try {
			pro.getPro0(t);
		}catch(Exception e){
			e.printStackTrace();
		}
		bp.setRate(String.valueOf(rate++));
	}

	public IProductModel getPro() {
		return pro;
	}

	public void setPro(IProductModel pro) {
		this.pro = pro;
	}
}
