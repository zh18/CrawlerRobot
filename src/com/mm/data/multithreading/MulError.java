package com.mm.data.multithreading;

import java.util.HashSet;
import java.util.Set;

import com.mm.data.Idata;
import com.mm.data.struct.Selector;
import com.mm.mul.Dispatcher;
import com.mm.mul.impl.DispatcherImpl;
import com.mm.spider.SpiderFactory;
import com.mm.stop.BreakPoint;

public class MulError implements Idata{
	
	private BreakPoint bp = null;
	private Selector selector = null;
	private SpiderFactory sf = null;
	private String name;
	private Set<String> error = null;
	
	public MulError(){
		error = new HashSet<String>();
	}
	
	public BreakPoint getBreakPoint() {
		return bp;
	}

	public void setBreakPoint(BreakPoint breakpoint) {
		this.bp = breakpoint;
	}

	public void data() throws Exception {
		Dispatcher<String> pro = new DispatcherImpl<String>();
		Dispatcher<String> fist = new DispatcherImpl<String>();
		Dispatcher<String> download = new DispatcherImpl<String>();
		
	}

	public void setFactory(SpiderFactory factory) {
	}

	public void setSelector(Selector selector) {
	}

	public Selector getSelector() {
		
		return null;
	}

	public void setName(String name) {
	}

	public void setNums(int nums) {
	}

	public void setError(Set<String> error) {
	}

	
	
}
