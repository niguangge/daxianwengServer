package com.niguang.daxianfeng.controller;

import com.niguang.daxianfeng.model.Room;
import com.niguang.daxianfeng.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/room/")
@CrossOrigin
public class RoomController {
	@Autowired
	private RoomService roomService;

	@RequestMapping(value = "getRooms", method = RequestMethod.GET)
	public @ResponseBody Map<Integer, Room> getRooms(Model model, HttpServletRequest request) throws Exception {
		Map<Integer, Room> map = roomService.getRoomMap();
		return map;
	}

	@RequestMapping(value = "leaveRoom", method = RequestMethod.POST)
	public @ResponseBody int leaveRoom(Model model, HttpServletRequest request, @RequestParam("userId") String userId,
			@RequestParam("roomId") int roomId) throws Exception {
		return roomService.leaveRoom(roomId, userId);
	}

	@RequestMapping(value = "getUserBelong", method = RequestMethod.POST)
	public @ResponseBody int getUserBelong(Model model, HttpServletRequest request,
			@RequestParam("userId") String userId) throws Exception {
		return roomService.getUserBelong(userId);
	}

	@RequestMapping(value = "cleanAllRooms", method = RequestMethod.POST)
	public @ResponseBody void cleanAllRooms(Model model, HttpServletRequest request) throws Exception {
		roomService.cleanAllRooms();
	}
}
