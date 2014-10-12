package scheme;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.mm.data.SuperData;
import com.mm.util.SystemUtil;

public class Yhd extends SuperData {
	private int getTotal(String url){

		html = spider.spider(url);
		if (html == null) {
			error.add(url);
			return 1;
		}
		doc = Jsoup.parse(html);
		Elements elist = doc.select(selector.getNext());
		Element e = null;
		try {
			e = elist.get(0);
		} catch (Exception e1) {
			return -1;
		}
		String result = e.text();
		// result =
		// result.substring(result.indexOf("span>")+6,result.indexOf("<input"));
		result = result.substring(result.indexOf("/") + 1, result.length());
		return Integer.parseInt(result);
	}
	
	
	protected void pro0(int rate) throws IOException {
		process = PRODUCT;
		List<String> urls = SystemUtil.readLine(selector.getSavepath()+fname);
		if(urls.size() == 0) return;
		if(rate>urls.size()) return ;
		boolean newfile = rate==0;
		String url = null;
		
		for(int i=rate;i<urls.size();i++){
			url = urls.get(i);
			breakpoint.setTotla(urls.size());
			breakpoint.recover(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()).toString(), 
					name, process, String.valueOf(i));
			
			int total = getTotal(url);
			for(int page=1;page<=total;page++) {
				for(String s:getPageProductUrls(url,page)) {
					SystemUtil.appendFile(selector.getSavepath()+uname, s, newfile);
					newfile = false;
				}
			}
		}
		
	}
	
	
	private List<String> getPageProductUrls(String path,int page) {
		if (null == path || "".equals(path))
			return null;
		List<String> result = new ArrayList<String>();
		String html36 = spider.spider(path);
		String html72 = spider.spider(this.getLast36Url(path, page));
		try {
			result.addAll(this.getYhdFirst36(html36));
			result.addAll(this.getYhdLast36(html72));
		} catch (NullPointerException e) {
			return null;
		}
		return result;
	}
	
	
	
	
	private String getLast36Url(String url, int page) {
		String first = url.substring(0, url.indexOf("com/") + 4);
		String last = url.substring(url.indexOf("com/") + 4, url.length());
		try {
			last = last.substring(0, last.indexOf("#"));
		}catch(Exception e){}
		String result = first
				+ "searchPage/"
				+ last
				+ "b/a-s1-v0-p"
				+ page
				+ "-price-d0-f0-m1-rt0-pid-mid0-k/?isGetMoreProducts=1&moreProductsDefaultTemplate=0";
		return result;
	}
	
	private List<String> getYhdFirst36(String html) {
		if (null == html || "".equals(html))
			return null;
		List<String> result = new ArrayList<String>();
		Document doc = Jsoup.parse(html);
		Elements elist = null;
		Element e = null;
		try {
			elist = doc.select("input#jsonValue");
			e = elist.get(0);
		} catch (Exception e1) {
			return null;
		}
		String json = e.attr("value");
		json = json.substring(json.indexOf("ld7\":\"") + 6,
				json.lastIndexOf("\"s") - 4);
		String temp[] = json.split(",");
		for (String s : temp) {
			result.add(this.inCloths(s, "http://item.yhd.com/item/"));
		}
		return result;
	}
	
	private List<String> getYhdLast36(String html) {
		if (null == html || "".equals(html))
			return null;
		List<String> result = new ArrayList<String>();
		html = html.substring(html.indexOf("moreTopProductsId")
				+ "moreTopProductsId".length(), html.length());
		html = html.substring(0, html.indexOf("\";"));
		html = html.substring(html.indexOf("\"") + 1, html.lastIndexOf("\\"));
		for (String s : html.split(",")) {
			result.add(this.inCloths(s, "http://item.yhd.com/item/"));
		}
		return result;
	}
	
	private String inCloths(String old, String root) {
		String result = old.replaceAll("_\\d", "");
		return root + result;
	}

	private List<String> getLittleCata(String url, String rootpath) {
		if (null == url || "".equals(url) || null == rootpath)
			return null;

		String first = url.substring(0, url.indexOf("com/") + 4);
		String last = url.substring(url.indexOf("com/") + 4, url.length());
		// http://list.yhd.com/searchPage/c33618-0-60364/b/a-s1-v0-p1-price-d0-f0-m1-rt0-pid-mid0-k/?type=moreBrand
		String jsonResult = first + "searchPage/" + last
				+ "b/a-s1-v0-p1-price-d0-f0-m1-rt0-pid-mid0-k/?type=moreBrand";
		// Document docc = Jsoup.parse(url);
		// Elements elist = docc.select("div.multiple_choice a.more");
		// Element ee = elist.get(0);
		// String jsonResult = ee.attr("url") + "?type=moreBrand";

		String html = spider.spider(jsonResult);

		List<String> result = new ArrayList<String>();
		// code from here is from yue
		html = html.replaceAll("\\\\r|\\\\n|\\\\t", "");
		// html = html.replaceAll("\\\\n", "");
		// html = html.replaceAll("\\\\t", "");
		html = html.replaceAll("\\\\\"", "\"");
		html = html.replaceAll("\\\\/", "/");
		html = html.substring(html.indexOf("<"), html.lastIndexOf(">") + 1);

		Document doc = Jsoup.parse(html);
		Elements elements = doc.select("#brandListAll .clearfix a");
		for (Element e : elements)
			result.add(e.attr("url"));
		System.out.println(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
				.format(new Date()) + ", getting brands ");
		return result;
	}
}
