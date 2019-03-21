package com.jayan.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jayan.demo.bean.PersonForm;
import com.jayan.demo.repository.RegisterRepository;

@Service
public class RegisterFormDAO implements RegisterService{

	@Autowired
	RegisterRepository registerRepository;
	
	@Override
	public PersonForm insertDetails(PersonForm personform) {
				
		return registerRepository.save(personform);
	}

	

}
