package com.mm.spider;

import java.util.Collection;

import com.mm.db.DataBase;

public interface SpiderFactory {

	public String ERROR = DataBase.getString("error");
	
	public ISpider getSpider();

	public ISpider getSpider(String name);
	
	public void writeError(String path,String url);
	
	public void push(String str);
	
	public String pop();
	
	public ISpider uplevel();
	
	public Collection<String> reList();
	
	public int getlevel();
	
	public void resetlevel();
	
}
