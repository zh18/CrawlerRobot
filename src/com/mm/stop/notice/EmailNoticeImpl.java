package com.mm.stop.notice;

public class EmailNoticeImpl implements IStopNotice {

	/**
	 * dependency inject
	 */
	private  String target;
	
	
	@Override
	public boolean notice(String message) {
		// TODO Auto-generated method stub
		return false;
	}

	
	public void setTarget(String target){
		this.target = target;
	}
	public String getTarget(){
		return target;
	}
	
}
