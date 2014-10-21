package com.mm.mul.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import com.mm.mul.Dispatcher;
import com.mm.mul.Doable;
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
	private Doable<T> doable = null;
	private List<Thread> threadlist = null;
	private Set<T> reset = null;
	
	public DispatcherImpl(){
		cins = new Stack<T>();
		pots = new ArrayList<Pot<T>>();
		threadlist = new ArrayList<Thread>();
	}
	
	public DispatcherImpl(Set<T> reset){
		cins = new Stack<T>();
		pots = new ArrayList<Pot<T>>();
		threadlist = new ArrayList<Thread>();
		this.reset = reset;
	}
	
	public void setDoable(Doable<T> doable){
		this.doable = doable;
	}
	
	
	public boolean addPot(Pot<T> pot) {
		if(null != pots) {
			Thread temp = null;
			if (pots.size() < Dispatcher.MAXPOT) {
				pots.add(pot);
				temp = new Thread(pot);
				temp.start();
				threadlist.add(temp);
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
		cutAllPots();
		while(!potsLeaking()){
			try{
				Thread.sleep(500);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	public void dispatcher() {
		for(Pot<T> p:pots){
			if(!p.full() && !cins.isEmpty()){
				if(p.visiable())
					p.put(cins.pop());
			}
			if(cins.isEmpty()) break;
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
	
	public boolean potsLeaking(){
//		for(Pot<T> p:pots){
//			if(!p.isEmpty()) {
//				return false;
//			}
//		}
		for(Thread t:threadlist){
			if(t.isAlive()){
				return false;
			}
		}
		return true;
	}
	
	protected void cutAllPots(){
		for(Pot<T> p:pots){
			p.setVisiable(false);
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
