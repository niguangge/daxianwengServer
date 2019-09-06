package com.niguang.daxianfeng.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Room {
	private int roomId;
	private int maxUserCount;
	private int curUserCount;
	private String ownerId;
	private List<String> users;

	public Room(int roomId) {
		this.roomId = roomId;
		init();
	}

	private void init() {
		this.maxUserCount = 4;
		this.curUserCount = 0;
		this.ownerId = "0";
		this.users = new ArrayList<>();
	}

	public int addUser(String userId) {
		if (this.curUserCount < this.maxUserCount) {
			if (this.curUserCount == 0) {
				this.ownerId = userId;
			}
			this.curUserCount++;
			this.users.add(userId);
			return 1;
		}
		return -1;
	}

	public int deleteUser(String userId) {
		if (users.contains(userId)) {
			users.remove(userId);
			curUserCount--;
			if (curUserCount != 0) {
				if (ownerId.equals(userId)) {
					ownerId = users.get(0);
				}
			} else {
				ownerId = "0";
			}
			return 1;
		}
		return 0;
	}

	public void clean() {
		init();
	}

}
