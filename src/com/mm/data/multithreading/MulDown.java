package com.mm.data.multithreading;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashSet;
import java.util.Set;

import com.mm.data.Idata;
import com.mm.data.struct.Selector;
import com.mm.mul.Dispatcher;
import com.mm.mul.Doable;
import com.mm.mul.impl.DispatcherImpl;
import com.mm.mul.impl.PotImpl;
import com.mm.spider.SpiderFactory;
import com.mm.spider.SpiderFactoryImpl;
import com.mm.stop.BreakPoint;
import com.mm.util.SystemUtil;

public class MulDown implements Idata {

	Selector selector = null;
	BreakPoint breakpoint = null;
	SpiderFactory sf = new SpiderFactoryImpl();
	String name = null;
	Set<String> error = null;
	Doable<String> downdo = new DownDo(selector, breakpoint, sf, error);
	Dispatcher<String> downdis = new DispatcherImpl<String>();
	private int nums = 5;
	
	public MulDown(){
		name = this.getClass().getName();
		error = new HashSet<String>();
	}
	
	public BreakPoint getBreakPoint() {
		return breakpoint;
	}

	public void setBreakPoint(BreakPoint breakpoint) {
		this.breakpoint = breakpoint;
	}

	public void data() throws Exception {
		if(!check(Idata.DOWNLOAD)) {
			System.out.println("there is no url file");
			return ;
		}
		// 开启downdispatcher服务
		Thread dis = new Thread(downdis);
		for(int i=0;i<nums;i++){
			downdis.addPot(new PotImpl<String>(i, downdo));
		}
		dis.start();
		BufferedReader br = new BufferedReader(new FileReader(selector.getSavepath()+uname));
		String line = null;
		while((line = br.readLine()) != null){
			if(downdis.full()){
				try {
					Thread.sleep(50);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			else {
				downdis.cin(line);
			}
		}
		br.close();
		downdis.live(false);
	}

	public void setFactory(SpiderFactory factory) {
		// do nothing
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

	public  boolean check(String part){
		if(part.equals(PRODUCT))
			return check0(selector.getSavepath()+fname);
		else if(part.equals(DOWNLOAD))
			return check0(selector.getSavepath()+uname);
		return false;
	}
	
	private boolean check0(String name){
		File file = new File(name);
		if (file.exists())
			return true;
		return false;
	}
	
	public void setNum(int num){
		this.nums = num;
	}
	
}
