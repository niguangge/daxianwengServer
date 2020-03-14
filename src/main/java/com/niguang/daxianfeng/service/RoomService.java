package com.niguang.daxianfeng.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.websocket.Session;

import org.springframework.stereotype.Service;

import com.niguang.daxianfeng.model.Room;

@Service("roomService")
public class RoomService {
	private Map<Integer, Room> roomMap = new HashMap<>();

	public RoomService() {
		init();
	}

	@PostConstruct
	// 指定初始化方法
	private void init() {
		roomMap = new HashMap<>();
		for (int i = 0; i < 10; i++) {
			Room room = new Room(i);
			roomMap.put(i, room);
		}
	}

	public int joinRoom(int roomId, String userId, Session session) {
		Room room = roomMap.get(roomId);
		if (room == null) {
			return -1;
		}
		return room.addUser(userId, session);
	}

	public Map<Integer, Room> getRoomMap() {
		return roomMap;
	}

	public Room getRoom(int roomId) {
		return roomMap.get(roomId);
	}

	public List<Session> getRoomSessions(int roomId) {
		if (roomMap.get(roomId) == null) {
			return new ArrayList<Session>();
		}
		Collection<Session> sessions = roomMap.get(roomId).getUserSessions().values();
		return new ArrayList<Session>(sessions);
	}

	public int leaveRoom(int roomId, String userId) {
		Room room = roomMap.get(roomId);
		return room.deleteUser(userId);

	}

	public int getUserBelong(String userId) {
		for (Room room : roomMap.values()) {
			if (room.getUsers().contains(userId)) {
				return room.getRoomId();
			}
		}
		return -1;
	}

	public int getRoomUserCount(int roomId) {
		Room room = roomMap.get(roomId);
		return room.getCurUserCount();
	}

	public Map<Integer, Room> getRooms() {
		return roomMap;
	}

	public int cleanRoom(int roomId) {
		Room room = roomMap.get(roomId);
		if (room != null) {
			room.clean();
			return 0;
		}
		return -1;
	}

	public void cleanAllRooms() {
		init();
	}

	public List<String> getExistUsers(int roomId) {
		Room room = roomMap.get(roomId);
		return room.getUsers();
	}

	public void boardToRoomUsers(int roomId, String msg) throws IOException {
		List<Session> SessionsInRoom = getRoomSessions(roomId);
		for (Session userSessions : SessionsInRoom) {
			if (userSessions.isOpen()) {
				userSessions.getBasicRemote().sendText(msg);
			}
		}
	}
}
