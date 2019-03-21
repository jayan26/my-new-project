package com.jayan.demo.service;

import java.util.Optional;

import com.jayan.demo.bean.LoginDetails;
import com.jayan.demo.bean.UserDetails;
import com.jayan.demo.repository.UserRegisterRepository;

public interface UserRegisterService{

	UserDetails insertUserDetails(UserDetails userDetails);

	Iterable<UserDetails> getAllDetails();

	Optional<UserDetails> getDetailsExistbyID(String userID);

	UserDetails getDetailsbyID(LoginDetails loginDetails);
	
	void deleteDetailsbyID(String userID);
	
}
