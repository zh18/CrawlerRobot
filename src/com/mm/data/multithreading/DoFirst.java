package com.mm.data.multithreading;

import java.io.IOException;
import java.util.Set;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.mm.data.Idata;
import com.mm.data.model.IFirstModel;
import com.mm.data.model.IProductModel;
import com.mm.data.model.SuperProductModel;
import com.mm.data.struct.Selector;
import com.mm.logger.Log;
import com.mm.mul.Doable;
import com.mm.spider.ISpider;
import com.mm.spider.SpiderFactory;
import com.mm.stop.BreakPoint;
import com.mm.util.SYS;
import com.mm.util.SystemUtil;

public class DoFirst implements Doable<String>{
	
	private Selector selector = null;
	private BreakPoint bp = null;
	private Set<String> error = null;
	protected Document doc = null;
	protected Elements elist = null;
	protected String html,line;
	
	protected IFirstModel pro = null;
	
	public DoFirst(Selector selector,BreakPoint bp,Set<String> error){
		this.selector = selector;
		this.bp = bp;
		this.error = error;
		Class<IFirstModel> clazz = null;
//		pro = new SuperProductModel(selector, sf, bp, error);
		
		try {
			clazz = (Class<IFirstModel>) Class.forName(SYS.SYS_MODEL+"."+Selector.getClassName(selector.getName()));
			pro = clazz.newInstance();
			pro.init(selector,bp,error);
		}catch(Exception e){
			
		}
	}
	
	public void x(String t){
		try {
			Set<String> temp = pro.first0();
			SystemUtil.writeColl(temp, selector.getSavepath()+Idata.fname);
		}catch(Exception e){
			Log.logger.error("model first method x() error ",e); 
		}
	}
}
