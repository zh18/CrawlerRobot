package schemeInfo;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.mm.data.FBase;
import com.mm.data.struct.Element;

public class JiuMiFImpl extends FBase{
	

	Document doc = null;
	Elements elist = null;
	
	public Element getMessage(String content) {
		String html = getHtml(content);
		Document doc = Jsoup.parse(html);
		String url = getUrl(content);
		String murl = url;
		String price = getPrice(doc,"span#dfDrinksPrice");
		String type = getType(doc,"li.dr-links");
		String name = getName(doc,"span#dfDrinksTitle");
		List<String> imgs = getImgs(doc, "div#contentDfimage a");
		return new Element(url,murl,price,name,imgs,type);
	}
	
	public List<String> getImgs(Document doc,String cssQuery){
		List<String> result = new ArrayList<String>();
		elist = doc.select(cssQuery);
		String temp = elist.attr("style");
		temp = temp.substring(temp.indexOf("url")+10,temp.lastIndexOf(")"));
		result.add("http://www.drinkfans.com/"+temp);
		return result;
	}
	
	
	public Element getMessage(String html, String path) {

		return null;
	}
}
