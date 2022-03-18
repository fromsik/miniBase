package com.kmsystem.common.exception;

public class ErrorDetailInfo {
	
	private String target;
	private String message;
	
	public ErrorDetailInfo(String target, String message) {
		this.target = target;
		this.message = message;
	}
	
	public String getTarget() {
		return target;
	}
	
	public String getMessage() {
		return message;
	}
}
