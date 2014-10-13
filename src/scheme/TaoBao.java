package scheme;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import com.mm.data.SuperData;
import com.mm.data.struct.Type;
import com.mm.spider.Spider;
import com.mm.util.SystemUtil;

public class TaoBao extends SuperData{

	String jsonnum = "";
	
	protected void first0() {
		process = FIRST;
		List<String> urls = selector.getRootpath();
		List<String> result = new ArrayList<String>();
		String url = null;
		for(int i=0;i<urls.size();i++){
			url = urls.get(i);
			breakpoint.setTotla(urls.size());
			breakpoint.recover(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()).toString(), 
					name, process, String.valueOf(i));
			url = getJsonUrl(url, 0);
			List<String> temp = new ArrayList<String>();
			temp.add(url);
			result.addAll(rotine(temp, 3));
		}
		SystemUtil.writeColl(result, selector.getSavepath()+fname);
	}
	
	protected void pro0(int rate) throws IOException {
		process = PRODUCT;
		List<String> urls = SystemUtil.readLine(selector.getSavepath()+fname);
		if(urls.size()==0 || rate > urls.size()) return ;		
		String url = null;
		String brandtemp = null;
		int total = 0;
		boolean newfile = rate==0;
		for(int i=0;i<urls.size();i++){
			url = urls.get(i);
			
			breakpoint.setTotla(urls.size());
			breakpoint.recover(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()).toString(), 
					name, process, String.valueOf(i));
			
			String json = spider.spider(url);
			total = getTotal(json);
			brandtemp = getClassify(json);
			
			for(int page=0;page<total && page<10;page++){
				json = spider.spider(url);
				for(String s:listpros(json)){
					SystemUtil.appendFile(selector.getSavepath()+uname, s, newfile);
					SystemUtil.appendFile(selector.getSavepath()+tname, new Type(brandtemp,s).toString(), newfile);
					newfile = false;
				}
				url = getJsonUrl(url,page);
			}
		}
		
	}
	
	private List<String> listpros(String json) {
		String temp = json.substring(json.indexOf("itemList"),json.lastIndexOf("filterForm")-2);
		List<String> result = new ArrayList<String>();
		String lines[];
		lines = temp.split("\n");
		for (int i=0;i<lines.length;i++){
			if (lines[i].indexOf("href") != -1){
				lines[i] = lines[i].substring(9, lines[i].length()-2);
				if (lines[i].startsWith("http") && lines[i].indexOf("detail") == -1)
					if(lines[i].indexOf("item.taobao") != -1)
						result.add(lines[i]);
			}
		}
		return result;
	}
	
	//http://list.taobao.com/itemlist/market/baby.htm?cat=50005998&viewIndex=1&as=0&spm=a2106.2206569.0.0.kZUj0m&atype=b&style=grid&same_info=1&isnew=2&tid=0&_input_charset=utf-8
	private String getBase(String url){
		String result = url;
		try {
			result = result.substring(0, result.indexOf("?"));
		}catch(Exception e){
		}
		return result;
	}
	
	protected final String getId(String url){
		String result = url;
		try {
			result = result.substring(result.indexOf("cat=")+4);
			result = result.substring(0, result.indexOf("&"));
		}catch(Exception e){
		}
		return result;
	}
	
	//list.taobao.com/itemlist/market/baby.htm?json=on&cat=50005998&style=grid&pSize=95&callback=jsonp68
	private String getJsonUrl(String url,int page){
		String head = getBase(url);
		String id=getId(url);
		String spage=String.valueOf(page*95);
		
		return head+"?json=on&cat="+id+"&style=grid&pSize="+spage;
	}
	
	private int getTotal(String json){
		int result = 0;
		try {
			json = json.substring(json.indexOf("totalPage")+12);
			json = json.substring(0,json.indexOf(",")-2);
			result = Integer.parseInt(json);
		}catch(Exception e){
		}
		return result;
	}
	
	public String getClassify(String json){
		StringBuffer sb = new StringBuffer();
		try {
			json = json.substring(json.indexOf("marketInfo"),json.indexOf("options"));
			json.replaceAll(" ", "");
			String lines[] = json.split(",");
			for(String s:lines){
				if(s.indexOf("displayName")!=-1){
					s = s.replaceAll(" ", "");
					s = s.substring(s.indexOf("displayName")+12);
					s = s.substring(s.indexOf("\"")+1);
					s = s.substring(0, s.lastIndexOf("\""));
					if(s.indexOf(":")==-1 || s.indexOf("\"") == -1 || !s.trim().equals(""))
						sb.append(s+"က");
				}
			}
		}catch(Exception e){
			return "";
		}
		return sb.toString();
	}
	
	public List<String> getF(String url,String json){
		List<String> result = new ArrayList<String>();
		String base = getBase(url);
		try {
			json = json.substring(json.indexOf("catList"),json.indexOf("itemListSize"));
			json.replaceAll(" ", "");
			json.replaceAll("\\\\\n", "");
			String lines[] = json.split(",");
			for(String s:lines){
				if(s.indexOf("value")!=-1){
					s = s.replaceAll(" ", "");
					s = s.substring(s.indexOf("value")+7);
					s = s.substring(s.indexOf("\"")+1);
					s = s.substring(0, s.lastIndexOf("\""));
					if(s.indexOf(":")==-1 && s.indexOf("\"") == -1 && !s.trim().equals(""))
						result.add(base+"?json=on&cat="+s+"&style=gridpSize=95");
				}
			}
		}catch(Exception e){
			
		}
		return result;
	}
	
	private List<String> rotine(List<String> urls,int times){
		times--;
		List<String> result = new ArrayList<String>();
		String url = null;
		for(int i=0;i<urls.size();i++){
			url = urls.get(i);
			breakpoint.setTotla(urls.size());
			breakpoint.recover(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()).toString(), 
					name, process, String.valueOf(i));
			url = getJsonUrl(url, 0);
			html = spider.spider(url);
			if(html == null) {
				error.add(url);
				continue;
			}

			result.addAll(getF(url, html));
		}
		if(times==0) return result;
		return rotine(result,times);
	}
}
