package com.mm.data;

import java.util.Set;

import com.mm.data.struct.Selector;
import com.mm.db.DataBase;
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
	
//	public final static String fname = DataBase.getString("fname"),
//			  uname = DataBase.getString("uname"),
//			  hfname = DataBase.getString("hname"),
//			  tname = DataBase.getString("tname"),
//			  iname = DataBase.getString("iname"),
//			  ename = DataBase.getString("error"),
//			  mark = "ကကက";
	public final static String fname = "first.txt",
			  uname = "url.txt",
			  hfname = "html",
			  tname = "type.txt",
			  iname = "info.txt",
			  ename = "error.txt",
			  mark = "ကကက";
	
	public BreakPoint getBreakPoint();
	
	public void setBreakPoint(BreakPoint breakpoint);
	
	public void data() throws Exception ;
	
	public void setFactory(SpiderFactory factory);
	
	public void setSelector(Selector selector);
	
	public Selector getSelector();
	
	public void setName(String name);
	
	public void setNums(int nums);
	
	public void setError(Set<String> error);

}
