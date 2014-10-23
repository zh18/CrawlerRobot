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

public class MulData implements Idata {

	Selector selector = null;
	BreakPoint breakpoint = null;
	SpiderFactory sf = new SpiderFactoryImpl();
	String name = null;
	Set<String> error = null;
	Doable<String> doable = null;
	Dispatcher<String> dispatcher = new DispatcherImpl<String>();
	private int nums = 5;

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
			System.out
					.println("there is no " + breakpoint.getPname() + " file");
			return;
		}
		Thread dis = new Thread(dispatcher);
		breakpoint.setTotla(SystemUtil.getLineOfFile(selector.getSavepath()
				+ uname));
		if (breakpoint.getRate().trim().equals("")) {
			breakpoint.setRate("0");
		}
		BufferedReader br = null;
		if (breakpoint.getPname().equals(Idata.PRODUCT)) {
			doable = new DoPro(selector, breakpoint, sf, error);
			br = new BufferedReader(new FileReader(selector.getSavepath()
					+ fname));
		} else if (breakpoint.getPname().equals(Idata.DOWNLOAD)) {
			doable = new DoDown(selector, breakpoint, sf, error);
			br = new BufferedReader(new FileReader(selector.getSavepath()
					+ uname));
		}
		for (int i = 0; i < nums; i++) {
			dispatcher.addPot(new PotImpl<String>(i, doable));
		}
		dis.start();
		String line = null;

		int skip = 0;
		// 从文件中获取要工作的序列
		while ((line = br.readLine()) != null) {
			if (breakpoint.getRate().trim().equals("")) {
				breakpoint.setRate("0");
			}
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
		dis.join();
		SystemUtil.writeColl(error, selector.getSavepath() + ename);
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
}
