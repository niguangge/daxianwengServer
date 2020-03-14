package com.niguang.daxianfeng.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.niguang.daxianfeng.model.User;
import com.niguang.daxianfeng.service.UserService;

@RestController
@RequestMapping("/user/")
@CrossOrigin
public class UserController {
	@Autowired
	private UserService userService;

	@RequestMapping(value = "getUser", method = RequestMethod.GET)
	public @ResponseBody User addUser(Model model, HttpServletRequest request,
			@RequestParam(value = "userId", defaultValue = "") String userId,
			@RequestParam(value = "avatarUrl", defaultValue = "") String avatarUrl,
			@RequestParam(value = "nickName", defaultValue = "") String nickName) throws Exception {
		User user = userService.getUserByWxId(userId);
		if (user == null) {
			user = userService.createUser(userId, nickName, avatarUrl);
			userService.addUser(user);
		} else {
			userService.updateUser(user, userId, nickName, avatarUrl);
		}
		return user;
	}

	@RequestMapping(value = "addExp", method = RequestMethod.GET)
	public @ResponseBody User addExp(Model model, HttpServletRequest request,
			@RequestParam(value = "userId", defaultValue = "") String userId,
			@RequestParam(value = "exp", defaultValue = "") int exp) throws Exception {
		User user = userService.getUserByWxId(userId);
		if (user == null) {
			return null;
		} else {
			userService.addExp(user, exp);
		}
		return user;
	}

}
