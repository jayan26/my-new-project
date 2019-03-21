package com.jayan.demo.bean;

import javax.validation.constraints.NotEmpty;

public class ConfirmationCode {

	@NotEmpty
	private String code;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	
	
}
