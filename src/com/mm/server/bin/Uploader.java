/**
 * 上传class文件，进行热加载
 * 
 * @author zh
 * @version 0.1
 */
package com.mm.server.bin;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import com.mm.logger.Log;
import com.mm.server.Bin;

public class Uploader {
	
	private static String path;
	
	public static void upload(InputStream is,OutputStream os,String filename){
		BufferedInputStream bis = new BufferedInputStream(is);
		FileOutputStream bos = null;
		byte buffer[] = new byte[2048];
		int len = 0;
		try {
			File file = new File(path+File.separator+(filename.indexOf(".class")!=-1?filename:filename+".class"));
			bos = new FileOutputStream(file);
			while((len = bis.read(buffer)) != -1){
				bos.write(buffer, 0, len);
			}
			bos.flush();
			bos.close();
			new PrintStream(os).println("File :"+filename+" upload success");
		} catch (IOException e) {
			Log.logger.error("upload error",e);
		}
	}
	
	
	public static String getPath(){
		return path;
	}
	public static void setPath(String path){
		Uploader.path = path;
	}


	public void run(InputStream is, OutputStream os, String cmd) {
		// TODO Auto-generated method stub
		
	}


	public String getName() {
		return " uploader";
	}
	
}