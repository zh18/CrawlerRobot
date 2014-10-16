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
import com.mm.spider.SpiderFactoryImpl;
import com.mm.stop.BreakPoint;
import com.mm.util.ReadSelector;

public class MulData implements Idata {

	protected Set<String> error = null;
	protected Selector selector = null;
	protected SpiderFactory factory = new SpiderFactoryImpl();
	protected Stack<String> prostack = null;
	protected String name;
	private int pthreadn=1;
	
	protected BreakPoint breakpoint = null;
	protected Set<String> proset = null;
	protected Set<String> htmset = null;
	
	protected Doable<String> downdo = null;
	protected Doable<String> prodo = null;
	protected Doable<String> firstdo = null;

	Hen<String> pro,first;
	
	public MulData(String name,boolean bp) {
		this.name = name;
		error = new HashSet<String>();
		proset = new HashSet<String>();
		htmset = new HashSet<String>();
		selector = ReadSelector.getSelector(name);
		prostack = new Stack<String>();
		pro = new Hen<String>(prostack);
		first = new Hen<String>(prostack);
		prodo = new ProductDoImpl(selector, error,htmset,null,factory);
		for(int i=0;i<pthreadn;i++){
			pro.addChicken(new MulImpl(prostack, prodo));
		}
		firstdo = new FirstDoImpl(selector, factory, pro);
		first.addChicken(new MulImpl(null, firstdo));
	}

	/**
	 * product写url文件，写完之后notify downloadthread
	 * download读url下载，没有url时等待，直到product全部退出 
	 */
	public MulData(String name,BreakPoint bp){
	}

	public void data() throws Exception {
		first.start();
		pro.start();
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
	
	public static void main(String[] args) throws Exception {
		Idata data = new MulData("T_ppd",false);
		data.data();
	}

	public BreakPoint getBreakPoint() {
		return breakpoint;
	}

	public void setBreakPoint(BreakPoint breakpoint) {
		this.breakpoint = breakpoint;
	}
}
