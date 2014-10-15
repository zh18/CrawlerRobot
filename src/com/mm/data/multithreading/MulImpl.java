package com.mm.data.multithreading;

import java.util.Stack;

public class MulImpl implements IMul<String>{
	
	protected Doable<String> doable = null;
	protected Stack<String> stack = null;
	protected boolean upend; 
	protected String url;
	protected static boolean end = false;
	
	public MulImpl(Stack<String> stack,Doable<String> doable) {
		this.stack = stack;
		this.doable = doable;
	}

	public void setStack(Stack<String> stack) {
		this.stack = stack;
	}

	public String pop() {
		synchronized (this) {
			return stack.pop();
		}
	}

	public void shoot(){
		notify();
		upend = true;
	}
	
	public void run(){
		while(true){
			//如果stack为空，说明这个东西，不许要上一层的数据。
			if(stack != null) {
				if(stack.isEmpty() && (upend || end)) break;
				if(stack.isEmpty()) {
					synchronized (doable) {
						try {
							wait();
							System.out.println("stack size"+stack.size()+" wait");
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
				url = this.pop();
			}
			try {
				// when we got an exception we exit
				doable.x(url);
			}catch(Exception e){
				break;
			}
		}
	}

	public void push(String ... s){
		synchronized (this) {
			for(String temp:s){
				this.stack.push(temp);
				System.out.println("we add "+s+" and will be notify");
			}
		}
		notify();
	}
	
	public void setDoable(Doable<String> d) {
		this.doable = d;
	}

	public static void shootAll(){
		MulImpl.end = true;
	}
}
