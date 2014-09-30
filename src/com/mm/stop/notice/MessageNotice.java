package com.mm.stop.notice;

public class MessageNotice implements IStopNotice {

	private String target;
	
	@Override
	public boolean notice(String message) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}
}
