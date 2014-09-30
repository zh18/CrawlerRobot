package com.mm.infomaker;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jsoup.select.Elements;

import com.gargoylesoftware.htmlunit.WebConsole.Formatter;
import com.mm.infomaker.base.Element;
import com.mm.infomaker.base.FormatterBase;

public class Util {
	
	public static final String AMAZON = "_SS40_:_SX500_",  //http://ec4.images-amazon.com/images/I/41zCpI8Gh0L._SS40_.jpg
							   AMAZONCOM = "_SS40_:_SX500_", //http://ecx.images-amazon.com/images/I/41HeFbcQxiL._SS40_.jpg
							   TAOBAO = ".jpg:.jpg_430x430q90.jpg",  //http://gw.alicdn.com/bao/uploaded/i3/28266957/TB2E8XRapXXXXc8XXXXXXXXXXXX_!!28266957.jpg  
							   YHD    = "_60x60:_500x500",  //http://d9.yihaodianimg.com/V00/M08/B3/CC/CgQDsVP0BUeAJ8z2AAVuijBP52s68200_60x60.jpg
							   DD     = "_x_1:_w_5",  //http://img36.ddimg.cn/70/24/1216637206-1_x_1.jpg
							   WALMART= "",  //no use
							   JX     = "c1.jpg:c5.jpg",  //http://img09.sohu.jiuxian.com/2014/0726/d17f45a65d6e43cd9980c9ec489ddddc9.jpg
							   JIUMI  = "",  //no use
							   YEMAIJIU  = "_60x98:_380x620", // http://img18.yesmyimg.com/5195777/9890784_60x98.jpg
							   LANQUAN = ""; //no use
	public static final String CATA[] = {"ghhz","spyl","jiulei","wanju","my","yybj"};
	public static final String NAME[] = {"amazon","amazon.com","dd","yhd","walmart","taobao","jiuxian","jiumi","yemaijiu","lanquan"};
	public static final String FEATURE[] = {AMAZON,AMAZONCOM,DD,YHD,WALMART,TAOBAO,JX,JIUMI,YEMAIJIU,LANQUAN};
	public static final String ROOT = "/home/public/zhanhe/dd/";
	/**
	 * To make info.txt for those directory
	 * @param path
	 * @param fb
	 * @throws IOException
	 */
	public void infoMaker(List<String> paths,FormatterBase fb) throws IOException{
		for (int i=0;i<paths.size();i++) {
			new Thread(threadRuning(paths.get(i), fb)).start();
		}
	}
	
	
	/**
	 * ### 频繁的打开关闭随机读写流 ###
	 * @param set
	 * @param path
	 */
	public static void appendFile(Set<String> set,String path){
		try {
			RandomAccessFile raf = new RandomAccessFile(path, "rw");
			long pointer = raf.length();
			raf.seek(pointer);
			for(String s:set){
				raf.writeChars(s+"\n");
			}
			raf.close();
		}catch(Exception e){};
	}
	
	public Runnable threadRuning(final String path,final FormatterBase fb) {
		return new Runnable() {
			public void run() {
				File type = null;
				BufferedWriter bw = null;
				List<Element> elist = null;
				
				System.out.println(path);
				try {
					type = new File(path+"info.txt");
					if (!type.exists()) type.createNewFile();
					bw = new BufferedWriter(new FileWriter(type));
					elist = new InFo().getInfo(path+"/html/",fb);
					for(Element e:elist){
						try { 
							bw.write(e.toString());
						}catch(NullPointerException e1){continue;}
					}
					bw.close();
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		};
	}
	
	public static void showBar(String name, int now, int total) {
		StringBuffer result = new StringBuffer("");
		int count = 60;
		int printStar = (int) ((now * 1.0 / total) * count);
		result.append("[");
		
		for (int i = 0; i < count; i++) {
			if (i < printStar)
				result.append("=");
			else if (i == printStar) {
				result.append(">");
			} else
				result.append(" ");
		}
		result.append("]");
		System.out.print(name
				+ " : "
				+ result.append(" "
						+ String.format("%.2f", (now * 100 * 1.0 / total))
						+ " %  " + now + "/"+total+"\r"));
	}
	
	public static void change(String path) throws IOException {
		File [] files = new File(path).listFiles();
		BufferedReader br = null;
		BufferedWriter bw = null;
		String line = "";
		boolean flag = false;
		StringBuilder sb = new StringBuilder();
		for(File f:files){
//			System.out.println(f.getAbsolutePath());
			br = new BufferedReader(new FileReader(f));
			bw = new BufferedWriter(new FileWriter(f.getAbsoluteFile()+".cpy"));
			while ((line = br.readLine()) != null){
				if (line.indexOf("ကက") != -1 && flag){
					flag = false;
					bw.write(sb.toString());
					sb = new StringBuilder();
					continue;
				}
				else if (line.indexOf("ကက") != -1 && !flag){
					flag = true;
				}
				sb.append(line+"\n");
			}
			br.close();
			bw.close();
		}
	}

	public  void pickpic(Map<String,String> ff){
		for(String s:ff.keySet()) {
			try {
				new Thread(getRun(s,ff.get(s))).start();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	public Runnable getRun(final String path,final String feature){
		return new Runnable() {
			public void run() {
				String f = null;
				String re = null;
				try {
					f = feature.substring(0, feature.indexOf(":"));
					re = feature.substring(feature.indexOf(":")+1,feature.length());
				}catch(Exception e){}
				try {
					BufferedWriter fw = new BufferedWriter(new FileWriter(path+"info_reurl"+getName(path)+"0.txt"));
					BufferedReader fr = new BufferedReader(new FileReader(path+"info.txt"));
					String line = "";
					while((line = fr.readLine()) != null) {
						fw.write(getPics(line,f,re)+"\n");
					}
					fw.close();
					fr.close();
				}catch(Exception e){
					// 找不到文件证明没有这个东西
				}
			}
		};
	}
	// too much be called
	public String getPics(String line,String feature,String replace){
		if (null == feature) return line;
		String ttt = line.substring(0,line.lastIndexOf("ကက"));
		ttt = ttt.substring(ttt.lastIndexOf("ကက")+2,ttt.length());
		String result = "";
		String lines[] = ttt.split("က");
		if (lines.length == 0) return ttt.replace(feature, replace);
		for(String s:lines){
			if (null != replace){
				line = line.replaceAll(feature, replace);
				result += line+"က";
			}
			else {
				if (s.indexOf(feature) != -1){
					result = s;
				}
			}
		}
		return result;
	}
	
	public String getName(String url){
		if(url.indexOf("amazon") != -1 || url.indexOf("yhd") != -1){
			return "_500_500_";
		}
		else if (url.indexOf("taobao") != -1){
			return "_430_430_";
		}
		else if (url.indexOf("jiuxian") != -1) {
			return "_440_440_";
		}
		else if (url.indexOf("yemaijiu") != -1){
			return "_360_620_";
		}
		else if (url.indexOf("dd") != -1){
			return "_350_350_";
		}
		return "";
	}
	
	
	public static void main(String[] args) {
		Util u = new Util();
		Map<String,String> map = new HashMap<String,String>();
		for(int i=0;i<CATA.length;++i){
			for(int j=0;j<NAME.length;++j){
				map.put(ROOT+CATA[i]+"/"+NAME[j]+"/", FEATURE[j]);
			}
		}
		u.pickpic(map);
	}
}
