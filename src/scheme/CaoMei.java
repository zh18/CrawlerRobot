package scheme;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.mm.data.SuperData;
import com.mm.util.SystemUtil;

public class CaoMei extends SuperData {

	protected void pro0(int rate) throws IOException,FileNotFoundException {
		process = PRODUCT; 
		String url = null;
		Set result = new HashSet();
		List<String> urls = SystemUtil.readLine(selector.getSavepath()+fname);
		System.out.println(urls.size());
		if (rate > urls.size()) return ;
		int total = urls.size();
		String line = "";
		boolean newfile = rate == 0;
		
		
		for(int i=rate;i<urls.size();i++){
			url = urls.get(i);

			breakpoint.setTotla(urls.size());
			breakpoint.recover(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()).toString(), 
					name, process, String.valueOf(i));
			
			try {
				html = spider.spider(url);
			}catch(Exception e){
				System.out.println(url);
				continue;
			}
			int count = Integer.parseInt(getCount(html)==null?"0":getCount(html));
			for(int page=1;page<=count;page++){
				try {
					html = spider.spider(url+"&page="+page);
					doc = Jsoup.parse(html);
				}
				catch(Exception e){
					error.add(url);
					continue;
				}
				elist = doc.select(selector.getProducts());
				if (elist.size() == 0) {
					System.out.println(url+"&page="+page);
					continue;
				}
				for(Element e:elist){
					// i use this to stop this thread
					if (!selector.getPbase().equals("#"))
						line = selector.getPbase()+e.attr("href");
					else 
						line = e.attr("href");
					result.add(line);
					SystemUtil.appendFile(selector.getSavepath()+uname, line, newfile);
					newfile = false;
				}	
			}
		}
	}
	
	private String getCount(String html){
		Document doc = Jsoup.parse(html);
		Elements elist = doc.select(selector.getNext());
		if (elist.size() == 0) return "1";
		String count = null;
		String temp = elist.get(elist.size()-1).text();
		
		if (temp.indexOf("next") != -1)
			count = elist.get(elist.size()-2).attr("href");
		else
			count = elist.get(elist.size()-1).attr("href");
		try {
			count = count.substring(count.indexOf("page=")+5);
		}catch(Exception e) {
			return null;
		}
		return count;
	}
}
