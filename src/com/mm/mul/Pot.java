/**
 *  mul Executor
 * 
 * @author zh
 * @version 0.1
 * @since Oct 17,2014
 */
package com.mm.mul;

import java.util.Queue;

public interface Pot<T> {
	
	public int MAXSIZE = 50;
	
	public int getId();
	
	public void setVisiable(boolean visiable);
	
	public void setBody(Queue<T> q);
	
	public Queue<T> getBody();
	
	public void put(T t);
	
	public T get();
	
	public boolean full();
	
	public void leak(Doable<T> doable);
}