package com.mm.spider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Stack;

import com.mm.logger.Log;
import com.mm.util.SystemUtil;

public class SpiderFactoryImpl implements SpiderFactory {
	
	private Stack<String> url = null;
	private int level = 0;
	private ISpider spider;
	
	public SpiderFactoryImpl(){
		url = new Stack<String>();
		level = 0;
		spider = switchSpider();
	}
	
	public ISpider getSpider() {
		return switchSpider();
	}
	
	private ISpider switchSpider(){
		switch(level){
		case 0:
			spider = new Spider();
			break;
		case 1:
			spider = new SpiderWithHeader();
			break;
		case 2:
			spider = new SpiderSleep200();
			break;
		default:
			spider = new SpiderSleep200();
		}
		return spider;
	}
	
	public void push (String str){
		url.push(str);
	}
	
	public String pop(){
		return url.pop();
	}
	
	public Collection<String> reList(){
		Collection<String> result = new ArrayList<String>();
		while(!url.isEmpty()){
			result.add(url.pop());
		}
		return result;
	}
	
	public boolean change(){
		if (level <= 3) {
			++level;
			return true;
		}
		return false;
	}

	public void writeError(String path,String url){
		try {
			SystemUtil.appendFile(path+ERROR, url);
		}catch(Exception e){
			Log.logger.warn("write error", e);
		}
	}
	
	public ISpider uplevel(){
		if (change())
			return switchSpider();
		return null;
	}
	
	public int getlevel(){
		return level;
	}
	public void resetlevel(){
		level = 0;
	}
	
}
