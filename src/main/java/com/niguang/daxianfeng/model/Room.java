package com.niguang.daxianfeng.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.websocket.Session;

import lombok.Data;

@Data
public class Room {
	private int roomId;
	private int maxUserCount;
	private int curUserCount;
	private String ownerId;
	private List<String> users;
	private Map<String, Session> userSessions;

	public Room(int roomId) {
		this.roomId = roomId;
		init();
	}

	private void init() {
		this.maxUserCount = 4;
		this.curUserCount = 0;
		this.ownerId = "0";
		this.users = new ArrayList<>();
		this.userSessions = new LinkedHashMap<>();
	}

	public int addUser(String userId, Session session) {
		if (curUserCount < this.maxUserCount) {
			if (curUserCount == 0) {
				ownerId = userId;
			}
			users.add(userId);
			userSessions.put(userId, session);
			System.out.println(curUserCount);
			return ++curUserCount;
		} else {
			return -1;
		}
	}

	public int deleteUser(String userId) {
		if (users.contains(userId)) {
			users.remove(userId);
			userSessions.remove(userId);
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
