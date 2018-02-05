package com.bjit.springBoot.model;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.stereotype.Component;

@Component
public class User {

	private int id;
	
	@Size(max = 20, min = 3, message = "Name entered is invalid. It must be between 3 and 20 characters.")
	private String name;
	
	@Email(message = "Invalid email! Please enter valid email.")
	@Size(max=120, min=8, message= "Email must be between 8 and 120 character long.")
	private String email;
	
	@Size(max = 40, min = 8, message = "It must be between 8 and 40 characters.")
	private String password;
	
	@NotEmpty(message = "User Tupe field cann't be empty.")
	private String userType;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", email=" + email + ", password=" + password + ", userType="
				+ userType + "]";
	}
}
