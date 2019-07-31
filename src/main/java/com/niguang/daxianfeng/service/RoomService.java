package com.niguang.daxianfeng.service;

import com.niguang.daxianfeng.model.Room;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RoomService {
    private Map<Integer, Room> roomMap;
    private Map<Integer, Room> busyRooms;
    private List<Room> emptyRooms;

    public RoomService() {
        roomMap = new HashMap<>();
        busyRooms = new HashMap<>();
        emptyRooms = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Room room = new Room(i);
            emptyRooms.add(room);
        }
    }

    public Room createRoom(Long userId) {
        Room room = emptyRooms.get(0);
        if (room == null) {

        } else {
            room.addUser(userId);
            emptyRooms.remove(room);
            roomMap.put(room.getRoomId(), room);
        }
        return room;
    }

    public int joinRoom(int roomId, long userId) {
        Room room = roomMap.get(roomId);
        return room.addUser(userId);
    }

    public Map<Integer, Room> getRoomMap() {
        return roomMap;
    }

    public int leaveRoom(int roomId, long userId) {
        Room room = roomMap.get(roomId);
        if (room.getCurUserCount() - 1 == 0) {
            emptyRooms.add(room);
            roomMap.remove(room);
        }
        return room.deleteUser(userId);

    }


}
