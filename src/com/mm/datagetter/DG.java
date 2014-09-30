package com.mm.datagetter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.mm.db.DataBase;
import com.mm.logger.Log;
import com.mm.spider.ISpider;
import com.mm.spider.Spider;
import com.mm.spider.SpiderFactory;
import com.mm.spider.SpiderNoDataException;
import com.mm.stop.BreakPoint;
import com.mm.util.SystemUtil;

public abstract class DG implements IDG{

	private String mark;
	private ISpider spider;
	private String done=NOT_DONE;
	private SpiderFactory spiderfactory;
	private String currentHtmlFile=null;
	private String wname,pname,rate;
	private int total;
	protected static String path,uname,fname,hfname;
	protected Selectors selector;
	protected BreakPoint bp;
	
	/**
	 * temp var
	 */
	private String html;
	private Document doc;
	private Elements elist;
	private BufferedWriter bw;
	
	public DG(){
		wname = this.getClass().getSimpleName();
		if (null == path || "".equals(path))
			path = DataBase.getString("root");
		if (null == uname || "".equals(uname))
			uname = DataBase.getString("uname");
		if (null == fname || "".equals(fname))
			fname = DataBase.getString("fname");
		if (null == hfname || "".equals(hfname))
			hfname = DataBase.getString("hname");
	}

	public final void init(Selectors selector,SpiderFactory factory,BreakPoint bp){
		setSelector(selector);
		setBreakPoint(bp);
		setSpiderfactory(factory);
	}
	
	public final void go(String part,boolean bp){
		if(bp && null != this.bp) {
			setBreakPoint(this.bp);
			go(part,Integer.parseInt(rate));
		}
		else {
			go(part,0);
		}
	}
	
	
	/**
	 * 按照part给定的部分进行下载，具体参见{@link IDG}
	 */
	private final void go(String part,int rate) {
		if (null == part) 
			part = ALL;
		try {
			switch(part){
			case FIRST:
				first();
				break;
			case PRO:
				if (!check(part))
					go(part,rate);
				product(rate);
				break;
			case DOWN:
				if (!check(part))
					go(part,rate);
				download(rate);
				break;
			default:
				first();
				product(rate);
				download(rate);
			}
		}
		catch(Exception e){
			Log.logger.error("go "+part+" error", e);
		}
	}

	protected final void download(int rate) throws IOException,FileNotFoundException {
		List<String> targetlist = SystemUtil.readLine(path+uname);
		pname = DOWN;
		done = NOT_DONE;
		int total = targetlist.size();
		if (rate >= total) return ;
		String url = "";
		File file = new File(path+File.separator+hfname);
		if (!file.exists()) file.mkdir();
		int record=0;
		bw = new BufferedWriter(new FileWriter(path+hfname+File.separator+(record*10000+"~"+(record+1)*10000)));
		for(int i=rate;i<targetlist.size(); ++i) {
			url = targetlist.get(i);
			url = url.trim();
			System.out.println(url.trim());
			if ("".equals(url)) continue;
			html = spiderGuest(url);
			setRateString(i, total);
			// ### url ### 在前的方式
			bw.write("\n"+mark+targetlist.get(i)+mark+"\n");
			bw.write(html);
			if (i%10000 == 0)   {  //每一万个变成一个新文件
				file = new File(path+hfname+File.separator+((++record)*10000+"~"+(record+1)*10000));
				bw.close();
				bw = new BufferedWriter(new FileWriter(file));
			}
		}
		done = DONE;
		bw.close();
	}
	/**
	 * 获取下一页的链接
	 * @param html
	 * @return nextlink
	 */
	protected String getNextlink(String html,String base,String next) {
		doc = Jsoup.parse(html);
		elist = doc.select(next);
		
		String nextlink = elist.attr("href");
		if ("".equals(nextlink)) return null;
		return base+nextlink;
	}
	
