package com.mm.data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.mm.data.struct.Selector;
import com.mm.data.struct.Type;
import com.mm.db.DataBase;
import com.mm.spider.ISpider;
import com.mm.spider.SpiderFactory;
import com.mm.stop.BreakPoint;
import com.mm.util.SystemUtil;

public class SuperData implements Idata{

	protected ISpider spider ;
	protected SpiderFactory factory;
	protected String name,process;
	protected BreakPoint breakpoint;
	protected Selector selector;
	
	protected BufferedReader br = null;
	protected BufferedWriter bw = null;
	protected Document doc = null;
	protected Elements elist = null;
	protected String html,line;
	protected Set<String> error;
	protected int rate;
	
	protected final static String fname = DataBase.getString("fname"),
								  uname = DataBase.getString("uname"),
								  hfname = DataBase.getString("hname"),
								  tname = DataBase.getString("tname"),
								  mark = "ကကက";
	
	
	public SuperData(){
		name = this.getClass().getName();
		rate = 0;
		error = new HashSet<String>();
	}
	
	protected String getNextLink(String html,String basepath,String cssQuery) {
		doc = Jsoup.parse(html);
		elist = doc.select(cssQuery);
		
		String nextlink = elist.attr("href");
		if ("".equals(nextlink)) return null;
		return basepath+nextlink;
	}
	
	public void data() throws Exception {
		if (null == breakpoint) {
			this.breakpoint = new BreakPoint("","",name,"#","0");
			fromwhere(null, "0");
		}
		else {
			this.breakpoint = DataBase.getProtection(name);
			if(null == breakpoint) 
				this.breakpoint = new BreakPoint("","",name,"#","0");
			fromwhere(breakpoint.getPname(),breakpoint.getRate());
		}
			
	}

	private void fromwhere(String process,String rate) throws Exception {
		if (null == process || process.equals("#"))
			process = FIRST;
		switch(process){
		case FIRST:
			first0();
			rate = "0";
		case PRODUCT:
			pro0(Integer.parseInt(rate));
		case DOWNLOAD:
			download(Integer.parseInt(rate));
			done();
		}
	}
	
