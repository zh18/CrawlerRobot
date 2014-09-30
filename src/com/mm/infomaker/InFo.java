package com.mm.infomaker;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.management.RuntimeErrorException;

import com.mm.infomaker.base.Element;
import com.mm.infomaker.base.FormatterBase;

public class InFo {
	
	public List<Element> getInfo(String path,FormatterBase fb) throws IOException {
		List<Element> result = new ArrayList<Element>();
		File files [] = new File(path).listFiles();
		String typeFilePath = path.substring(0, path.lastIndexOf("html")-1);
		// path here have plus ~/../html/ , we need a ~/../
		System.out.println(typeFilePath);
		StringBuilder sb = new StringBuilder();
		String line = "";
		BufferedReader br = null;
		boolean flag = false;
		Element ele = null;
		int i = 0;
		for(File f:files){
			br = new BufferedReader(new FileReader(f));
			while ((line = br.readLine()) != null) {
				if (line.indexOf("ကကက") != -1 && flag){   // if flag == true , means we has next file
					if (path.indexOf("taobao") != -1 || path.indexOf("amazon") != -1 || path.indexOf("walmart") != -1) { // if have type file or other , we can use this .
						ele = fb.getMessage(sb.toString(), typeFilePath);
//						if (null == ele) continue;          // if product was not be found , then pass
						flag = false;
						sb = new StringBuilder();
						if (ele != null && ele.getUrl().indexOf("tmall") == -1) 
							result.add(ele);
//						System.out.println(i++);
					}
					else {
						ele = fb.getMessage(sb.toString());
						if (null != ele) {
							result.add(ele);
						}
						flag = false;
						sb = new StringBuilder();
					}
			    }	
			    if (line.indexOf("ကကက") != -1 && !flag) {  // if flag == false, means head ### URL ### 
					flag = true;
				}
				if ("".equals(line)) continue;
				sb.append(line+"\n");
			}
			br.close();
			System.out.println(f.getAbsolutePath());
		}
		return result;
	}
}
