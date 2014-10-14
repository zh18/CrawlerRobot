package com.mm.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.mm.data.struct.Element;
import com.mm.data.struct.Selector;
import com.mm.db.DataBase;
import com.mm.spider.ISpider;
import com.mm.spider.SpiderFactory;
import com.mm.stop.BreakPoint;
import com.mm.util.SystemUtil;

public abstract class FBase implements FormatterBase {
	
	public static final String INFO="info";
	
	protected String name,process;
	protected static final String iname=DataBase.getString("iname");
	protected static final String hfname=DataBase.getString("hname");
	protected static final String tname = DataBase.getString("tname");
	protected static final String mark = "ကကက";
	protected BreakPoint breakpoint;
	protected SpiderFactory factory;
	protected ISpider spider;
	protected Document doc = null;
	protected Elements elist = null;
	protected Selector selector = null;
	
	
	// 此2方法需要从selector中获取，selector真是个大方法了.
	public Element getMessage(String content) {
		String html = getHtml(content);
		Document doc = Jsoup.parse(html);
		String url = getUrl(content);
		String murl = getMurl(url);
		String price = getPrice(doc,selector.getPrice());
		String type = getType(doc,selector.getType());
		String name = getName(doc,selector.getTitle());
		List<String> imgs = getImgs(doc, selector.getImgs());
		if (null == type || "".equals(type)) return null;
		if (imgs.size() == 0) return null;
		return new Element(url,murl,price,name,imgs,type);
	}


	public Element getMessage(String content, String path){
		String html = getHtml(content);
		Document doc = Jsoup.parse(html);
		String url = getUrl(content);
		String murl = getMurl(url);
		String name = getName(doc,"h1.productTitle");
		String price = null;    //getPrice(html);
		String id = url.substring(url.lastIndexOf("/")+1, url.length());
		String type = getType(id,path+"type.txt");
		List<String> imgs = getImgs(doc, "img#mainImage");
		if (type.equals("")) return null;
		if (imgs.size() == 0) return null;
		if (null == name || "".equals(name)) return null;
		if (name.indexOf("script") != -1 || name.indexOf("ml>") != -1) return null;
		if (null == type || "".equals(type.trim())) return null;
		return new Element(url,murl,price,name,imgs,type);
	}

	
	public FBase(){
		
	}

	public String getUrl(String content){
		String lines [] = content.split("\n");
		String temp = "";
		for(String s:lines){
			if (s.indexOf("ကကက") != -1){
				temp = s;
				break;
			}
		}
		return temp.substring(3, temp.length()-3);
	}
	
	protected String getMurl(String url) { return "";}
	
	public String getName(Document doc,String cssQuery) {
		elist = doc.select(cssQuery);
		if (elist.size() == 0) return "";
		String name = elist.text();
		name = name.trim();
		return name;
	}
	
	public String getType(Document doc,String cssQuery){
		elist = doc.select(cssQuery);
		String temp = "";
		for(org.jsoup.nodes.Element e:elist){
			if (e.text().indexOf("(") != -1 || e.text().indexOf(">") != -1) continue;
			temp = temp + e.text() + "က";
		}
		return temp;
	}
	
	public List<String> getImgs(Document doc,String cssQuery){
		elist = doc.select(cssQuery);
		List<String> result = new ArrayList<String>();
		for(org.jsoup.nodes.Element e:elist){
			result.add(e.attr("src"));
		}
		return result;
	}
	
	public String getHtml(String content){
		String temp[] = content.split("\n");
		StringBuilder sb = new StringBuilder("");
		for(int i=0;i<temp.length;i++){
			if (temp[i].indexOf("ကကက") != -1)  // have this line 
				continue;
			if (temp[i].indexOf("ကက") != -1)    // have this json line 
				return sb.toString();
			sb.append(temp[i]+"\n");
		}
		return sb.toString();
	}

	public String getJson(String content){
		String temp[] = content.split("\n");
		StringBuilder sb = new StringBuilder();
		int i = 0;
		for(;i<temp.length;i++) {
			if (temp[i].startsWith("ကကh"))   // don't have this line ကကhttp://
				break;
		}
		for(i++;i<temp.length;i++){
			sb.append(temp[i]);
		}
		return sb.toString();
	}
	
	public String getPrice(Document doc,String cssQuery){
		elist = doc.select(cssQuery);
	 	if (elist.size() == 0) return "0";
		return elist.text();
	}
	
	public String getType(String id,String path) {
		BufferedReader br = null;
		String line = "";
		String temp = "";
		try {
			br = new BufferedReader(new FileReader(path+tname));
			while ((line = br.readLine()) != null){
				if (line.indexOf(id) != -1){
					temp = line;
					break;
				}
			}
			br.close();
		}catch(Exception e){}
		if ("".equals(temp)) return "";
		temp = temp.substring(temp.indexOf("ကက"), temp.length());
		return temp;
	}
	
	// over write interface idata method
	public BreakPoint getBreakPoint(){
		return breakpoint;
	}
	
	public void setBreakPoint(BreakPoint breakpoint){
		this.breakpoint = breakpoint;
	}
	
	// 多线程
	public void data()  throws Exception {
		makeinfo(0);
		done();
	}
	
	protected final void done(){
		breakpoint.recover(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()).toString(),
				name, DONE, "0");
	} 
	
	private synchronized void makeinfo(int rate) throws IOException {
		process = INFO;
		boolean newfile = rate==0;
		String path = selector.getSavepath()+hfname;
		File [] files = new File(path).listFiles();
		BufferedReader br = null;
		String line = "";
		StringBuffer sb = new StringBuffer();
		for(File f:files){
			br = new BufferedReader(new FileReader(f));
			while((line = br.readLine()) != null){
				if(line.indexOf(mark) != -1){
					SystemUtil.appendFile(selector.getSavepath()+iname,
							selector.getClassify().equals("#")?
									getMessage(sb.toString()).toString():
										getMessage(sb.toString(),selector.getSavepath()).toString()
							, newfile);
					newfile = false;
					sb = new StringBuffer();
				}
				if(line.trim().equals("")) continue;
				sb.append(line);
			}
		}
	}
	
	
	
	public void setFactory(SpiderFactory factory){
		this.factory = factory;
		spider = factory.getSpider();
	}
	
	public void setSelector(Selector selector){
		this.selector = selector;
	}
	
	public Selector getSelector(){
		return selector;
	}
	
	public void setName(String name){
		this.name = name;
	}
}
