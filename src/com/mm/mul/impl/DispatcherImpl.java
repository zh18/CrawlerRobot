package com.mm.mul.impl;

import java.util.Collection;
import java.util.List;
import java.util.Stack;

import com.mm.mul.Dispatcher;
import com.mm.mul.Pot;

/**
 * default dispatcher impl
 * 
 * @author zh
 * @version 0.1
 * @since Oct 17,2014
 */

public class DispatcherImpl<T> implements Dispatcher<T>{

	
	private List<Pot<T>> pots = null;
	private Stack<T> cins = null;
	private boolean alive = true;
	
	public DispatcherImpl(){
		cins = new Stack<T>();
	}
	
	public boolean addPot(Pot<T> pot) {
		if(null != pots) {
			if (pots.size() < Dispatcher.MAXPOT) {
				pots.add(pot);
				new Thread(pot).start();
				return true;
			}
		}
		return false;
	}

	public boolean full(){
		if(null != cins){
			return cins.size() == MAXCINS;
		}
		return false;
	}
	
	public boolean cutPot(int id) {
		if (id == -1 && null != pots && pots.size()>0){
			pots.get(potSize()-1).setVisiable(false);
			pots.remove(pots.size()-1);
		}
		return false;
	}

	public void run() {
		while(alive || !cins.isEmpty()){
			if(cins.isEmpty()){
				try {
					synchronized (this) {
						wait();
					}
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			dispatcher();
		}
	}
	
	public void dispatcher() {
		for(Pot<T> p:pots){
			if(!p.full() && !pots.isEmpty()){
				p.put(cins.pop());
			}
		}
	}

	public  void cin(T t) {
		if(null != cins){
			synchronized (cins) {
				cins.add(t);
			}
			synchronized (this) {
				try {
					this.notify();
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
	}

	public void cin(Collection<T> col) {
		if(null != cins){
			synchronized(cins){
				cins.addAll(col);
			}
			synchronized (this) {
				try {
					this.notify();
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
	}

	public boolean clearPots() {
		if(null != pots) {
			for(Pot<T> p:pots){
				p.setVisiable(false);
			}
			pots.clear();
			return true;
		}
		return false;
	}

	public int potSize() {
		if(null != pots) 
			return pots.size();
		return -1;
	}
	
	public void live(boolean alive){
		this.alive = alive;
	}
}
