package schemeInfo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.mm.data.FBase;
import com.mm.data.struct.Element;


public class AComFImpl extends FBase{

	Document doc = null;
	Elements elist = null;
	
	public Element getMessage(String content) {
		return null;
	}
	
	public Element getMessage(String content, String path) {
		String html = getHtml(content);
		Document doc = Jsoup.parse(html);
		String url = getUrl(content);
		String murl = url;
		String price = getPrice(doc,"div#price_feature_div td.a-span12 > span#priceblock_ourprice"); // my
		if ("".equals(price) || "0".equals(price))
			price = getPrice(doc,"b.priceLarge"); 
		if ("".equals(price) || "0".equals(price))
			price = getPrice(doc,"span#priceblock_saleprice");
		if ("".equals(price) || "0".equals(price))
			price = getPrice(doc,"span.olp-padding-right span.a-color-price");
		String type = getType(url,path);
		String name = getName(doc,"#productTitle"); // my
		if ("".equals(name))
			name = getName(doc,"h1.parseasinTitle");
		List<String> imgs = getImgs(doc, "li.a-spacing-small span[data-action=thumb-action] img");
		if (imgs.size() == 0)
			imgs = getImgs(doc,"div#thumb-strip img");
		if (imgs.size() == 0) {
			imgs = getImgs(doc,"div#altImages img");
		}
		if ("".equals(name)) return null;
		if (imgs.size() == 0) return null;
		if (null == type || "".equals(type)) return null;
		return new Element(url,murl,price,name,imgs,type);
	}
	
	public String getPrice(Document doc,String cssQuery){
		elist = doc.select(cssQuery);
//		System.out.println(elist.size());
	 	if (elist.size() == 0) return "0";
		String result = elist.text();
		return result;
	}
	
	public String getUrl(String content){
		String temp = super.getUrl(content);
		String id = temp.substring(temp.indexOf("dp")+3,temp.length());
		try {
			id = id.substring(0, id.indexOf("/"));
		}catch(Exception e){}
		return "http://www.amazon.com/dp/"+id;
	}
	
	public String getType(String url,String path){
		String id = url.substring(url.indexOf("dp")+3,url.length());
		try {
			id = id.substring(0, id.indexOf("/"));
		}catch(Exception e){}
		
		BufferedReader br = null;
		
		String line = "";
		String temp = "";
		try {
		 br = new BufferedReader(new FileReader(path+"type.txt"));
		 while ((line = br.readLine()) != null) {
			 if (line.indexOf(id) != -1){
				 temp = line;
				 break;
			 }
		 }
		 br.close();
		}catch(Exception e){e.printStackTrace();}
		if (null == temp || "".equals(temp)) return "";
		temp = temp.substring(temp.indexOf("ကက")+2, temp.length());
		temp = temp.replaceAll("[()\\d]", "");
		return temp;
	}
	
}
