package com.mm.data.multithreading;

import java.util.Stack;

public interface IMul<T> extends Runnable{
	
	public void setStack(Stack<T> stack);
	
	public T pop();
	
	public void push(T ... t);
	
	public void run();
	
	public void setDoable(Doable<T> d);
	
	public void shoot();
}
