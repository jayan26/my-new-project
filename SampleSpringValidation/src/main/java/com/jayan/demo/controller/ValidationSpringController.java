package com.jayan.demo.controller;

import java.util.Date;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validator;
import javax.validation.executable.ExecutableValidator;
import javax.validation.metadata.BeanDescriptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.jayan.demo.bean.ConfirmationCode;
import com.jayan.demo.bean.LoginDetails;
import com.jayan.demo.bean.PasswordConfirmation;
import com.jayan.demo.bean.PersonForm;
import com.jayan.demo.bean.UserDetails;
import com.jayan.demo.service.RegisterFormDAO;
import com.jayan.demo.service.UserDetailsDAO;

import net.bytebuddy.utility.RandomString;

@Controller
@SessionAttributes("name")
public class ValidationSpringController  {
	
	private static String generatedString = null;
	private static String latestUserID = null;
	private static int instances = 2;
	
	@Autowired
	private JavaMailSender sender;
	
	@Autowired
	RegisterFormDAO registerFormDAO;
	
	@Autowired
	UserDetailsDAO userDAO;
	
	@RequestMapping("/")
	public String display(LoginDetails loginDetails) {	
		return "Login";
	}
	///////////////		Logging in page process		////////////////
	@RequestMapping("/loginprocess")
	public String loginValidation(@Valid LoginDetails loginDetails, Errors errors, ModelMap model) {
		Optional<UserDetails> check = userDAO.getDetailsExistbyID(loginDetails.getLoginid());
		if(errors.hasErrors()) {
			return "Login";
		}else if(!check.isPresent()) {
			model.addAttribute("usererror", "user does not exists");
			return "Login";
		}else {
			UserDetails requiredUserDetails = userDAO.getDetailsbyID(loginDetails);
			if(requiredUserDetails.getPassword().equals(loginDetails.getPassword())) {
				String name = loginDetails.getLoginid();
				model.put("name", "Welcome "+name+"!!!");
				model.put("author", name);
				model.put("personForm", new PersonForm());
				return "form";
			}else {
				model.addAttribute("passvaliderror", "Password is wrong");
				return "Login";
			}
		}
	}
	
	//////////////		After Logging in details form	////////////////////
	@RequestMapping(value = "/formprocess", method=RequestMethod.POST)
	public String processForm(@Valid PersonForm personForm, Errors errors, Model model) {
		if(errors.hasErrors()) {
			model.addAttribute("result","failure");
			return "form";
		}
		registerFormDAO.insertDetails(personForm);
		model.addAttribute("result", "Successfully registered the student detail");
		return "form";
	}
	
	@RequestMapping("/registeruser")
	public String registerNewUSer(UserDetails userDetails, Errors errors, Model model) {
		model.addAttribute("passwordConfirmation", new PasswordConfirmation());
		return "UserRegister";
	}
	
