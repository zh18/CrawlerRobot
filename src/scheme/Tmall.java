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
import org.jsoup.select.Elements;

import com.mm.data.SuperData;
import com.mm.util.SystemUtil;

public class Tmall extends SuperData{

	//获取所有分类后的链接
	protected void first0() {
		process = FIRST;
		List<String> urls = selector.getRootpath();
		List<String> result = new ArrayList<String>();
		if(urls.size() == 0) return ;
		String url = null;
		for(int i=0;i<urls.size();i++) {
			breakpoint.setTotla(urls.size());
			breakpoint.recover(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()).toString(), 
					name, process, String.valueOf(i));
			url = urls.get(i);
			String brandHtml = spider.spider(url);
			if (brandHtml == "" || brandHtml == null) {
				error.add(url);
				continue;
			}
			Document brandDoc = Jsoup.parse(brandHtml);
			Elements moreElements = brandDoc
					.select(".brandAttr div .attrValues .av-options a");
			if (moreElements != null) {
				String jsonUrl = moreElements.get(1).attr("data-url");
				String jsonString = spider.spider(jsonUrl);
				if (jsonString == null) {
					continue;
				}
				try {
					JSONArray jsonArray = new JSONArray(jsonString);
	
					// 得到指定的key对象的value
					for (int k = 0; k < jsonArray.length(); k++) {
						JSONObject jsonObject = (JSONObject) jsonArray.get(k);
						String urltemp = jsonObject.getString("href");
						result.add(urltemp);
					}
				} catch(Exception e){
				}
			}
		}
		SystemUtil.writeColl(result, selector.getSavepath()+fname);
	}

	
	
	protected void pro0(int rate) throws IOException {
		process = PRODUCT;
		List<String> urls = SystemUtil.readLine(selector.getSavepath()+fname);
		if(urls.size() == 0 || rate>urls.size()) return ;
		boolean newfile = rate==0;
		
		for(int i=0;i<urls.size();i++){
			
		}
	}
}
