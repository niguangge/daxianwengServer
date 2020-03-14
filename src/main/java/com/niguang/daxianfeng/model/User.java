package com.niguang.daxianfeng.model;

import lombok.Data;

@Data
public class User {
	private String userId;
	private String wxId;
	private String nickName;
	private int level;
	private int curExp;
	private String avatarUrl;

	public User(String userId, String wxId, String nickName, int level, int curExp, String avatarUrl) {
		super();
		this.userId = userId;
		this.wxId = wxId;
		this.nickName = nickName;
		this.level = level;
		this.curExp = curExp;
		this.avatarUrl = avatarUrl;
	}

}
