package com.niguang.daxianfeng.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import com.niguang.daxianfeng.model.Room;

@Service("roomService")
public class RoomService {
	private Map<Integer, Room> roomMap = new HashMap<>();
	private List<Room> emptyRooms = new ArrayList<>();

	public RoomService() {
		init();
	}

	@PostConstruct
	// 指定初始化方法
	private void init() {
		roomMap = new HashMap<>();
		emptyRooms = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			Room room = new Room(i);
			//TODO 临时测试
			if (i == 1) {
				roomMap.put(1, room);
			} else {
				emptyRooms.add(room);

			}
		}
	}

	public Room createRoom(String userId) {
		Room room = emptyRooms.get(0);
		if (room != null) {
			room.addUser(userId);
			emptyRooms.remove(room);
			roomMap.put(room.getRoomId(), room);
			System.out.println(room.getRoomId());
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
