package com.mm.data.multithreading;

import java.util.Stack;

public class MulImpl implements IMul<String>{
	
	protected Doable<String> doable = null;
	protected Stack<String> stack = null;
	protected boolean upend; 
	protected String url;
	
	
	public MulImpl(Stack<String> stack,Doable<String> doable) {
		this.stack = stack;
		this.doable = doable;
	}

	public void setStack(Stack<String> stack) {
		this.stack = stack;
	}

	public synchronized String pop() {
		return stack.pop();
	}

	public void shoot(){
		notify();
		upend = true;
	}
	
	public void run(){
		while(true){
			if(stack.isEmpty() && upend) break;
			if(stack.isEmpty()) {
				try {
					wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			url = this.pop();
			try {
				// when we got an exception  we exit
				doable.x(url);
			}catch(Exception e){
				//do something
			}
		}
	}

	public void push(String ... s){
		for(String temp:s){
			this.stack.push(temp);
		}
		notify();
	}
	
	public void setDoable(Doable<String> d) {
		this.doable = d;
	}

	
}
