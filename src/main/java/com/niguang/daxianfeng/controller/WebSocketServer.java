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
import com.niguang.daxianfeng.service.GameService;
import com.niguang.daxianfeng.service.MessageService;
import com.niguang.daxianfeng.service.RoomService;

@ServerEndpoint("/webSocketServer/{ro_user}")
@Component
public class WebSocketServer {

	private static final Map<String, List<UserSession>> rooms = new HashMap<>();
	private String userId;
	private String roomId;
	private RoomService roomService;
	private GameService gameService;
	private MessageService messageService;

	private class UserSession {
		public String userId;
		public Session session;

		public UserSession(String userId, Session session) {
			this.userId = userId;
			this.session = session;
		}
	}

	@OnOpen
	public void onOpen(@PathParam(value = "ro_user") String ro_user, Session session) throws IOException {
		String[] params = ro_user.split("-");
		roomId = params[0];
		userId = params[1];
		roomService = (RoomService) ApplicationContextRegister.getBean("roomService");
		gameService = (GameService) ApplicationContextRegister.getBean("gameService");
		messageService = (MessageService) ApplicationContextRegister.getBean("messageService");
		roomService.joinRoom(Integer.parseInt(roomId), userId);
		UserSession userSessions = new UserSession(userId, session);

		List<UserSession> userList = rooms.get(roomId);
		if (null == userList || userList.size() == 0) {
			userList = new ArrayList<>();
		}
		userList.add(userSessions);
		rooms.put(roomId, userList);
		session.getBasicRemote().sendText(messageService.getJoinMsg(userId, roomId));
	}

	@OnMessage
	public void onMessage(String message, Session session)
			throws IOException, NoSuchMethodException, SecurityException {
		JSONObject msg = JSON.parseObject(message);
		System.out.println(message);
		JSONObject params = JSON.parseObject((String) msg.get("params"));
		params = gameService.websocketHandler(roomId, userId, message);
		String stage = (String) msg.get("stage");
		String result = messageService.getMessage(userId, roomId, stage, params);
		for (UserSession userSessions : rooms.get(roomId)) {
			if (userSessions.session.isOpen()) {
				userSessions.session.getBasicRemote().sendText(result);
			}
		}
	}

	@OnClose
	public void onClose(Session session, CloseReason reason) throws IOException {
		System.out.println("玩家" + userId + "已离开房间" + roomId + ",sessionId=" + session.getId());
		System.out.println("webSocket连接关闭：sessionId:" + session.getId() + "关闭原因是：" + reason.getReasonPhrase() + "code:"
				+ reason.getCloseCode());
		session.getBasicRemote().sendText(messageService.getLeaveMsg(userId, roomId));
	}

	@OnError
	public void onError(Throwable t) {
		t.printStackTrace();
	}

}