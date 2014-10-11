package schemeInfo;

import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.mm.data.FBase;
import com.mm.data.struct.Element;


public class EbayFImpl extends FBase {

	public synchronized Element getMessage(String content) {
		String html = getHtml(content);
		Document doc = Jsoup.parse(html);
		String url = getUrl(content);
		String murl = getMurl(url);
		String type = getType(doc, "td#vi-VR-brumb-lnkLst a");
		String name = getName(doc,"h1#itemTitle");
		String price = getPrice(doc,"span[itemprop=price]");
		
		List<String> imgs = getImgs(doc, "div#mainImgHldr img");
		return new Element(url,murl,price,name,imgs,type);
	}


	public Element getMessage(String html, String path) {
		return null;
	}

	// http://m.ebay.com/itm/id
	public String getMurl(String url){
		String temp = url.substring(url.lastIndexOf("/")+1, url.length());
		temp = temp.substring(0, temp.indexOf("?"));
		return "http://m.ebay.com/itm/"+temp;
	}
	
	public String getPrice(Document doc,String cssQuery){
		elist = doc.select(cssQuery);
		if (elist.size() == 0) return "0";
		System.out.println(elist.text());
		return elist.text();
	}
	
}
