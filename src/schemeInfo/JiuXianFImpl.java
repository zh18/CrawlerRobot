package schemeInfo;

import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.mm.data.FBase;
import com.mm.data.struct.Element;

public class JiuXianFImpl extends FBase {

	Document doc = null;
	Elements elist = null;
	
	
	public Element getMessage(String content) {
		String html = getHtml(content);
		Document doc = Jsoup.parse(html);
		String url = getUrl(content);
		String murl = getMurl(url);
		String name = getName(doc,"div.depict-name h3");
		String price = getPrice(url);
		String type = getType(doc,"div.detail-guide > a");
		List<String> imgs = getImgs(doc, "div.show-list-con li a img");
		return new Element(url,murl,price,name,imgs,type);
	}

	@Override
	public Element getMessage(String html, String path) {
		// TODO Auto-generated method stub
		return null;
	}
	// http://m.jiuxian.com/goods/view/
	public String getMurl(String url){
		String id = url.substring(url.lastIndexOf("-")+1, url.lastIndexOf(".html"));
		return "http://m.jiuxian.com/goods/view/"+id;
	}
	//http://act.jiuxian.com/act/selectPricebypids.htm?ids=
	public String getPrice(String url){
		String id = url.substring(url.lastIndexOf("-")+1,url.lastIndexOf(".html"));
		String json = spider.spider("http://act.jiuxian.com/act/selectPricebypids.htm?ids="+id);
		try {
			json = json.substring(json.lastIndexOf("np")+4, json.length());
			json = json.substring(0, json.indexOf("}"));
			System.out.println(json);
		}catch(Exception e){return "0";}
		return json;
	}

}
