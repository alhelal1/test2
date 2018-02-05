package com.bjit.springBoot.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bjit.springBoot.model.User;
import com.bjit.springBoot.userDao.UserDao;

@Controller
public class LoginController {

	@Autowired
	private UserDao userDao;

	public UserDao getUserDao() {
		return userDao;
	}

	@Autowired
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
	
	private List<User> allUser = new ArrayList<>();

	@RequestMapping(value = "/doLogin", method = RequestMethod.POST)
	public String doLogin(HttpSession session, Model model, User user) {
		// session.setAttribute("email", model.s);
		// System.out.println(user);
		User chkUser = userDao.login(user.getEmail(), user.getPassword());
		
		if (chkUser != null) {
			model.addAttribute("name", chkUser.getName());
			System.out.println(chkUser);
			if (chkUser.getUserType().equals("admin")) {
				session.setAttribute("suser", chkUser);
				return "admin";
			} else if (chkUser.getUserType().equals("user")) {
				session.setAttribute("suser", chkUser);
				return "user";
			} else {
				session.setAttribute("error", "Invalid user");
				return "homee";
			}
		}else {
			session.setAttribute("error", "User doesnot exist in the database.");
			return "homee";
		}
	}
	
	@RequestMapping(value="/addUser")
	public String addUser(User user) {
		return "addUser";
	}
	
	@RequestMapping(value="/manageUsers", method = RequestMethod.GET)
	public String manageUsers(Model model) {
		List<User> uList = userDao.getAllUsers();
		allUser = uList;
		model.addAttribute("userList", uList);
		//System.out.println(uList);
		return "manageUsers";
	}
	
	@RequestMapping(value="/updateUserForm/{userId}", method = RequestMethod.GET)
	public String updateUserForm(@PathVariable("userId") int userId, Model model) {
		System.out.println(userId);
//		User updatedUser = null;
//		for (User user : allUser) {
//			if(user.getId() == userId) {
//				updatedUser = user;
//				break;
//			}
//				
//		}
		User updatedUser = userDao.getUserById(userId);
		model.addAttribute("updatedUser", updatedUser);
		return "updateUserForm";
	}
	
	@PostMapping("/saveUpdatedForm")
	public String saveUpdatedForm(HttpSession session ,Model model, User user) {
		//System.out.println(user);
		
		if(user.getUserType() == null) {
			session.removeAttribute("suser");
			user.setUserType("user");
			session.setAttribute("suser", user);
			userDao.update(user);
			return "user";
		}else {
			userDao.update(user);
			List<User> allUser = userDao.getAllUsers();
			model.addAttribute("userList", allUser);
			return "manageUsers";
		}	
	}
	
	@GetMapping("/deleteUser/{userId}")
	public String deleteUser(@PathVariable("userId") int userId, Model model) {
		userDao.deleteUser(userId);
		return manageUsers(model);
	}
	
	@PostMapping("/insertNewUser")
	public String addUserForm(@Valid User user, BindingResult result, RedirectAttributes redirectAttributes) {
		if(result.hasErrors()) {
			
			return "addUser";
		}else {
			userDao.addUser(user);
			return "admin";
		}
	}
	
	@GetMapping("/loggedout")
	public String showLoggedOut(HttpSession session) {
		session.invalidate();
		return "homee";
	}
	
	
	
}
