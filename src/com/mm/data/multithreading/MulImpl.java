package com.mm.data.multithreading;

import java.util.Stack;

import com.mm.exception.MulGoOutException;

public class MulImpl implements IMul<String>{
	
	protected Doable<String> doable = null;
	protected Stack<String> stack = null;
	protected boolean upend; 
	protected String url;
	protected static boolean end = false;
	private Object lock = new Object();
	
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
		System.out.println("will poping");
		synchronized (doable) {
			if(stack.isEmpty()) return null;
			String temp = stack.pop();
			System.out.println("poped : "+temp);
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
			//如果stack为空，说明这个东西，不许要上一层的数据。
			if(stack != null) {
				if(stack.isEmpty() && (upend || end)) break;
				if(stack.isEmpty() || (url = this.pop()) == null) {
					synchronized (this) {
						try {
							System.out.println("stack size"+stack.size()+" wait");
							wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}
			try {
				// when we got an exception we exit
				doable.x(url);
			}catch(MulGoOutException mgoe){
				break;
			}catch(RuntimeException re){
				re.printStackTrace();
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
				System.out.println("we add "+s+" and will be notify");
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
