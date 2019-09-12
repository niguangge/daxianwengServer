package com.niguang.daxianfeng.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
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
	private String userId;
	private String roomId;
	private RoomService roomService;
	private GameService gameService;
	private MessageService messageService;
	private UserService userService;

	@OnOpen
	public void onOpen(Session session) throws IOException {
		initParams(session);
		initServices();
		int roomUserCount = roomService.joinRoom(Integer.parseInt(roomId), userId, session);
		if (roomUserCount == 2) {
			List<User> userDetails = getExistUserDedails(session);
			String msg = messageService.getMessage("start", userDetails);
			boardToRoomUsers(msg);
		}
	}

	@OnMessage
	public void onMessage(String message, Session session)
			throws IOException, NoSuchMethodException, SecurityException {
		String result = getHandleResult(message);
		boardToRoomUsers(result);
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

	private void initParams(Session session) {
		roomId = session.getRequestParameterMap().get("roomId").get(0);
		userId = session.getRequestParameterMap().get("userId").get(0);
	}

	public void initServices() {
		roomService = (RoomService) ApplicationContextRegister.getBean("roomService");
		messageService = (MessageService) ApplicationContextRegister.getBean("messageService");
		gameService = (GameService) ApplicationContextRegister.getBean("gameService");
		userService = (UserService) ApplicationContextRegister.getBean("userService");
	}

	public List<User> getExistUserDedails(Session session) throws IOException {
		List<String> existUsers = roomService.getExistUsers(Integer.parseInt(roomId));
		List<User> userInfos = new ArrayList<>();
		for (int i = 0; i < existUsers.size(); i++) {
			String existUserId = existUsers.get(i);
			userInfos.add(userService.getUserByWxId(existUserId));
		}
		return userInfos;
	}

	private String getHandleResult(String message) throws NoSuchMethodException, SecurityException {
		JSONObject msg = JSON.parseObject(message);
		JSONObject params = (JSONObject) msg.get("params");
		params = gameService.websocketHandler(roomId, userId, message);
		String stage = msg.get("stage").toString();
		return messageService.getMessage(userId, roomId, stage, params);
	}

	private void boardToRoomUsers(String msg) throws IOException {
		roomService.boardToRoomUsers(Integer.parseInt(roomId), msg);
	}
}