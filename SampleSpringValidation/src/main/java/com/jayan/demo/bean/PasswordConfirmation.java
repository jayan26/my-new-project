package com.jayan.demo.bean;

import javax.validation.constraints.NotEmpty;

public class PasswordConfirmation {

	@NotEmpty
	private String confpass;

	public String getConfpass() {
		return confpass;
	}

	public void setConfpass(String confpass) {
		this.confpass = confpass;
	}

}
