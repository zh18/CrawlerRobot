package scheme;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.mm.data.SuperData;
import com.mm.data.struct.Type;
import com.mm.util.SystemUtil;

public class Tmall extends SuperData{


//	protected void pro0(int rate) throws IOException {
//		process = PRODUCT;
//		//查看是否需要加入type文件
//		boolean isType = isTypes();
//		String typeq=isType?selector.getClassify():"";
//		String typetemp = "";
//		
//		String url = null;
//		List<String> urls = SystemUtil.readLine(selector.getSavepath()+fname);
//		if (rate > urls.size()) return;
//		String line = "";
//		boolean newfile = rate==0;
//		for(int i=rate;i<urls.size();i++){
//			url = urls.get(i);
//			breakpoint.setTotla(urls.size());
//			breakpoint.recover(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()).toString(), 
//					name, process, String.valueOf(i));
//			do {
//				try {
//					html = spider.spider(url);
//				}catch(Exception e){
//					error.add(url);
//					continue;
//				}
//				if (null == html) {
//					error.add(url);
//					continue;
//				}
//				doc = Jsoup.parse(html);
//				elist = doc.select(selector.getProducts());
//
//				if(isType){
//					typetemp = getType(doc, isType);
//				}
//				
//				for(Element e:elist){
//					if (!selector.getPbase().equals("#"))
//						line = selector.getPbase()+getId(e.attr("href"));
//					else 
//						line = e.attr("href");
//					SystemUtil.appendFile(selector.getSavepath()+uname, line,newfile);
//					if (isType) {
//						SystemUtil.appendFile(selector.getSavepath()+tname,new Type(typetemp,line).toString(),newfile);
//					}
//					newfile = false;
//				}
//			}while ((url = getNextLink(html,selector.getNbase().equals("#")?"":selector.getNbase(),selector.getNext())) != null);
//		}
//	}
	
	protected final String getId(String url){
		String temp = url.substring(url.indexOf("id=")+3);
		temp = temp.substring(0,url.indexOf("&"));
		return temp;
	}
}
