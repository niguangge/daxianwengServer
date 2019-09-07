package com.niguang.daxianfeng.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.niguang.daxianfeng.mapper.UserMapper;
import com.niguang.daxianfeng.model.User;

@Service("userService")
public class UserService {
	@Autowired
	private UserMapper userMapper;
	private float expMulti = 1;
	private int[] levelExps = { 0, 100, 400, 900, 1600, 2500, 3600, 4900, 6400, 8100 };

	public UserService() {
		init();
	}

	private void init() {
		expMulti = 1;
	}

	public User createUser(String wxId, String nickName) {
		if (wxId != null && !wxId.equals("")) {
			String userId = UUID.randomUUID().toString().replaceAll("-", "");
			User user = new User(userId, wxId, nickName, 0, 0);
			return user;
		} else {
			return null;
		}
	}

	public void addUser(User user) {
		userMapper.addUser(user);
	}

	public User getUserByWxId(String wxId) {
		return userMapper.getUserByWxId(wxId);
	}

	public void addExp(User user, int exp) {
		exp = user.getCurExp() + (int) (exp * expMulti);
		int level = updateExpAndLevel(user, exp);
		userMapper.addExp(user.getWxId(), exp, level);
	}

	private int updateExpAndLevel(User user, int exp) {
		user.setCurExp(exp);
		int level = 0;
		for (; level < levelExps.length; level++) {
			if (levelExps[level] <= exp) {
				exp -= levelExps[level];
			} else {
				break;
			}
		}
		user.setLevel(level);
		return level;
	}
}
