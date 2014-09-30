package com.mm.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.Writer;
import java.net.Proxy;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.springframework.beans.factory.parsing.Problem;

import com.mm.datagetter.Selectors;
import com.mm.logger.Log;

public class SystemUtil {
	
	public static void showBar(String name, int now, int total) {
		StringBuffer result = new StringBuffer("");
		int count = 40;
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
	
	
	/**
	 * 按行读文件
	 * @param path
	 * @return set
	 */
	public static List<String> readLine(String path) {
		List<String> result = new ArrayList<String>();
		BufferedReader br = null;
		String line = "";
		try {
			br = new BufferedReader(new FileReader(path));
			while((line = br.readLine()) != null) {
				result.add(line);
			}
		}catch(IOException e){
			//log4j
		}
		return result;
	}

	/**
	 * efficiency problem here ~~~~
	 */
	public static void appendFile(String path,String line) throws IOException{
		RandomAccessFile raf = new RandomAccessFile(path, "rw");
		raf.seek(raf.length());
		raf.writeBytes(line+"\n");
		raf.close();
	}
	
	/**
	 * 按照给定的路径，制定的包名加载类
	 * @param path class文件存放路径
	 * @param url  系统bin文件路径
	 * @param packagename 加载的包名
	 */
	public static void loadClass(String path,String url,String packagename) {
		ClassLoader cl = null;
		int i = 0;
		try {
			File files[] = new File(path).listFiles();
			URL [] urls= new URL[]{new URL("file:"+url)};
			cl = new URLClassLoader(urls);
			for(File f:files){
				// e.g.   com.mm.core.Core
				cl.loadClass(packagename+"."+(f.getName().substring(0, f.getName().indexOf("."))));
			}
		}catch(Exception e){
			Log.logger.error("SystemUtil Load Class Error", e);
		}
	}
	
	
	public static Selectors getSelectors(String name){
		Properties pro = new Properties();
		String base = "/home/public/javaproject/CrawlerRobot/bin/";
		try {
			pro.load(new FileInputStream(base+SYS.SYS_DG_SCHEME_FLODER+name+"."+"properties"));
		}catch(Exception e){
			Log.logger.error("Load Properties error", e);
		}
		String rootpath = pro.getProperty("rootpath");
		String fselects = pro.getProperty("fselects");
		String [] fs = fselects.split(",");
		String basepath = pro.getProperty("basepath");
		String products = pro.getProperty("products");
		String next = pro.getProperty("next");
		boolean fbase = Boolean.parseBoolean(pro.getProperty("fbase"));
		boolean pbase = Boolean.parseBoolean(pro.getProperty("pbase"));
		boolean nbase = Boolean.parseBoolean(pro.getProperty("nbase"));
		return new Selectors(rootpath,basepath, fs, products, next, fbase, pbase, nbase);
	}
	
	public static void writeColl(Collection col,String path){
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(path));
			for(Object obj:col){
				bw.write(obj.toString()+"\n");
			}
			bw.close();
		}catch(Exception e){
			Log.logger.error("Writer collection (first) error", e);
		}
	}
	
	public static String[][] readProxy() {
		List<String> pro = readLine("./proxy");
		String [][] result = new String[pro.size()][];
		String line[] = null;
		for(int i=0;i<pro.size();i++){
			line = pro.get(i).split(":");
			result[i] = new String[2];
			result[i][0] = line[0];
			result[i][1] = line[1];
		}
		return result;
	}
}
