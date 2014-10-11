package schemeInfo;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.mm.data.FBase;
import com.mm.data.struct.Element;

public class YhdFImpl extends FBase {
	
	Elements elist = null;
	Document doc = null;
	
	public String getName(Document doc) {
		elist = doc.select("div.prodname h1#productMainName");
		if (elist.size() == 0) return "";
		return elist.get(0).text();
	}

	
	public String getUrl(String html) {
		String lines[] = html.split("\n");
		for(String s:lines){
			if (s.indexOf("ကကက") != -1){
				return s.substring(3, s.length()-3);
			}
		}
		return "";
	}

	
	public String getMurl(String url) {
		//http://m.yhd.com/item/1012103
		//http://item.yhd.com/item/29027152
		if (null == url || "".equals(url)) return "";
		String result = url.substring(url.lastIndexOf("/")+1, url.length());
		return "http://m.yhd.com/item/"+result;
	}

	
	public List<String> getImgs(Document doc) {
		elist = doc.select("ul.imgtab_con li img");
		List<String> result = new ArrayList<String>();
		for(org.jsoup.nodes.Element e:elist){
			if (e.attr("src").indexOf("http://") != -1)
				result.add(e.attr("src"));
		}
		return result;
	}

	
	public String getType(Document doc) {
		elist = doc.select("div[data-tpa=DETAIL_CRUMBBOX] a");
		StringBuilder sb = new StringBuilder();
		for(org.jsoup.nodes.Element e:elist){
			sb.append(e.text()+"က");
		}
		return sb.toString();
	}

	
	//http://gps.yihaodian.com/restful/detail?mcsite=1&provinceId=2&pmId=6628547&callback=jsonp1411532648588
	public String getPrice(String url){
		String result = url.substring(url.lastIndexOf("/")+1, url.length());
		String line = "http://gps.yihaodian.com/restful/detail?mcsite=1&provinceId=2&pmId="+result+"&callback=jsonp"+System.currentTimeMillis();
		String json = spider.spider(line);
		if (null == json) json = spider.spider(line);
		if (null == json) return "0";
		String price = null;
		try {
			price = json.substring(json.indexOf("currentPrice")+14, json.length());
			price = price.substring(0, price.indexOf(","));
		}catch(Exception e){return "0";}
		return price;
	}
	
	
	public synchronized Element getMessage(String content) {
		String html = getHtml(content);
//		String json = DDFImpl.getJson(content);
		doc = Jsoup.parse(html);
		String url = this.getUrl(content);
		String murl = this.getMurl(url);
		List<String> imgs = this.getImgs(doc);
		String type = this.getType(doc);
		String price = this.getPrice(url);
		String name = this.getName(doc);
		if (null == type || "".equals(type)) return null;
		if (null == name || "".equals(name)) return null;
		if (imgs.size() == 0) return null;
		return new Element(url,murl,price,name,imgs,type);
	}


	@Override
	public Element getMessage(String html, String path) {
		// TODO Auto-generated method stub
		return null;
	}

}
