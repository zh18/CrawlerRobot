package com.mm.data.multithreading;

import java.util.Stack;

import org.apache.log4j.net.SyslogAppender;

import com.mm.exception.MulGoOutException;

public class MulImpl implements IMul<String>{
	
	protected Doable<String> doable = null;
	protected Stack<String> stack = null;
	protected boolean upend = false; 
	protected String url;
	protected static boolean end = false;
	
	public MulImpl(Stack<String> stack,Doable<String> doable) {
		this.stack = stack;
		this.doable = doable;
	}

	
	
	public void setStack(Stack<String> stack) throws RuntimeException {
		this.stack = stack;
		if(!stack.isEmpty()) {
			synchronized (this) {
				notify();
			}
		}
	}

	public String pop() {
		synchronized (doable) {
			if(stack.isEmpty()) return null;
			String temp = stack.pop();
			System.out.println("poped : "+stack.size() + temp);
			return temp;
		}
	}

	public void shoot(){
		synchronized (this) {
			notify();
		}
		upend = true;
	}
	
	public void run(){
		while(true){
			try {
				if(null == stack) doable.x(null);
				if(stack.isEmpty() && !upend) {
					synchronized (this) {
						System.out.println("waiting : "+stack.size());
						wait();
					}
				}
				if (stack.isEmpty() && upend) break;
				if(!stack.isEmpty()) {
					url = pop();
					doable.x(url);
				}
			}catch(MulGoOutException mgoe){
				break;
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}

	public void push(String ... s){
		synchronized (doable) {
			for(String temp:s){
				this.stack.push(temp);
			}
		}
		synchronized (this) {
			notify();
		}
	}
	
	public void setDoable(Doable<String> d) {
		this.doable = d;
	}

	public static void shootAll(){
		MulImpl.end = true;
	}
}
