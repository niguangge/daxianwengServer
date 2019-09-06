package com.niguang.daxianfeng.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.niguang.daxianfeng.model.Room;
import com.niguang.daxianfeng.model.User;

@Service("userService")
public class UserService {
	float expMulti = 1;

	public UserService() {
		init();
	}

	private void init() {
		expMulti = 1;
	}

	public User createUser(String wxId, String nickName) {
		if (wxId != null && !wxId.equals("")) {
			User user = new User(wxId, nickName);
			return user;
		} else {
			return null;
		}
	}

}
