package com.mm.stop;

public class BreakPoint {
	
	private String breakreason;
	private String time;
	private String wname;
	private String pname;
	private String rate;
	private String done;   /* 0 is done , 1 have not */
	
	public BreakPoint(String breakreason, String time, String wname,
			String pname, String rate,String done) {
		this.breakreason = breakreason;
		this.time = time;
		this.wname = wname;
		this.pname = pname;
		this.rate = rate;
		this.done = done;
	}
	public String getBreakreason() {
		return breakreason;
	}
	public void setBreakreason(String breakreason) {
		this.breakreason = breakreason;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getWname() {
		return wname;
	}
	public void setWname(String wname) {
		this.wname = wname;
	}
	public String getPname() {
		return pname;
	}
	public void setPname(String pname) {
		this.pname = pname;
	}
	public String getRate() {
		return rate;
	}
	public void setRate(String rate) {
		this.rate = rate;
	}
	
	
	public String toString(){
		return "breakreason="+breakreason+"\n"+
			   "time="+time+"\n"+
			   "wname="+wname+"\n"+
			   "pname="+pname+"\n"+
			   "rate="+rate+"\n";
	}

	public String getDone(){
		return done;
	}
	public void setDone(String done){
		this.done = done;
	}
}
