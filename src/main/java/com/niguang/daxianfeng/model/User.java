package com.niguang.daxianfeng.model;

import java.util.UUID;

import lombok.Data;

@Data
public class User {
	private String UserId;
	private String wxID;
	private String nickName;
	private int level;
	private int curExp;

	public User(String wxID, String nickName) {
		super();
		this.wxID = wxID;
		this.level = 0;
		this.curExp = 0;
		this.UserId = UUID.randomUUID().toString().replaceAll("-", "");
		this.nickName = nickName;
		System.out.println(UserId);
	}

}
