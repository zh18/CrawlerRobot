package com.mm.data.multithreading;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.mm.data.Idata;
import com.mm.data.model.IProductModel;
import com.mm.data.model.SuperProductModel;
import com.mm.data.struct.Selector;
import com.mm.logger.Log;
import com.mm.mul.Doable;
import com.mm.spider.ISpider;
import com.mm.spider.SpiderFactory;
import com.mm.stop.BreakPoint;
import com.mm.util.FileC;
import com.mm.util.SYS;
import com.mm.util.SystemUtil;

public class DoPro implements Doable<String> {

	private Selector selector = null;
	private BreakPoint bp = null;
	private Set<String> error = null;
	protected Document doc = null;
	protected Elements elist = null;
	protected String html, line;
	private FileC wUname,wUFname;
	/*
	 * 自动通过反射加载
	 */
	protected IProductModel pro = null;

	public DoPro(Selector selector, BreakPoint bp, SpiderFactory sf,
			Set<String> error) {
		this.selector = selector;
		this.bp = bp;
		this.error = error;
		bp.setPname(Idata.PRODUCT);
		pro = new SuperProductModel(selector, sf, bp, error);
	}
	
	public DoPro(Selector selector, BreakPoint bp,SpiderFactory sf,
			Set<String> error,FileC wUname,FileC wUFname) {
		this.selector = selector;
		this.bp = bp;
		this.error = error;
		bp.setPname(Idata.PRODUCT);
		this.wUname = wUname;
		this.wUFname = wUFname;
		pro = new SuperProductModel(selector, sf, bp, error,wUname,wUFname);
	}

	public void x(String t) {
		if (null == bp || null == pro || null == bp.getRate()) {
			System.out.println("null bp");
			return;
		}
		try {
			pro.getPro0(t);
			bp.setPname(Idata.PRODUCT);
			bp.refreshTime();
			synchronized (bp) {
				bp.incRate();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public IProductModel getPro() {
		return pro;
	}

	public void setPro(IProductModel pro) {
		this.pro = pro;
	}
}