	////////////		Registering new User	///////////////////////////////
	@RequestMapping(value="/registerprocess", method=RequestMethod.POST)
	public String registrationProcess(@Valid UserDetails userDetails,Errors error1, @Valid PasswordConfirmation passwordConfirmation, Errors errors, Model model) {
		System.out.println(passwordConfirmation.getConfpass());

		if(errors.hasErrors() || error1.hasErrors()) {
			if(!passwordConfirmation.getConfpass().equals(userDetails.getPassword())) {
				model.addAttribute("passworderror", "Password did not Match");
				return "UserRegister";
			}
			return "UserRegister";
		}else {
			if(!passwordConfirmation.getConfpass().equals(userDetails.getPassword())) {
				model.addAttribute("passworderror", "Password did not Match");
				return "UserRegister";
			}
			System.out.println(generatedString);
			
			///////////////		Sending Email Part(with code)	///////////////////////
			MimeMessage message = sender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message);
			try {
				generatedString = RandomString.make(6);
				helper.setTo(userDetails.getUseremail());
				helper.setText("Hello User!\n"+"Please use the below code to confirm the registeration!\n"
				+"Confirmation Code: "+generatedString);
				helper.setSubject("Sent from springboot");
			} catch (MessagingException e) {e.printStackTrace();
			model.addAttribute("error", "Invalid email");
			return "Login";
			}
			sender.send(message);
			/////////////////////////////////////////////////////////////////////////////
			latestUserID=userDetails.getUserid();
			userDAO.insertUserDetails(userDetails);
			model.addAttribute("confirmationCode",new ConfirmationCode());
			return "CodeCheck";
		}
	}
	
	@RequestMapping(value="/codeconfirm", method=RequestMethod.POST)
	public String checkConfirmationCode(@Valid ConfirmationCode confirmationCode,Errors errors , Model model) {
		
		if(errors.hasErrors()) {
			if(instances!=0) {
				instances--;
				return "CodeCheck";
			}else {
				userDAO.deleteDetailsbyID(latestUserID);
				System.out.println("deleted user: "+ latestUserID);
				model.addAttribute("deleted", "User is deleted due to 3 wrong codes");
				model.addAttribute("loginDetails",new LoginDetails());
				return "Login";
			}
		}else if(!confirmationCode.getCode().equals(generatedString)) {
			if(instances!=0) {
				model.addAttribute("wrong", "the entered code is wrong");
				instances--;
				return "CodeCheck";
			}else {
				userDAO.deleteDetailsbyID(latestUserID);
				model.addAttribute("loginDetails",new LoginDetails());
				model.addAttribute("deleted", "User is deleted due to 3 wrong codes");
				System.out.println("deleted user :"+ latestUserID);
				return "Login";
			}
		}
		model.addAttribute("registermessage","User Registered");
		model.addAttribute("loginDetails",new LoginDetails());
		return "Login";
	}
	
	@RequestMapping(value="/forgotpassword")
	public String forgotPassword(Model model) {
		model.addAttribute("loginDetails", new LoginDetails());
		return "ForgotPassword";
	}
	
	private static String genForgotPassCode=null;
	
	@RequestMapping(value="/processforgotpassword", method=RequestMethod.POST)
	public String processForgotPassword(LoginDetails loginDetails, Model model) {
		Iterable<UserDetails> userlist = userDAO.getAllDetails();
		for (UserDetails userDetails : userlist) {
			if(userDetails.getUseremail().equals(loginDetails.getLoginid())){
				MimeMessage message = sender.createMimeMessage();
				MimeMessageHelper helper = new MimeMessageHelper(message);
				try {
					genForgotPassCode = RandomString.make(6);
					helper.setTo(userDetails.getUseremail());
					helper.setText("Hello User!\n"+"Please use the below code to confirm the Password Change!\n"
					+"Confirmation Code: "+genForgotPassCode);
					helper.setSubject("Sent from springboot");
					sender.send(message);
					
				} catch (MessagingException e) {e.printStackTrace();}
				model.addAttribute("code",new LoginDetails());
				return "ForgotPassConf";
			}
		}
		model.addAttribute("error", "The userid is not on the list");
		return "ForgotPassword";
	}
	
	@RequestMapping(value="/codeValidation", method=RequestMethod.POST)
	public String ForgotPassCodeValidate(LoginDetails loginDetails, Model model) {
		if(loginDetails.getLoginid().equals(genForgotPassCode)) {
			model.addAttribute("pass1", new PasswordConfirmation());
			model.addAttribute("pass2", new PasswordConfirmation());
			return "ChangePassword";
		}else {
			model.addAttribute("code", new LoginDetails());
			model.addAttribute("wrong","The code you have entered is wrong. Please do check it");
			return "ForgotPassConf";
		}
	}
	
	@RequestMapping(value="/ValidatePassChange", method=RequestMethod.POST)
	public String passChangeValidate(PasswordConfirmation pass1, Model model) {
		
			
			//change in the database
			
			model.addAttribute("success", "The password has been successfully changed");
			model.addAttribute("loginDetails", new LoginDetails());
			return "Login";
		
	}
	
}
