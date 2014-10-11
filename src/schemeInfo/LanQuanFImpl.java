package schemeInfo;

import java.util.List;

import org.apache.log4j.net.SyslogAppender;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.mm.data.FBase;
import com.mm.data.struct.Element;

public class LanQuanFImpl extends FBase {


	Document doc = null;
	Elements elist = null;
	
	public Element getMessage(String content) {
		String html = getHtml(content);
		Document doc = Jsoup.parse(html);
		String url = getUrl(content);
		if (url.indexOf("search") != -1) return null;
		String murl = url;
		String name = getName(doc,"h1#bookTitle_CH","h1#bookTitle_EN");
		String price = getPrice(url);
		String type = getType(doc,"div.moreDetail dd.topDetail a");
		List<String> imgs = getImgs(doc, "div#productImg img");
		if (imgs.size() == 0) return null;
		return new Element(url,murl,price,name,imgs,type);
	}

	public Element getMessage(String html, String path) {
		return null;
	}

	public String getName(Document doc,String cssQuery,String cssQuery1){
		elist = doc.select(cssQuery);
		if("".equals(elist.text())) 
			elist = doc.select(cssQuery1);
		return elist.text();
	}
	// http://product.51eng.com/js/djs/notrack/productstatus.aspx?pid=9780439737661
	// http://product.51eng.com/js/djs/notrack/productstatus.aspx?pid=9780439737661
	//9780634001826
	public String getPrice(String url){
		String [] lines = url.split("/");
		String id = "";
		boolean flag = false;
		for(String s:lines){
			if (s.matches("^[0-9]*$") && !"".equals(s)){
				id = s.trim();
				flag = !flag;
				break;
			}
		}
		System.out.println(url);
		if (!flag) return "0";
		String jsonurl = "http://product.51eng.com/js/djs/notrack/productstatus.aspx?pid="+id;
		String json = spider.spider(jsonurl);
		if (null == json || "".equals(json)) return "0";
		String temp = null;
		try {
			temp = json.substring(json.indexOf("OurPrice:")+9, json.length());
			temp = temp.substring(0, temp.indexOf(","));
		}catch(Exception e){return "0";}
		return temp;
	}
	
}
