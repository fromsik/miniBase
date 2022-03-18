package com.kmsystem.common.exception;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ErrorInfo {
	
	private String message;

	private int status;

	private String error;
	
	private final List<ErrorDetailInfo> details = new ArrayList<ErrorDetailInfo>();
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	public void addDetailInfo(String target, String message) {
		details.add(new ErrorDetailInfo(target, message));
	}
	
	public List<ErrorDetailInfo> getDetails(){
		return details;
	}
	
	
}
