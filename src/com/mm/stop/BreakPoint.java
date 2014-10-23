package com.mm.stop;

public class BreakPoint {
	
	private String breakreason;
	private String time;
	private String wname;
	private String pname;
	private String rate;
	private String done;   /* 0 is done , 1 have not */
	private transient long total;
	private transient boolean newfile = true;
	
	public BreakPoint(){
	}
	
	public BreakPoint(String wname,String pname,String rate){
		this.wname = wname;
		this.pname = pname;
		this.rate = rate;
	}
	
	public BreakPoint(String breakreason, String time, String wname,
			String pname, String rate) {
		this.breakreason = breakreason;
		this.time = time;
		this.wname = wname;
		this.pname = pname;
		this.rate = rate;
	}
	
	public void recover(String time, String wname,
			String pname, String rate){
		this.time = time;
		this.wname = wname;
		this.pname = pname;
		this.rate = rate;
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
		return String.format("%12s","breakreason")+" = "+breakreason+"\n"+
			   String.format("%12s","time")+" = "+time+"\n"+
			   String.format("%12s","scheme name")+" = "+wname+"\n"+
			   String.format("%12s","proces name")+" = "+pname+"\n"+
			   String.format("%12s","rate")+" = "+rate+"\n";
	}

	public String getDone(){
		return done;
	}
	public void setDone(String done){
		this.done = done;
	}
	public long getTotal(){
		return total;
	}
	public void setTotla(long total){
		this.total = total;
	}

	public boolean getNewfile() {
		return newfile;
	}

	public void setNewfile(boolean newfile) {
		this.newfile = newfile;
	}

	public void setTotal(long total) {
		this.total = total;
	}
	
	public synchronized void incRate(){
		int temp = Integer.parseInt(rate);
		rate = String.valueOf(++temp);
	}
	
}
