package com.mm.data.model;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import com.mm.data.struct.Selector;
import com.mm.stop.BreakPoint;

public interface IFirstModel {
	
	public Set<String> first0() throws IOException;	
	
	public void init(Selector selector,BreakPoint breakpoint,Set<String> error);
}