	protected void first0(){
		process = FIRST;
		
		List<String> temp2 = new ArrayList<String>(); // 把根网页中 含有selects的信息提哪家到需要爬取的网页中
		List<List<String>> temp = new ArrayList<List<String>>(); // 获取循环爬取的网页
		temp.add(selector.getRootpath());
		List<String> add = new ArrayList<String>();
		for (int i = 0; i < selector.getFselects().length; i++) {   
			// 当有第一级为根节点时，加入add列表中，再第二级加入爬取下一级子节点，如果不存再，则继续加入下一节点
			temp2 = new ArrayList<String>();
			breakpoint.setTotla(selector.getFselects().length);
			for (String url : temp.get(i)) {
				// i use this to stop this thread
				breakpoint.recover(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()).toString(), 
						name, process, String.valueOf(i));
				
				html = spider.spider(url);
				if (html == null) {
					continue;
				}
				doc = Jsoup.parse(html);
				elist = doc.select(selector.getFselects()[i]);
				if (elist.size() == 0) {
					add.add(url);
					continue;
				} else {
					for (Element e : elist) {
						if (!selector.getFbase().equals("#")) 
							line = selector.getFbase()+e.attr("href");
						else
							line = e.attr("href");
						temp2.add(line);
					}
				}
			}
			temp2.addAll(add);
			if (temp2.size() != 0) {
				temp.add(temp2);
				add = new ArrayList<String>();
			}
		}
		SystemUtil.writeColl(temp.get(temp.size()-1), selector.getSavepath()+fname);
	}
	
	protected void pro0(int rate) throws IOException {
		process = PRODUCT;
		//查看是否需要加入type文件
		boolean isType = isTypes();
		String typeq=isType?selector.getClassify():"";
		String typetemp = "";
		
		String url = null;
		List<String> urls = SystemUtil.readLine(selector.getSavepath()+fname);
		if (rate > urls.size()) return;
		String line = "";
		boolean newfile = rate==0;
		for(int i=rate;i<urls.size();i++){
			url = urls.get(i);
			breakpoint.setTotla(urls.size());
			breakpoint.recover(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()).toString(), 
					name, process, String.valueOf(i));
			do {
				try {
					html = spider.spider(url);
				}catch(Exception e){
					error.add(url);
					continue;
				}
				if (null == html) {
					error.add(url);
					continue;
				}
				doc = Jsoup.parse(html);
				elist = doc.select(selector.getProducts());
				
				if(isType){
					typetemp = getType(doc, isType);
				}
				
				for(Element e:elist){
					if (!selector.getPbase().equals("#"))
						line = selector.getPbase()+e.attr("href");
					else 
						line = e.attr("href");
					SystemUtil.appendFile(selector.getSavepath()+uname, line,newfile);
					if (isType) {
						SystemUtil.appendFile(selector.getSavepath()+tname,new Type(typetemp,line).toString(),newfile);
					}
					newfile = false;
				}
			}while ((url = getNextLink(html,selector.getNbase().equals("#")?"":selector.getNbase(),selector.getNext())) != null);
		}
	}
	
	protected final boolean isTypes(){
		return !selector.getClassify().equals("#");
	}
	
	protected String getType(Document doc,boolean isType){
		if(!isType) return "";
		Elements elist = doc.select(selector.getClassify());
		StringBuffer bs = new StringBuffer();
		String temp = null;
		for(Element e:elist){
			temp = e.text();
			if(temp.indexOf(">") != -1 || temp.indexOf("<") != -1) continue;
			bs.append(temp+"က");
		}
		return bs.toString();
	}
	
	protected final void download(int rate) throws IOException{
		process = DOWNLOAD;
		List<String> targetlist = SystemUtil.readLine(selector.getSavepath()+uname);
		int total = targetlist.size();
		if (rate >= total) return ;
		String url = "";
		File file = new File(selector.getSavepath()+hfname);
		if (!file.exists()) file.mkdir();
		int record=rate%10000;  // if rate = 0 so record was 0  
		bw = new BufferedWriter(new FileWriter(selector.getSavepath()+hfname+File.separator+(record*10000+"~"+(record+1)*10000)));
		for(int i=rate;i<targetlist.size(); ++i) {
			breakpoint.setTotla(targetlist.size());
			// i use this to stop this thread , not better
			breakpoint.recover(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()).toString(), 
					name, process, String.valueOf(i));
			url = targetlist.get(i);
			url = url.trim();
			if ("".equals(url)) continue;
			try {
				html = spider.spider(url);
			}catch(Exception e){
				error.add(url);
				continue;
			}
			if (null == html || "".equals(html)) {
				error.add(url);
				continue;
			}
			// ### url ### 在前的方式
			bw.write("\n"+mark+targetlist.get(i)+mark+"\n");
			bw.write(html);
			if (i!= 0 && i%10000 == 0)   {  //每一万个变成一个新文件
				file = new File(selector.getSavepath()+hfname+File.separator+((++record)*10000+"~"+(record+1)*10000));
				bw.close();
				bw = new BufferedWriter(new FileWriter(file));
			}
		}
		bw.close();
		if(error.size() != 0){
			SystemUtil.writeColl(error, new File(hfname).getParent()+File.separator+"error.txt");
		}
		
	}
	
	
	protected final void done(){
		breakpoint.recover(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()).toString(),
				name, DONE, "0");
	}
	
	
	public void setName(String name){
		this.name = name;
	}
	// ---------------------------------  task 反射时需要的信息
	public final void setFactory(SpiderFactory factory){
		this.factory = factory;
		spider = factory.getSpider();
	}
	public final void setSelector(Selector selector){
		this.selector = selector;
	}
	
	// ---------------------------------
	public final BreakPoint getBreakPoint(){
		if(this.breakpoint == null) this.breakpoint = new BreakPoint("","",name,"#","0");
		return breakpoint;
	}
	public final void setBreakPoint(BreakPoint breakpoint){
		this.breakpoint = breakpoint;
	}
	
	
	
	//----------------------------------check uname fname 是否存在
	private boolean check(String part){
		switch(part){
		case PRODUCT:
			return check0(selector.getSavepath()+fname);
		case DOWNLOAD:
			return check0(selector.getSavepath()+uname);
		}
		return false;
	}
	
	private boolean check0(String name){
		File file = new File(name);
		if (file.exists() && SystemUtil.readLine(name).size() > 0)
			return true;
		return false;
	}
}
