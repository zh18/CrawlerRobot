package com.mm.datagetter;

import java.util.List;
import java.util.Set;

import com.mm.spider.ISpider;
import com.mm.spider.SpiderFactory;
import com.mm.stop.BreakPoint;
/**
 * 所有需要下载的网站scheme必须实现此接口
 * 
 * 可以继承DG类来实现一部分功能
 * @author zh
 * @version 0.1
 * @since Sep 29,2014
 */

public interface IDG {
	
	public int REPEATMAX = 5;
	public String FIRST = "first";
	public String PRO = "product";
	public String DOWN = "download";
	public String DONE = "0";
	public String NOT_DONE="1";
	
	
	/**
	 * part const
	 */
	public String ALL = "all";  
	/**
	 * downloading message from breakpoint 
	 * 
	 * <pre> if we don't have a hreakpoint , we start from head</pre>
	 */
	public void go(String part,boolean bp);
	
	/**
	 * set break point so that my go() method can read where start
	 * @param bk 断点
	 */
	public void setBreakPoint (BreakPoint bk);
	
	public void setSelectors (Selectors slector);
	
	public void setSpiderfactory(SpiderFactory factory);
	
	
	public String getWname();
	
	public String getPname();
	
	public String getRate();
	
	public String getDone();
	
	public String loggerhelper(String message);
	
	public String getRateString();
	
	public void init(Selectors selector,SpiderFactory facotry,BreakPoint bp);
}
