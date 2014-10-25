package com.mm.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class FileC {
	
	private String path = null;
	private BufferedWriter bw = null;
	
	
	public FileC(){}
	
	public FileC (String path) throws IOException {
		this.path = path;
		bw = new BufferedWriter(new FileWriter(path));
	}
	
	public synchronized void write(String line) throws IOException{
		bw.write(line+"\n");
	}
	
	public void setPath(String path) throws IOException{
		this.path = path;
		if(bw != null) bw.close();
		bw = new BufferedWriter(new FileWriter(path));
	}
	
	public String getPath(){
		return path;
	}
	
	public void flush() throws IOException {
		if(bw != null){
			bw.flush();
		}
	}
	
	public boolean close() throws IOException {
		if(null != bw){
			bw.close();
			return true;
		}
		return false;
	}
}
