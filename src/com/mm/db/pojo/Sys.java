package com.mm.db.pojo;

import java.util.Date;

public class Sys {
	
	 private int id;
	 private long rotine;
	 private long last;
	 private Date recent;
	 private String root,fname,pname,hname;
	 
	public Sys(int id, long rotine, long last, Date recent, String root,
			String fname, String pname, String hname) {
		super();
		this.id = id;
		this.rotine = rotine;
		this.last = last;
		this.recent = recent;
		this.root = root;
		this.fname = fname;
		this.pname = pname;
		this.hname = hname;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public long getRotine() {
		return rotine;
	}

	public void setRotine(long rotine) {
		this.rotine = rotine;
	}

	

	public long getLast() {
		return last;
	}

	public void setLast(long last) {
		this.last = last;
	}

	public Date getRecent() {
		return recent;
	}

	public void setRecent(Date recent) {
		this.recent = recent;
	}

	public String getRoot() {
		return root;
	}

	public void setRoot(String root) {
		this.root = root;
	}

	public String getFname() {
		return fname;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}

	public String getPname() {
		return pname;
	}

	public void setPname(String pname) {
		this.pname = pname;
	}

	public String getHname() {
		return hname;
	}

	public void setHname(String hname) {
		this.hname = hname;
	}
	 
	 
	 
}
