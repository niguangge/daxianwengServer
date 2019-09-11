package com.niguang.daxianfeng.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.niguang.daxianfeng.ApplicationContextRegister;
import com.niguang.daxianfeng.model.User;
import com.niguang.daxianfeng.service.GameService;
import com.niguang.daxianfeng.service.MessageService;
import com.niguang.daxianfeng.service.RoomService;
import com.niguang.daxianfeng.service.UserService;

@ServerEndpoint("/webSocketServer")
@Component
public class WebSocketServer {

	private static final Map<String, List<UserSession>> allSessions = new HashMap<>();
	private String userId;
	private String roomId;
	private RoomService roomService;
	private GameService gameService;
	private MessageService messageService;
	private UserService userService;

	private class UserSession {
		public String userId;
		public Session session;

		public UserSession(String userId, Session session) {
			this.userId = userId;
			this.session = session;
		}
	}

	@OnOpen
	public void onOpen(Session session) throws IOException {
		initSessionList(session);
		initServices();
		roomService.joinRoom(Integer.parseInt(roomId), userId);
		List<User> userInfos = getExistUsers();
		session.getBasicRemote().sendText(messageService.getJoinMsg(userId, roomId, userInfos));
		if (roomService.getRoomUserCount(Integer.parseInt(roomId)) == 2) {
			boardToRoomUsers(messageService.getMessage("start"));

		}
	}

	@OnMessage
	public void onMessage(String message, Session session)
			throws IOException, NoSuchMethodException, SecurityException {
		String result = getResult(message);
		for (UserSession userSessions : allSessions.get(roomId)) {
			if (userSessions.session.isOpen()) {
				userSessions.session.getBasicRemote().sendText(result);
			}
		}
	}

	@OnClose
	public void onClose(Session session, CloseReason reason) throws IOException {
		System.out.println("webSocket连接关闭：sessionId:" + session.getId() + "关闭原因是：" + reason.getReasonPhrase() + "code:"
				+ reason.getCloseCode());
		session.getBasicRemote().sendText(messageService.getLeaveMsg(userId, roomId));
		roomService.leaveRoom(Integer.parseInt(roomId), userId);
	}

	@OnError
	public void onError(Throwable t) {
		t.printStackTrace();
	}

	private void initSessionList(Session session) {
		roomId = session.getRequestParameterMap().get("roomId").get(0);
		// 坑爹的微信居然openId里还有-中划线
		userId = session.getRequestParameterMap().get("userId").get(0);
		UserSession userSession = new UserSession(userId, session);
		List<UserSession> userSessionsInRoom = allSessions.get(roomId);
		if (null == userSessionsInRoom || userSessionsInRoom.size() == 0) {
			userSessionsInRoom = new ArrayList<>();
		}
		userSessionsInRoom.add(userSession);
		allSessions.put(roomId, userSessionsInRoom);
	}

	public void initServices() {
		roomService = (RoomService) ApplicationContextRegister.getBean("roomService");
		messageService = (MessageService) ApplicationContextRegister.getBean("messageService");
		gameService = (GameService) ApplicationContextRegister.getBean("gameService");
		userService = (UserService) ApplicationContextRegister.getBean("userService");
	}

	public List<User> getExistUsers() {
		List<String> existUsers = roomService.getOtherUsers(Integer.parseInt(roomId), userId);
		List<User> userInfos = new ArrayList<>();
		for (int i = 0; i < existUsers.size(); i++) {
			String existUserId = existUsers.get(i);
			userInfos.add(userService.getUserByWxId(existUserId));
		}
		return userInfos;

	}

	private String getResult(String message) throws NoSuchMethodException, SecurityException {
		JSONObject msg = JSON.parseObject(message);
		JSONObject params = (JSONObject) msg.get("params");
		params = gameService.websocketHandler(roomId, userId, message);
		String stage = msg.get("stage").toString();
		return messageService.getMessage(userId, roomId, stage, params);
	}

	private void boardToRoomUsers(String msg) throws IOException {
		for (UserSession userSessions : allSessions.get(roomId)) {
			if (userSessions.session.isOpen()) {
				userSessions.session.getBasicRemote().sendText(msg);
			}
		}
	}
}