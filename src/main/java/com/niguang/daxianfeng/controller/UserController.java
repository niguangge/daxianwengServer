package com.niguang.daxianfeng.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.niguang.daxianfeng.model.Room;

@RestController
@RequestMapping("/user/")
@CrossOrigin
public class UserController {
	@Autowired
	private UserService userSerive;

	@RequestMapping(value = "createRoom", method = RequestMethod.POST)
	public @ResponseBody Room createRoom(Model model, HttpServletRequest request, @RequestParam("userId") String userId)
			throws Exception {
		Room room = userSerive.createRoom(userId);
		return room;
	}

	@RequestMapping(value = "getRooms", method = RequestMethod.GET)
	public @ResponseBody Map<Integer, Room> getRooms(Model model, HttpServletRequest request) throws Exception {
		Map<Integer, Room> map = userSerive.getRoomMap();
		return map;
	}

	@RequestMapping(value = "joinRoom", method = RequestMethod.POST)
	public @ResponseBody int joinRoom(Model model, HttpServletRequest request, @RequestParam("userId") String userId,
			@RequestParam("roomId") int roomId) throws Exception {
		return userSerive.joinRoom(roomId, userId);
	}

	@RequestMapping(value = "leaveRoom", method = RequestMethod.POST)
	public @ResponseBody int leaveRoom(Model model, HttpServletRequest request, @RequestParam("userId") String userId,
			@RequestParam("roomId") int roomId) throws Exception {
		return userSerive.leaveRoom(roomId, userId);
	}

	@RequestMapping(value = "getUserBelong", method = RequestMethod.POST)
	public @ResponseBody int getUserBelong(Model model, HttpServletRequest request,
			@RequestParam("userId") String userId) throws Exception {
		return userSerive.getUserBelong(userId);
	}

	@RequestMapping(value = "cleanAllRooms", method = RequestMethod.POST)
	public @ResponseBody void cleanAllRooms(Model model, HttpServletRequest request) throws Exception {
		userSerive.cleanAllRooms();
	}

	@RequestMapping(value = "test", method = RequestMethod.POST)
	public @ResponseBody int test(Model model, HttpServletRequest request) throws Exception {
		return userSerive.test();
	}
}
