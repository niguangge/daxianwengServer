package com.niguang.daxianfeng.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.niguang.daxianfeng.model.Room;

@Service("roomService")
public class RoomService {
	private Map<Integer, Room> roomMap;
	private List<Room> emptyRooms;

	public RoomService() {
		init();
	}

	private void init() {
		roomMap = new HashMap<>();
		emptyRooms = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			Room room = new Room(i);
			emptyRooms.add(room);
		}
	}

	public Room createRoom(String userId) {
		Room room = emptyRooms.get(0);
		if (room != null) {
			room.addUser(userId);
			emptyRooms.remove(room);
			roomMap.put(room.getRoomId(), room);
		}
		return room;
	}

	public int joinRoom(int roomId, String userId) {
		Room room = roomMap.get(roomId);
		if (room == null) {
			return -1;
		}
		return room.addUser(userId);
	}

	public Map<Integer, Room> getRoomMap() {
		return roomMap;
	}

	public int leaveRoom(int roomId, String userId) {
		Room room = roomMap.get(roomId);
		if (room.getCurUserCount() == 1) {
			room.clean();
			emptyRooms.add(room);
			roomMap.remove(roomId);
		}
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

	public int test() {
		Integer[] dice = DiceService.get("normal");
		return dice[0];
	}
}
