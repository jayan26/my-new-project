package com.jayan.demo.repository;

import org.springframework.data.repository.CrudRepository;
import com.jayan.demo.bean.PersonForm;

public interface RegisterRepository extends CrudRepository<PersonForm, Integer>{

	
	
}
