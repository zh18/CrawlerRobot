package com.mm.data;

import com.mm.data.struct.Selector;
import com.mm.spider.SpiderFactory;
import com.mm.stop.BreakPoint;

public interface Idata {

	public String DOWNLOAD="download",
				  FIRST = "first",
				  PRODUCT = "product",
				  CLASS = "classify",
				  BRAND = "brand",
				  INFO = "info",
				  DONE = "done";
					
	
	public BreakPoint getBreakPoint();
	
	public void setBreakPoint(BreakPoint breakpoint);
	
	public void data() throws Exception ;
	
	public void setFactory(SpiderFactory factory);
	
	public void setSelector(Selector selector);
	
	public void setName(String name);

}
