
/**
 * @author public
 *
 */
package com.mm.data.model;

import java.io.IOException;
import java.util.Set;

import com.mm.data.struct.Selector;
import com.mm.spider.SpiderFactory;
import com.mm.stop.BreakPoint;

public interface IProductModel {
	
	public void getPro0(String url) throws IOException;

	public void init(Selector selector,BreakPoint breakpoint,Set<String> error);
}