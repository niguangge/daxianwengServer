package com.niguang.daxianfeng.controller;


import com.niguang.daxianfeng.service.GameService;
import com.niguang.daxianfeng.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ServerEndpoint("/webSocketServer/{ro_user}")
@Component
public class WebSocketServer {
    @Autowired
    private RoomService roomService;
    @Autowired
    private GameService gameService;
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
    public void onOpen(@PathParam(value = "ro_user") String ro_user, Session session) {
        String[] params = ro_user.split("-");
        roomId = Integer.valueOf(params[0]);
        userId = Integer.valueOf(params[1]);
        roomService.joinRoom(roomId, userId);
        UserSessions userSessions = new UserSessions(userId, session);

        List<UserSessions> userList = rooms.get(roomId);
        if (null == userList || userList.size() == 0) {
            userList = new ArrayList<>();
        }
        userList.add(userSessions);
        rooms.put(roomId, userList);
        System.out.println("新开启了一个webSocket连接" + session.getId());
    }

    @OnMessage
    public String onMessage(String message, Session session) throws IOException {
        System.out.println("收到客户端发送的信息:" + message);
        System.out.println("当前的sessionId:" + session.getId());
        String newMessage = "来自于房间" + roomId + "内用户" + userId + "的消息：" + message;
        //session.getAsyncRemote().sendText(message);
        String type = gameService.messageHandler(roomId, userId, message);
        if ("all".equals(type)) {
            for (UserSessions userSessions : rooms.get(roomId)) {
                if (userSessions.session.isOpen()) {
                    userSessions.session.getBasicRemote().sendText(newMessage);
                } else {
                    return "Failed";
                }
            }
        }
        return "SUCCESS";
    }

    @OnClose
    public void onClose(Session session, CloseReason reason) {
        System.out.println("webSocket连接关闭：sessionId:" + session.getId() + "关闭原因是：" + reason.getReasonPhrase() + "code:" + reason.getCloseCode());
    }


    @OnError
    public void onError(Throwable t) {
        t.printStackTrace();
    }

}