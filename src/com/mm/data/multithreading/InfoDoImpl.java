package com.mm.data.multithreading;

import java.io.IOException;

import com.mm.data.FBase;
import com.mm.data.Idata;
import com.mm.data.struct.Selector;
import com.mm.util.SystemUtil;

public class InfoDoImpl implements Doable<String> {
	
	private Selector selector = null;
//	private IMul<String> imul = null;
	private FBase fbase = null;
	
	public InfoDoImpl(Selector selector,FBase fbase) {
		this.selector = selector;
		this.fbase = fbase;
	}
	
	public void x(String s) throws IOException {
		if(selector.getClassify().equals("#")){
			SystemUtil.appendFile(selector.getSavepath()+Idata.iname, fbase.getMessage(s).toString());
		}
		else {
			SystemUtil.appendFile(selector.getSavepath()+Idata.iname, fbase.getMessage(s,selector.getSavepath()+Idata.tname).toString());
		}
	}
	
	
}
