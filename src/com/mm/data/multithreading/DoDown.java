package com.mm.data.multithreading;

import java.io.BufferedWriter;
import java.io.File;
import java.util.Set;

import com.mm.data.Idata;
import com.mm.data.struct.Selector;
import com.mm.logger.Log;
import com.mm.mul.Doable;
import com.mm.spider.ISpider;
import com.mm.spider.SpiderFactory;
import com.mm.stop.BreakPoint;
import com.mm.util.FileC;
import com.mm.util.SystemUtil;

public class DoDown implements Doable<String> {

	private Set<String> error = null;
	private ISpider spider = null;
	private Selector selector = null;
	private BreakPoint bp = null;
	private FileC uDname = null;
	
	
	public DoDown(Selector selector, BreakPoint bp, SpiderFactory sf,
			Set<String> error) {
		spider = sf.getSpider();
		this.error = error;
		this.selector = selector;
		this.bp = bp;
	}

	public DoDown(Selector selector, BreakPoint bp, SpiderFactory sf,
			Set<String> error,FileC uDname) {
		spider = sf.getSpider();
		this.error = error;
		this.selector = selector;
		this.bp = bp;
		this.uDname = uDname;
	}
	
	
	public void x(String t) {
		try {
			String html = spider.spider(t);
			File f = null;
			if (html == null) {
				Log.logger.warn("html is null");
				error.add(t);
				return;
			}
			int rate = Integer.parseInt(bp.getRate());
			int record = rate / 10000;
			String path = selector.getSavepath() + Idata.hfname
					+ File.separator
					+ ((record) * 10000 + "~" + (record + 1) * 10000);
			f = new File(selector.getSavepath() + Idata.hfname);
			if (!f.exists())
				f.mkdir();
//			SystemUtil.appendFile(path, Idata.mark + t + Idata.mark + "\n"
//					+ html, Doable.class);
			if (null == uDname.getPath()) {
				uDname.setPath(path);
			}
			else if(!uDname.getPath().equals(path)) {
				uDname.close();
				uDname.setPath(path);
			}
			uDname.write(Idata.mark + t + Idata.mark + "\n" + html);
			bp.incRate();	
		} catch (Exception e) {
			Log.logger.warn("do down error", e);
		}
	}
}
