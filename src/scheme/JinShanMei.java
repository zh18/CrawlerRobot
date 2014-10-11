package scheme;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.mm.data.SuperData;

public class JinShanMei extends SuperData {
	

	protected String getNextlink(String html, String base, String next) {
		Document doc = Jsoup.parse(html);
		Elements elist = doc.select(next);
		
		for(Element e:elist){
			if (e.text().indexOf("下") != -1){
				return base+e.attr("href");
			}
		}
		return null;
	}
	
}
