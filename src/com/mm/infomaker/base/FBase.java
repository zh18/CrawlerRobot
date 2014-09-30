package com.mm.infomaker.base;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public abstract class FBase implements FormatterBase {

	protected Elements elist = null;

	public abstract Element getMessage(String html);


	public abstract Element getMessage(String html, String path);


	public String getUrl(String content){
		String lines [] = content.split("\n");
		String temp = "";
		for(String s:lines){
			if (s.indexOf("ကကက") != -1){
				temp = s;
				break;
			}
		}
		return temp.substring(3, temp.length()-3);
	}
	
	public String getMurl(String url) { return "";}
	
	public String getName(Document doc,String cssQuery) {
		elist = doc.select(cssQuery);
		if (elist.size() == 0) return "";
		String name = elist.text();
//		name = name.replaceAll("\\\\\n", "");
//		try {
//			name = name.substring(name.indexOf("about")+5, name.length());
			
//		}catch(Exception e){}
		name = name.trim();
		return name;
	}
	
	public String getType(Document doc,String cssQuery){
		elist = doc.select(cssQuery);
		String temp = "";
		for(org.jsoup.nodes.Element e:elist){
			if (e.text().indexOf("(") != -1 || e.text().indexOf(">") != -1) continue;
			temp = temp + e.text() + "က";
		}
		return temp;
	}
	
	public List<String> getImgs(Document doc,String cssQuery){
		elist = doc.select(cssQuery);
		List<String> result = new ArrayList<String>();
		for(org.jsoup.nodes.Element e:elist){
			result.add(e.attr("src"));
		}
		return result;
	}
	
	public String getHtml(String content){
		String temp[] = content.split("\n");
		StringBuilder sb = new StringBuilder("");
		for(int i=0;i<temp.length;i++){
			if (temp[i].indexOf("ကကက") != -1)  // have this line 
				continue;
			if (temp[i].indexOf("ကက") != -1)    // have this json line 
				return sb.toString();
			sb.append(temp[i]+"\n");
		}
		return sb.toString();
	}

	public String getJson(String content){
		String temp[] = content.split("\n");
		StringBuilder sb = new StringBuilder();
		int i = 0;
		for(;i<temp.length;i++) {
			if (temp[i].startsWith("ကကh"))   // don't have this line ကကhttp://
				break;
		}
		for(i++;i<temp.length;i++){
			sb.append(temp[i]);
		}
		return sb.toString();
	}
	
	public String getPrice(Document doc,String cssQuery){
		elist = doc.select(cssQuery);
//		System.out.println(elist.size());
	 	if (elist.size() == 0) return "0";
		return elist.text();
	}
	
	public String getType(String id,String path) {
		BufferedReader br = null;
		String line = "";
		String temp = "";
		try {
			br = new BufferedReader(new FileReader(path+"type.txt"));
			while ((line = br.readLine()) != null){
				if (line.indexOf(id) != -1){
					temp = line;
					break;
				}
			}
			br.close();
		}catch(Exception e){}
		if ("".equals(temp)) return "";
		temp = temp.substring(temp.indexOf("ကက"), temp.length());
		return temp;
	}
	
	
}
