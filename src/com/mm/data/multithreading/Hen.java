package com.mm.data.multithreading;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Hen<T> {

	private List<IMul<T>> chickens = null;
	private Stack<T> stack = null;
	
	public Hen(Stack<T> stack){
		chickens = new ArrayList<IMul<T>>();
		this.stack = stack;
	}
	
	public void addChicken(IMul<T> imul){
		this.chickens.add(imul);
	}
	
	public void eat(Stack<T> stack) throws RuntimeException{
		for(IMul<T> i:chickens){
			i.setStack(stack);
		}
	}
	
	public void push(T ... t){
		for(T temp:t){
			stack.add(temp);
		}
		eat(stack);
	}
	
	public void shoot() {
		for(IMul<T> i:chickens){
			i.shoot();
		}
	}
	
	public void start(){
		for(IMul<T> i:chickens){
			new Thread(i).start();
		}
	}
}
