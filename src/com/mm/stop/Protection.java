package com.mm.stop;

public interface Protection {

	public void save(BreakPoint breakpoint);
	
	public void delete(BreakPoint breakpoint);
	
	public BreakPoint recover(String name);

}
