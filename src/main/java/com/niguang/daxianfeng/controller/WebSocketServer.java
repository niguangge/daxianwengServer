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

import com.alibaba.fastjson.JSONObject;
import com.niguang.daxianfeng.ApplicationContextRegister;
import com.niguang.daxianfeng.service.GameService;
import com.niguang.daxianfeng.service.RoomService;

@ServerEndpoint("/webSocketServer/{ro_user}")
@Component
public class WebSocketServer {

	private static final Map<Integer, List<UserSessions>> rooms = new HashMap<>();
	private Integer userId;

	private Integer roomId;

	private class UserSessions {
		public int userId;
		public Session session;

		public UserSessions(int userId, Session session) {
			this.userId = userId;
			this.session = session;
		}
	}

	@OnOpen
	public void onOpen(@PathParam(value = "ro_user") String ro_user, Session session) throws IOException {
		String[] params = ro_user.split("-");
		roomId = Integer.valueOf(params[0]);
		userId = Integer.valueOf(params[1]);
//		RoomService roomService = applicationContext.getBean(RoomService.class);
		RoomService roomService = (RoomService) ApplicationContextRegister.getBean("roomService");
		roomService.joinRoom(roomId, userId);
		UserSessions userSessions = new UserSessions(userId, session);

		List<UserSessions> userList = rooms.get(roomId);
		if (null == userList || userList.size() == 0) {
			userList = new ArrayList<>();
		}
		userList.add(userSessions);
		rooms.put(roomId, userList);
		session.getBasicRemote().sendText("玩家" + userId + "已进入房间" + roomId + ",sessionId=" + session.getId());
	}

	@OnMessage
	public String onMessage(String message, Session session)
			throws IOException, NoSuchMethodException, SecurityException {
		System.out.println("当前的sessionId:" + session.getId());
		String newMessage = "来自于房间" + roomId + "内用户" + userId + "的消息：" + message;
		JSONObject result = new JSONObject();
		GameService gameService = (GameService) ApplicationContextRegister.getBean("gameService");
		String type = gameService.messageHandler(roomId, userId, message, result);
		for (UserSessions userSessions : rooms.get(roomId)) {
			if (userSessions.session.isOpen()) {
				if ("all".equals(type)) {
					userSessions.session.getBasicRemote().sendText(newMessage);
				} else if ("personal".equals(type)) {
						session.getBasicRemote().sendText(newMessage);
				}
			}
		}
		return "SUCCESS";
	}

	@OnClose
	public void onClose(Session session, CloseReason reason) throws IOException {
		System.out.println("玩家" + userId + "已离开房间" + roomId + ",sessionId=" + session.getId());
		System.out.println("webSocket连接关闭：sessionId:" + session.getId() + "关闭原因是：" + reason.getReasonPhrase() + "code:"
				+ reason.getCloseCode());
		session.getBasicRemote().sendText("玩家" + userId + "已离开房间" + roomId + ",sessionId=" + session.getId());
	}

	@OnError
	public void onError(Throwable t) {
		t.printStackTrace();
	}

}