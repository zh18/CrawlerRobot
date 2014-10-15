package com.mm.data.multithreading;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

import com.mm.data.Idata;
import com.mm.data.struct.Selector;
import com.mm.spider.SpiderFactory;
import com.mm.stop.BreakPoint;
import com.mm.util.ReadSelector;

public class MulData implements Idata {

	protected Set<String> error = null;
	protected Selector selector = null;
	protected String process,name,rate;
	protected SpiderFactory factory;
	protected BreakPoint breakpoint;
	protected BufferedReader br = null;
	protected BufferedWriter bw = null;
	
	protected IMul<String> first = null;
	protected IMul<String> pro = null;
	protected IMul<String> down = null;
	
	protected Stack<String> prostack = null;
	protected Stack<String> htmlstack = null;
	
	private int pthreadn,dthreadn;
	
	protected Set<String> proset = null;
	protected Set<String> htmset = null;
	
	public MulData(String name,boolean bp){
		this.name = name;
		error = new HashSet<String>();
		proset = new HashSet<String>();
		htmset = new HashSet<String>();
		selector = ReadSelector.getSelector(name);
		try {
			br = new BufferedReader(new FileReader(selector.getSavepath()+uname));
		} catch(Exception e){
			
		}
		down = new MulImpl(htmlstack,new DownloadDoImpl(br, bw, factory, error));
		pro = new MulImpl(prostack, new ProductDoImpl(selector, error,htmset,down));
		first = new MulImpl(null,new FirstDoImpl(selector, factory, proset, pro));
	}

	/**
	 * product写url文件，写完之后notify downloadthread
	 * download读url下载，没有url时等待，直到product全部退出 
	 */
	public MulData(String name,BreakPoint bp){
	}
	
	public BreakPoint getBreakPoint() {
		return breakpoint;
	}

	public void setBreakPoint(BreakPoint breakpoint) {
		this.breakpoint = breakpoint;
	}

	public void data() throws Exception {
		// wait for fisrst step
		new Thread(first).start();
		for(int i=0;i<pthreadn;i++){
			new Thread(pro).start();
		}
		for(int i=0;i<dthreadn;i++){
			new Thread(down).start();
		}
	}

	public void setFactory(SpiderFactory factory) {
		this.factory = factory;
	}

	public void setSelector(Selector selector) {
		this.selector = selector;
	}

	public Selector getSelector() {
		return selector;
	}

	public void setName(String name) {
		this.name = name;
	}
}
