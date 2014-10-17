package com.mm.mul;

import java.util.Collection;

/**
 * pot's data dispatcher 
 * @author zh
 * @version 0.1
 * @since Oct 17 , 2014
 */

public interface Dispatcher<T>  extends Runnable{
	
	public int MAXPOT = 20;
	public int MAXCINS = 20000;
	
	public boolean addPot(Pot<T> pot);

	public boolean clearPots();
	
	public int potSize();
	
	public boolean cutPot(int id);
	
	public void dispatcher();
	
	public void cin(T t);
	
	public void cin(Collection<T> col);
	
	public void live(boolean alive);
	
	public boolean full();
}
