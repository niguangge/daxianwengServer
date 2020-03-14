package com.niguang.daxianfeng.controller;

import java.io.IOException;

import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.springframework.stereotype.Component;

import com.niguang.daxianfeng.ApplicationContextRegister;
import com.niguang.daxianfeng.service.GameService;

@ServerEndpoint("/webSocketServer")
@Component
public class WebSocketServer {
	private String userId;
	private String roomId;
	private GameService gameService;

	@OnOpen
	public void onOpen(Session session) throws IOException {
		roomId = session.getRequestParameterMap().get("roomId").get(0);
		userId = session.getRequestParameterMap().get("userId").get(0);
		gameService = (GameService) ApplicationContextRegister.getBean("gameService");
	}

	@OnMessage
	public void onMessage(String message, Session session)
			throws IOException, NoSuchMethodException, SecurityException {
		System.out.println("客户端信息：" + message);
		gameService.websocketHandler(message, session);

	}

	@OnClose
	public void onClose(Session session, CloseReason reason)
			throws IOException, NoSuchMethodException, SecurityException {
		System.out.println("webSocket连接关闭：sessionId:" + session.getId() + "关闭原因是：" + reason.getReasonPhrase() + "code:"
				+ reason.getCloseCode());
		// 非正常关闭，需要退出房间，并广播给其他用户
		if (reason.getCloseCode() != CloseReason.CloseCodes.getCloseCode(1000)) {
			String message = gameService.getLeaveMsg(userId, roomId);
			gameService.websocketHandler(message, session);
		}
	}

	@OnError
	public void onError(Throwable t) {
		t.printStackTrace();
	}

}