package scheme;

import org.jsoup.Jsoup;

import com.mm.data.SuperData;

public class Sunning extends SuperData {
	
	protected String getNextLink(String html, String basepath, String cssQuery) {
		doc = Jsoup.parse(html);
		elist = doc.select(cssQuery);
		
		String nextlink = elist.attr("href");
		System.out.println(nextlink);
		if(nextlink.trim().equals("")) return null;
		if(nextlink.indexOf("java") != -1) return null;
		if ("".equals(nextlink)) return null;
		return basepath+nextlink;
	}
}
