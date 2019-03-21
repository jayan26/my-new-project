package com.jayan.demo.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jayan.demo.bean.LoginDetails;
import com.jayan.demo.bean.UserDetails;
import com.jayan.demo.repository.UserRegisterRepository;

@Service
public class UserDetailsDAO implements UserRegisterService{

	@Autowired
	UserRegisterRepository userRepository;
	
	@Override
	public UserDetails insertUserDetails(UserDetails userDetails) {
		return userRepository.save(userDetails);
	}

	@Override
	public Iterable<UserDetails> getAllDetails() {
		return userRepository.findAll();
	}
	
	@Override
	public Optional<UserDetails> getDetailsExistbyID(String userID) {
		return userRepository.findById(userID);
	}

	@Override
	public UserDetails getDetailsbyID(LoginDetails loginDetails) {   //implement here
		UserDetails selectedUser = null;
		
		Iterable<UserDetails> allDetails = this.getAllDetails();
		for (UserDetails userDetails : allDetails) {
			if(userDetails.getUserid()==loginDetails.getLoginid()) {
				selectedUser= userDetails;
				break;
			}
		}
		
		return selectedUser;
	}

	@Override
	public void deleteDetailsbyID(String userID) {
		userRepository.deleteById(userID);
	}
	
	
	
}
