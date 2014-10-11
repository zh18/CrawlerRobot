package scheme;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.mm.data.SuperData;

public class No5 extends SuperData {

	protected String getNextlink(String html, String base, String next) {
		String nextlink = "";
		Document doc = Jsoup.parse(html);
		Elements elist = doc.select(next);
		if (elist.size() == 0) return null;
		boolean flag = false;
		for(Element e:elist){
			if (e.text().indexOf("下") != -1){
				nextlink = e.attr("href");
				flag = true;
				break;
			}
		}
		if (flag) return base+nextlink;
		return null;
	}
}