	/**
	 * 获取first 列表页
	 * @param root
	 * @param selector
	 * @return list
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	protected List getFirst0(Selectors selector) throws IOException,FileNotFoundException{
		pname = FIRST;
		done = NOT_DONE;
		List<String> temp2 = new ArrayList<String>(); // 把根网页中 含有selects的信息提哪家到需要爬取的网页中
		temp2.add(selector.getRootpath());
		List<List<String>> temp = new ArrayList<List<String>>(); // 获取循环爬取的网页
		temp.add(temp2);
		String line = "";
		List<String> add = new ArrayList<String>();
		for (int i = 0; i < selector.getFselects().length; i++) {   
			// 当有第一级为根节点时，加入add列表中，再第二级加入爬取下一级子节点，如果不存再，则继续加入下一节点
			temp2 = new ArrayList<String>();
			setRateString(i, selector.getFselects().length);
			for (String url : temp.get(i)) {
				html = spiderGuest(url);
				doc = Jsoup.parse(html);
				elist = doc.select(selector.getFselects()[i]);
				if (elist.size() == 0) {
					add.add(url);
					continue;
				} else {
					for (Element e : elist) {
						if (selector.isFbase()) 
							line = selector.getBasepath()+e.attr("href");
						else
							line = e.attr("href");
						try {
							line = line.substring(0, line.indexOf("html")+4);
						}catch(Exception e5){}
						temp2.add(line);
					}
				}
			}
			if (temp2.size() != 0) {
				temp2.addAll(add);
				temp.add(temp2);
				add = new ArrayList<String>();
			}
		}
		done = DONE;
		return temp.get(temp.size()-1);
	}

	
	public final String spiderGuest(String url) {
		
		String html = spider.spider(url);
		int times = 0;
		while(true){
			if (null != html && !"".equals(html)) return html;
			if (times%5==0) { 
				if((spider = spiderfactory.uplevel()) == null) {
					spiderfactory.writeError(path+wname, url);
					if (spiderfactory.getlevel() == 5) {
						spiderfactory.resetlevel();
						throw new SpiderNoDataException();
					}
				}
			}
			else {
				times ++ ; 
				spiderfactory.push(url);
			}
			html = spiderGuest(url);
		}
	}
	
	/**
	 * 
	 * 读取urls中的内容，按照selector，从节点r开始下载
	 * 
	 * @param urls 链接list
	 * @param selector 选择类
	 * @param r rate
	 * @return set product link set 
	 * 			<tt> null </tt> r>urls.length;
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	protected Set getPros0(int rate) throws IOException,FileNotFoundException {
		pname = PRO; 
		done = NOT_DONE;
		String url = null;
		Set result = new HashSet();
		List<String> urls = SystemUtil.readLine(path+fname);
		if (rate > urls.size()) return null;
		this.rate = String.valueOf(rate);
		int total = urls.size();
		String line = "";
		if(rate > total) return null;
		for(int i=rate;i<urls.size();i++){
			url = urls.get(i);
			setRateString(i,total);
			do {
				html = spiderGuest(url);
				doc = Jsoup.parse(html);
				elist = doc.select(selector.getProducts());
				for(Element e:elist){
					
					if (selector.isPbase())
						line = selector.getBasepath()+e.attr("href");
					else 
						line = e.attr("href");
					try {
						line = line.substring(0, line.indexOf("html")+4);
					}catch(Exception e2){}
					result.add(line);
					SystemUtil.appendFile(path+uname, line);
				}
			}while ((url = getNextlink(html,selector.isNbase()?selector.getBasepath():"",selector.getNext())) != null);
		}
		done = DONE;
		return result;
	}
	
	/**
	 * 读取断点并从断点处开始执行
	 */
	public final void setBreakPoint(BreakPoint bp){
		if (null == bp) {
			done = "0";
			pname = "never run";
			rate = "0";
			return ;
		}
		done = bp.getDone();
		pname = bp.getPname();
		rate = bp.getRate();
	}

	protected final void first() {
		try {
			List list = getFirst0(selector);
			SystemUtil.writeColl(list, path+fname);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	protected final Set product(int rate) {
		try {
			return this.getPros0(rate);
		}catch(Exception e){
			Log.logger.error(PRO+" "+wname+" error",e);
		}
		return null;
	}
	
	
	
	

	
	
	public final String loggerhelper(String message) {
		return "["+new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date())+"]"
				+getWname()+" "+getPname()+" "+message+" @ "+getRate();
	}
	
	public final String getWname(){
		return wname;
	}
	
	public final String getPname(){ 
		return pname;
	}
	
	public final String getRate(){
		return rate;
	}
	
	public final String getDone(){
		return done;
	}
	
	public final String getRateString(){
		return String.format("%.2f", (Integer.parseInt(rate) * 100 * 1.0 / total))+"% "+"N@"+rate+"/T@"+total;
	}

	public final void setRateString(int rate,int total){
		this.rate = String.valueOf(rate);
		this.total = total;
	}

	private boolean check(String part){
		switch(part){
		case PRO:
			return check0(path+fname);
		case DOWN:
			return check0(path+uname);
		}
		return false;
	}
	
	private boolean check0(String name){
		File file = new File(name);
		if (file.exists() && SystemUtil.readLine(name).size() > 0)
			return true;
		return false;
	}


	public ISpider getSpider() {
		return spider;
	}

	public void setSpider(ISpider spider) {
		this.spider = spider;
	}

	public Selectors getSelector() {
		return selector;
	}

	public SpiderFactory getSpiderfactory() {
		return spiderfactory;
	}

	public void setSpiderfactory(SpiderFactory factory) {
		this.spiderfactory = factory;
		this.spider = factory.getSpider();
	}

	public void setSelector(Selectors selector) {
		this.selector = selector;
	}

	public BreakPoint getBp() {
		return bp;
	}

	public void setBp(BreakPoint bp) {
		this.bp = bp;
	}

	public void setSelectors(Selectors selector){
		this.selector = selector;
	}
	
}
