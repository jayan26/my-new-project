package com.jayan.demo.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Entity
@Table(name="personform")
public class PersonForm {
	
	@NotEmpty(message="please enter the firstname")
	private String firstname;
	
	@NotEmpty(message="please enter the lastname")
	private String lastname;
	
	@NotEmpty(message="please enter the lastname")
	private String department;
	
	@NotEmpty(message="please enter the email")
	@Email
	private String email;
	
	@NotEmpty(message="please enter the phone number")
	@Id
	@Pattern(regexp = "((\\(\\d{3}\\) ?)|(\\d{3}-))?\\d{3}-\\d{4}")
	private String mobile;
	
	@Column(name="option1")
	@NotEmpty(message="the option cannto be empty")
	private String option;

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getOption() {
		return option;
	}

	public void setOption(String option) {
		this.option = option;
	}

}
