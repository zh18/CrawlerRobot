package schemeInfo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.mm.data.FBase;
import com.mm.data.struct.Element;

public class AmazonFImpl extends FBase {

	Elements elist = null;
	Document doc = null;
	String url = "";
	String type = "";
	
	public String getName(Document doc) {
		elist = doc.select("span#productTitle");
		if (elist.size() == 0) return "";
//		System.out.println(elist.text());
		return elist.text();
	}

	//http://www.amazon.cn/%E6%B3%B8%E5%B7%9E%E8%80%81%E7%AA%96%E9%86%87%E9%A6%99A-52%
	//E5%BA%A6500ml/dp/B00IUCX7SC/ref=sr_1_674/479-8655305-4102210?s=alcohol&ie=UTF8&qid=1410340397&sr=1-674ကက食品က酒က白酒
	public String getUrl(String content) {
		String lines[] = content.split("\n");
		String result = "";
		for(String s:lines){
			if (s.indexOf("ကကက") != -1){
				result = s;
				break;
			}
		}
		try {
			result = result.substring(3, result.length()-3);
		}catch(Exception e){e.printStackTrace();}
		url = result;
		return result;
	}

	public String getUrl(Document doc){
		elist = doc.select("link[rel=canonical]");
		if (elist.size() == 0) return "";
		return elist.text();
	}
	
	public String getMurl() {
		return "";
	}

	// sl1550_.jpg   change bigger
	public List<String> getImgs(Document doc) {
 		elist = doc.select("li.a-spacing-small span[data-action=thumb-action] img");
		List<String> result = new ArrayList<String>();
		for(org.jsoup.nodes.Element e:elist){
			if(e.attr("src").indexOf("http://") != -1)
				result.add(e.attr("src"));
		}
		return result;
	}

	
	public String getType(Document doc) {
		String result = "";
		elist = doc.select("a#brand");
		if(elist.size() == 0) return type;
		result =  result+elist.text()+"က";
		return result;
	}

	
	public String getPrice(Document doc) {
//		elist = doc.select("div#price_feature_div div#price span#priceblock_ourprice ");
//
//		if ("".equals(elist.text())) {
//			elist = doc.select("div#olp_feature_div span.a-color-price");
//		}
//		if ("".equals(elist.text())){
			elist = doc.select("span#priceblock_saleprice");
//		}
//		if ("".equals(elist.text())) return "0";
//		if (elist.size() > 0) 
//			System.out.println(doc.select("link[rel=canonical]"));
		String temp =  "";
		try {
			temp = elist.get(0).text();
		}catch(Exception e){return null;}
		
		try {
			temp =  temp.substring(1, temp.length());
			Double.parseDouble(temp);
		}catch(Exception e){return "0";}
		return temp;
	}

	public synchronized Element getMessage(String content){
		String html = getHtml(content);
		doc = Jsoup.parse(html);
//		String json = DDFImpl.getJson(content);
		String url = this.getUrl(content);
		String murl = this.getUrl(content);
		String name = this.getName(doc);
		String type = this.getType(doc);
		String price = this.getPrice(doc);
		if (null == price) price = "0";
		List<String> imgs = this.getImgs(doc);
		return new Element(url,murl,price,name,imgs,type);
	}
	
	public String getType(String html,String path,Document doc) {
		if ("".equals(url)) this.getUrl(html);
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
	
	public Element getMessage(String content,String path) {
		String html = getHtml(content);
		doc = Jsoup.parse(html);
//		String json = DDFImpl.getJson(content);
		String url = this.getUrl(content);
		String murl = this.getUrl(content);
		String name = this.getName(doc);
		String type = this.getType(content,path,doc);
		String price = this.getPrice(doc);
		List<String> imgs = this.getImgs(doc);
		if (null == name || "".equals(name) || null == price || "".equals(price) || "0".equals(price)) return null;
		return new Element(url,murl,price,name,imgs,type);
	}

}
