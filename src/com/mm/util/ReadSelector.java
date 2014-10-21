package com.mm.util;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.mm.data.struct.Selector;

public class ReadSelector {
	
	
	/**
	 * name like DD_ghhz
	 * @param name
	 */
	public static Selector getSelector(String name){
		String sname = name.substring(0, name.indexOf("_"));
		String subname = null;
		try {
			subname = name.substring(name.indexOf("_")+1,name.indexOf("."));
		}catch(Exception e){
			subname = name.substring(name.indexOf("_")+1);
		}
		List<String> properties =  SystemUtil.getAllKeys(SYS.PATH+SYS.SYS_DG_SCHEME_FLODER+sname+".properties");
		String num = findKey(subname,properties);
		String rootpath,fselect,pselect,nselect,fbase,nbase,pbase,savepath;
		String classify,brand;
		rootpath = findValue(Selector.ROOT, num, properties);
		fselect = findValue(Selector.FS, num, properties);
		pselect = findValue(Selector.PS, num, properties);
		nselect = findValue(Selector.NS, num, properties);
		savepath = findValue(Selector.SP, num, properties);
		fbase = findValue(Selector.FB, num, properties);
		pbase = findValue(Selector.PB, num, properties);
		nbase = findValue(Selector.NB, num, properties);
		classify = findValue(Selector.CS,num,properties);
		brand = findValue(Selector.BD, num, properties);
		
		return new Selector(name, rootpath.split(","), fselect.split(","), pselect, nselect, savepath,classify,brand, fbase, pbase, nbase);
	}
	
	
	private static String findKey(String name,List<String> lines) {
		for(String s:lines){
			if(s.indexOf("name") != -1 && s.indexOf(name) != -1){
				return s.substring(s.indexOf("_")+1, s.indexOf("="));
			}
		}
		return null;
	}
	
	public static List<String> getAllNames(){
		File [] files = null;
		List<String> result = new ArrayList<String>();
		try {
			files = new File(SYS.PATH+SYS.SYS_DG_SCHEME_FLODER).listFiles(new FileFilter() {
				public boolean accept(File pathname) {
					return pathname.getName().endsWith("properties");
				}
			});
			for(File f:files){
				result.addAll(getKeys(f.getName(),"name"));
			}
		}catch(Exception e){
		}
		return result;
	}
	
	private static List<String> getKeys(String sname,String name) {
		List<String> temp = SystemUtil.getAllKeys(SYS.PATH+SYS.SYS_DG_SCHEME_FLODER+sname);
		List<String> result = new ArrayList<String>();
		for(String s:temp){
			if(s.startsWith(name)){
				result.add(sname.substring(0,sname.indexOf("."))+"_"+s.substring(s.indexOf("=")+1));
			}
		}
		return result;
	}

	//如果有制定的，则返回制定的，如果没有制定的，则返回_0的
	private static String findValue(String valname,String num,List<String> lines){
		for(String s:lines){
			if(s.indexOf(valname+"_"+num) != -1) {
				return s.substring(s.indexOf("=")+1).trim();
			}
		}
		if(! num.trim().equals("0")){
			return findValue(valname,"0",lines);
		}
		return "";
	}
	
	public static boolean isIn(String name){
		for(String s:getAllNames()){
			if(s.equals(name.trim())) return true;
		}
		return false;
	}
	
	public static void main(String[] args) {
		for(String s:getAllNames()){
			System.out.println(s);
		}
	}
}
