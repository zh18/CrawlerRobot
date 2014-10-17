package scheme;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.mm.data.SuperData;
import com.mm.logger.Log;
import com.mm.util.SystemUtil;

public class Gnome extends SuperData {
	

	protected void pro0(int rate) throws IOException {
		process = PRODUCT;
		List<String> urls = SystemUtil.readLine(selector.getSavepath()+fname);
		if (rate > urls.size()) return;
		String url = null;
		boolean newfile = rate==0;
		for(int i=rate;i<urls.size();i++) {
			url = urls.get(i);
			
			breakpoint.setTotla(urls.size());
			breakpoint.recover(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()).toString(), 
					name, process, String.valueOf(i));
			
			String id = extractCatId(url);
			String jsonurl = addAjaxStr(1, id);
			int totalpage = getTotal(jsonurl);
			for(int page=0;page<=totalpage;page++){
				jsonurl = addAjaxStr(page, id);
				List<String> temp = jsongetProduct(jsonurl);
				if(temp.size() == 0) continue;
				for(String s:temp){
					SystemUtil.appendFile(selector.getSavepath()+uname, s, newfile);
					newfile = false;
				}
			}

		}

	}
	
	private List<String> jsongetProduct(String url){
		List<String> result = new ArrayList<String>();
		String html = spider.spider(url);
		if(null == html) {
			error.add(url);
			return result;
		}
		Document doc = Jsoup.parse(html);
		Elements jsonElements = doc.select("body");
		try {
			if(jsonElements!=null && jsonElements.size()>0){
				String jsonStr = jsonElements.get(0).text();
				if(!jsonStr.startsWith("{")){
					return result;
				}
				JSONObject jsonObject =new  JSONObject(jsonStr);
				JSONArray pageArray = jsonObject.getJSONArray("products");
				//提取每一页中的所有product url
				for (int j = 0; j < pageArray.length(); j++) {
					JSONObject product = (JSONObject) pageArray.get(j);
					JSONArray imagesArray = product.getJSONArray("images");
					JSONObject urlObject = (JSONObject) imagesArray.get(0); 
					String pId=(String) urlObject.get("pId");
					String skuid = (String) urlObject.get("skuId");
					result.add("http://www.gome.com.cn/product/"+pId+".html"+"&"+skuid);
				}
			}
		} catch(Exception e){
		}
		return result;
	}
	
	
	
	private int getTotal(String jsonurl){
		int result = -1;
		String json = spider.spider(jsonurl);
		if(null == json || json.equals("")) {
			json = spider.spider(jsonurl);
			if (null == json || json.equals("")) {
				return -1;
			}
		}
		String totalpage = json.substring(json.indexOf("totalPage")+9);
		totalpage = totalpage.substring(3,totalpage.indexOf("pageNumber")-3);
		try {
			result = Integer.parseInt(totalpage.trim());
		}catch(Exception e){
			Log.logger.warn("gnome get total number error", e);
		}
		return result;
	}
	
	
	private String extractCatId(String catId) {
		//提取catId
		String beginStr="http://www.gome.com.cn/category/";
		String endStr=".html";
		return catId.substring(catId.indexOf(beginStr)+beginStr.length(), catId.indexOf(endStr));
	}
	
	/*拼接字符每页的ajax请求*/
	private String addAjaxStr(int pageNum, String catId) {
		String str1="http://www.gome.com.cn/p/json?module=async_search&paramJson=%7B%22pageNumber%22%3A";
		String str2="%2C%22envReq%22%3A%7B%22catId%22%3A%22";
		String str3="%22%2C%22regionId%22%3A%2211011400%22%2C%22ip%22%3A%22123.123.1.76%22%2C%22et%22%3A%22%22%2C%22XSearch%22%3Afalse%2C%22pageNumber%22%3A1%2C%22pageSize%22%3A48%2C%22more%22%3A0%2C%22sale%22%3A0%2C%22instock%22%3A1%2C%22rewriteTag%22%3Afalse%2C%22priceTag%22%3A0%2C%22promoTag%22%3A0%2C%22esIp%22%3A%22%22%2C%22cookieid%22%3A%22ce70c1a5-836b-479e-b547-6b02e5a0cd2c%22%2C%22t%22%3A%22%26cache%3D3%26parse%3D5%22%2C%22question%22%3A%22%22%7D%7D";
		return str1+pageNum+str2+catId+str3;
	}
}
