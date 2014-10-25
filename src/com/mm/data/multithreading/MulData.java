package com.mm.data.multithreading;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashSet;
import java.util.Set;

import com.mm.data.Idata;
import com.mm.data.struct.Selector;
import com.mm.logger.Log;
import com.mm.mul.Dispatcher;
import com.mm.mul.Doable;
import com.mm.mul.impl.DispatcherImpl;
import com.mm.mul.impl.PotImpl;
import com.mm.spider.SpiderFactory;
import com.mm.spider.SpiderFactoryImpl;
import com.mm.stop.BreakPoint;
import com.mm.util.FileC;
import com.mm.util.SystemUtil;

public class MulData implements Idata {

	private Selector selector = null;
	private BreakPoint breakpoint = null;
	private SpiderFactory sf = new SpiderFactoryImpl();
	private String name = null;
	private Set<String> error = null;
	private Doable<String> doable = null;
	private Dispatcher<String> dispatcher = new DispatcherImpl<String>();
	private int nums = 5;

	private FileC wUname = null;
	private FileC wUFname = null;
	private FileC wDname = null;
	
	public MulData() {
		error = new HashSet<String>();
	}

	public BreakPoint getBreakPoint() {
		return breakpoint;
	}

	public void setBreakPoint(BreakPoint breakpoint) {
		this.breakpoint = breakpoint;
	}

	public void data() throws Exception {
		if (!check(breakpoint.getPname())) {
			System.out.println("there is no " + breakpoint.getPname() + " file");
			return;
		}
//		Thread dis = new Thread(dispatcher);
		String process = breakpoint.getPname();
		if(process.equals(Idata.PRODUCT))
			breakpoint.setTotla(SystemUtil.getLineOfFile(selector.getSavepath()
					+ fname));
		else if(process.equals(Idata.DOWNLOAD))
			breakpoint.setTotla(SystemUtil.getLineOfFile(selector.getSavepath()
					+ uname));
		if (breakpoint.getRate().trim().equals("")) {
			breakpoint.setRate("0");
		}
		BufferedReader br = null;
		if(process.equals(Idata.PRODUCT)){
			br = new BufferedReader(new FileReader(selector.getSavepath()+fname));
//			wUname = new FileC(selector.getSavepath()+uname);
//			wUFname = new FileC(selector.getSavepath()+"url-first.txt");
		}
		else if(process.equals(Idata.DOWNLOAD)){
			br = new BufferedReader(new FileReader(selector.getSavepath()+uname));
			wDname = new FileC();
		}
		for (int i = 0; i < nums; i++) {
			if(process.equals(Idata.PRODUCT))
				dispatcher.addPot(new PotImpl<String>(i, new DoPro(selector, breakpoint,sf, error,wUname,wUFname)));
			else if(process.equals(Idata.DOWNLOAD))
				dispatcher.addPot(new PotImpl<String>(i, new DoDown(selector,breakpoint,sf,error,wDname)));
		}
//		dis.start();
		String line = null;

		
		//# 临时变量  record 
		int record = 0;
		
		int skip = 0;
		// 从文件中获取要工作的序列
		while ((line = br.readLine()) != null) {
			if (breakpoint.getRate().trim().equals("")) {
				breakpoint.setRate("0");
			}
			record ++;
			if (++skip < Integer.parseInt(breakpoint.getRate()))
				continue;
			if (dispatcher.full()) {
				try {
					Thread.sleep(50);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else
				dispatcher.cin(line);
		}
		br.close();
		dispatcher.live(false);
		dispatcher.doing();
		done();
		try {
			if(record < breakpoint.getTotal()){
				throw new RuntimeException("Don't finish but out");
			}
		}catch(Exception e){
			Log.logger.error("dont'finish but out "+record, e);
		}
		//关闭写入流
		if(null != wDname )
			wDname.close();
//		wDname.close();
//		wUFname.close();
//		wUname.close();
		SystemUtil.writeColl(error, selector.getSavepath() + ename);
	}

	public void addPots(){
		//first arg does not matter
		dispatcher.addPot(new PotImpl<String>(1, doable));
	}
	
	public void setFactory(SpiderFactory factory) {
		this.sf = factory;
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

	public boolean check(String part) {
		if (part.equals(PRODUCT))
			return check0(selector.getSavepath() + fname);
		else if (part.equals(DOWNLOAD))
			return check0(selector.getSavepath() + uname);
		return false;
	}

	private boolean check0(String name) {
		File file = new File(name);
		if (file.exists())
			return true;
		return false;
	}

	public void setNums(int nums) {
		if (nums > 0) {
			this.nums = nums;
		} else
			this.nums = 5;
	}

	public void setError(Set<String> error) {

	}
	
	public void done(){
		breakpoint.setPname(DONE);
	}
}
