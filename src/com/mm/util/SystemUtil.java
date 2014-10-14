package com.mm.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
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
	
	public static void appendFile(String path,Collection<String> col) throws IOException {
		RandomAccessFile raf = new RandomAccessFile(path,"rw");
		raf.seek(raf.length());
		for(String s:col){
			raf.write((s+"\n").getBytes());
		}
		raf.close();
	}
	
	public static void appendFile(String path,File dpath) throws IOException {
		RandomAccessFile raf = new RandomAccessFile(path,"rw");
		BufferedReader br = new BufferedReader(new FileReader(dpath));
		String line="";
		while((line = br.readLine()) != null){
			raf.write((line+"\n").getBytes());
		}
		raf.close();
		br.close();
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
	
	/**
	 * 添加新的一行到文件尾部
	 * <pre>
	 * 频繁的open connection and close connection
	 * </pre>
	 * @param path
	 * @param line
	 * @param newfile 是否为新文件？<tt>true</tt>新文件   <tt>false</tt>原来的旧文件
	 * @throws IOException
	 */
	public static synchronized void appendFile(String path,String line,boolean newfile) throws IOException{
		//如果想要新文件的话，则把原来的删除
		if (newfile) new File(path).delete();
		RandomAccessFile raf = new RandomAccessFile(path, "rw");
		raf.seek(raf.length());
		raf.write((line+"\n").getBytes());
		raf.close();
	}
	
	public static void writeColl(Collection col,String path){
		BufferedWriter bw = null;
		File f = new File(path);
		File cf = new File(f.getParent());
		if (!cf.exists()) cf.mkdirs();
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
	
	public static List<String> getAllKeys(String path){
		List<String> temp = readLine(path);
		List<String> result = new ArrayList<String>();
		for(int i=0;i<temp.size();i++){
			if (temp.get(i).trim().equals("") || temp.get(i).trim().startsWith("#")) continue;
//			temp.set(i, temp.get(i).substring(0, temp.get(i).indexOf("=")));
			result.add(temp.get(i));
		}
		return result;
	}
	
	public static int getLoc(String [] src,String target){
		for(int i=0;i<src.length;i++){
			if (src[i].indexOf(target) != -1) {
				return i;
			}
		}
		return -1;
	}
	
	public static String printAL(String [] src,String insert){
		StringBuilder sb = new StringBuilder();
		for(String s:src){
			sb.append(s+insert);
		}
		return sb.toString();
	}
	public static String printAL(List<String> src,String insert){
		StringBuilder sb = new StringBuilder();
		for(String s:src){
			sb.append(s+insert);
		}
		return sb.toString();
	}
	
	
	public static synchronized boolean merge(String src,String dest) throws IOException{
		File [] srcf=null, destf=null;
		if(!(new File(src).isDirectory() && new File(dest).isDirectory())) return false; 
		srcf = new File(src).listFiles();
		destf = new File(dest).listFiles();
		boolean flag = false;
		for(int i=0;i<srcf.length;i++){
			String name = srcf[i].getName();
			for(int j=0;j<destf.length;j++){
				if(name.equals(destf[j]))
					flag=true;
			}
			if(!flag) return false;
			if(srcf[i].isFile()){
				for(int j=0;j<destf.length;j++){
					if(srcf[i].getName().equals(destf[j].getName())){
						appendFile(srcf[i].getAbsolutePath(), destf[j]);
					}
				}
			}
			else if(srcf[i].isDirectory()){
				for(int j=0;j<destf.length;j++){
					if(srcf[i].getName().equals(destf[j].getName())){
						File [] srcftemp = srcf[i].listFiles();
						for(File temp:srcftemp){
							copyFile(temp,destf[j].getAbsolutePath());
						}
					}
				}
			}
		}
		return false;
	}
	
	// targe 多线程复制
	public static synchronized void copyFile(File file,String path) throws IOException {
		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(path+File.separator+file.getName()));
		BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
		byte buffer[]=new byte[4096];
		int size = 0;
		while((size = bis.read(buffer)) != -1){
			bos.write(buffer, 0, size);
		}
		bos.close();
		bis.close();
	}
	
	public static void write(String path,String line) throws IOException {
		BufferedWriter bw = new BufferedWriter(new FileWriter(path));
		bw.write(line);
		bw.close();
	}
	
	public static boolean filecheck(String path) {
		return new File(path).exists();
	}
	
	public static String readProperties(String path,String key) throws IOException {
		Properties prop = new Properties();
		prop.load(new FileInputStream(path));
		return prop.getProperty(key);
	}
	
}
