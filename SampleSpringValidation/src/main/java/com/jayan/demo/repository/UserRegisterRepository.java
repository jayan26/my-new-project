package com.jayan.demo.repository;

import org.springframework.data.repository.CrudRepository;

import com.jayan.demo.bean.UserDetails;

public interface UserRegisterRepository extends CrudRepository<UserDetails, String>{

}
