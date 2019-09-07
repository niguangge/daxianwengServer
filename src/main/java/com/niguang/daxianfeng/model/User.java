package com.niguang.daxianfeng.model;

import java.util.UUID;

import lombok.Data;

@Data
public class User {
	private String userId;
	private String wxId;
	private String nickName;
	private int level;
	private int curExp;

	public User(String userId, String wxId, String nickName, int level, int curExp) {
		super();
		this.userId = userId;
		this.wxId = wxId;
		this.nickName = nickName;
		this.level = level;
		this.curExp = curExp;
	}

}
