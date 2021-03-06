/**
 * @author zh
 * @since Oct 17,2014
 * @version 0.1
 */
package com.mm.mul.impl;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import com.mm.mul.Doable;
import com.mm.mul.Pot;
import com.sun.swing.internal.plaf.synth.resources.synth;

public class PotImpl<T> implements Pot<T>{

	private int id = 0;
	private boolean visiable = true;
	private Doable<T> doable = null;
	private Queue<T> body = null; 
	private Set<T> reset = null;
	
	public PotImpl(int id,Doable<T> doable){
		this.id = id;
		this.doable = doable;
		body = new LinkedList<T>();
	}
	
	public PotImpl(int id,Doable<T> doable,Set<T> reset){
		this.id = id;
		this.doable = doable;
		body = new LinkedList<T>();
		this.reset = reset;
	}
	
	public int getId() {
		return id;
	}

	public boolean isEmpty(){
		if(body!=null){
			return body.isEmpty();
		}
		return true;
	}
	
	public void setVisiable(boolean visiable) {
		this.visiable = visiable;
	}

	
	public void setBody(Queue<T> q) {
		if(visiable)
			this.body = q;
	}

	public Queue<T> getBody() {
		return body;
	}

	
	public void put(T t) {
		if(visiable && null != body) {
			addEle(t);
			synchronized (this) {
				try {
					this.notify();
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
	}

	public T get() {
		T result = null;
		if(!body.isEmpty())
			result = getEle();
		return result;
	}

	public boolean full(){
		if(null != body)
			return body.size() == Pot.MAXSIZE;
		return false;
	}
	
	public void leak(Doable<T> doable) {
		this.doable = doable;
	}
	
	public void run(){
		T t = null;
		while(visiable || !body.isEmpty()){
			if(body.isEmpty()){
				try {
					synchronized (this) {
						this.wait();
					}
				}catch(InterruptedException e){
					e.printStackTrace();
				}
			}
			t = get(); 
			doable.x(t);
		}
	}
	
	public synchronized void addEle(T t){
		body.add(t);
	}
	
	public synchronized T getEle(){
		return body.poll();
	}
	
	public boolean visiable(){
		return visiable;
	}
}