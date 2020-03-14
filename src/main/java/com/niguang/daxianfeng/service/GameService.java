package com.niguang.daxianfeng.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.annotation.PostConstruct;
import javax.websocket.CloseReason;
import javax.websocket.Session;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.niguang.daxianfeng.ApplicationContextRegister;
import com.niguang.daxianfeng.model.JsonMessage;
import com.niguang.daxianfeng.model.User;

@Service("gameService")
public class GameService {
	public static final String WS_SCOPE = "scope";
	public static final String WS_SCOPE_PERSONAL = "personal";
	public static final String WS_SCOPE_ALL = "all";
	public static final String WS_STAGE = "stage";
	public static final String WS_STAGE_JOIN = "join";
	public static final String WS_STAGE_READY = "ready";
	public static final String WS_STAGE_START = "start";
	public static final String WS_STAGE_DICE = "dice";
	public static final String WS_STAGE_EVENT = "event";
	public static final String WS_STAGE_OPERATION = "operation";
	public static final String WS_STAGE_LEAVE = "leave";
	public static final String WS_STAGE_CLOSE = "close";
	public static final String WS_CHANNEL = "channel";
	public static final String WS_CHANNEL_SYSTEM = "system";
	public static final String WS_CHANNEL_CHAT = "chat";
	public static final String WS_PARAMS = "params";

	private RoomService roomService;
	private UserService userService;

	@PostConstruct
	// 指定初始化方法
	private void init() {
		roomService = (RoomService) ApplicationContextRegister.getBean("roomService");
		userService = (UserService) ApplicationContextRegister.getBean("userService");
	}

	public void websocketHandler(String message, Session session)
			throws NoSuchMethodException, SecurityException, IOException {
		JsonMessage jsonMsg = new JsonMessage(message);
		String roomId = jsonMsg.getRoomId();
		switch (jsonMsg.getStage()) {
		case WS_STAGE_JOIN:
			onJoin(jsonMsg, session);
			break;
		case WS_STAGE_READY:
			onReady(jsonMsg);
			break;
		case WS_STAGE_START:
			onStart(jsonMsg);
			break;
		case WS_STAGE_DICE:
			onDice(jsonMsg);
			break;
		case WS_STAGE_OPERATION:
			onOperation(jsonMsg);
			break;
		case WS_STAGE_CLOSE:
			onLeave(jsonMsg, session);// 关闭之前先退出房间
			onClose(jsonMsg, session);
			break;
		case WS_STAGE_LEAVE:
			onLeave(jsonMsg, session);
			break;
		default:
		}
		roomService.boardToRoomUsers(Integer.parseInt(roomId), jsonMsg.toString());
	}

	private void onClose(JsonMessage jsonMsg, Session session) throws IOException {
		// 设定CloseReason为正常关闭，防止websocket.onClose()再次执行本函数
		CloseReason closeReason = new CloseReason(CloseReason.CloseCodes.getCloseCode(1000), "用户手动退出房间");
		session.close(closeReason);
	}

	private void onLeave(JsonMessage jsonMsg, Session session) throws IOException {
		String roomId = jsonMsg.getRoomId();
		String userId = jsonMsg.getUserId();
		roomService.leaveRoom(Integer.parseInt(roomId), userId);
	}

	private void onJoin(JsonMessage jsonMsg, Session session) throws IOException {
		String roomId = jsonMsg.getRoomId();
		String userId = jsonMsg.getUserId();
		roomService.joinRoom(Integer.parseInt(roomId), userId, session);
		List<User> userDetails = getExistUserDedails(roomId, session);
		jsonMsg.setParam("userDetails", JSON.toJSONString(userDetails));
	}

	private void onReady(JsonMessage jsonMsg) {

	}

	private void onStart(JsonMessage jsonMsg) {

	}

	private void onDice(JsonMessage jsonMsg) {
		List<Integer> diceResult = new ArrayList<>();
		int diceNum = Integer.parseInt(jsonMsg.getParam("diceNums"));
		List<String> diceTypes = JSON.parseArray(jsonMsg.getParam("diceTypes"), String.class);
		int diceSum = 0;
		for (int i = 0; i < diceNum; i++) {
			Integer[] dice = DiceService.get(diceTypes.get(i));
			int diceCur = dice[new Random().nextInt(6)];
			diceResult.add(diceCur);
			diceSum += diceCur;
		}
		jsonMsg.setParam("diceResult", JSON.toJSONString(diceResult));
		jsonMsg.setParam("diceSum", String.valueOf(diceSum));
	}

	private void onOperation(JsonMessage jsonMsg) {
		int eventName = Integer.parseInt(jsonMsg.getParam("eventName"));
		int eventValue = Integer.parseInt(jsonMsg.getParam("eventValue"));
		int operation = Integer.parseInt(jsonMsg.getParam("operation"));
		int lingChange = 0;
		if (operation == 0) {
			lingChange = getLingChange(eventName, eventValue);
		}
		jsonMsg.setParam("lingChange", String.valueOf(lingChange));
		jsonMsg.setParam("description", getDescription(eventName));
	}

	public List<User> getExistUserDedails(String roomId, Session session) throws IOException {
		List<String> existUsers = roomService.getExistUsers(Integer.parseInt(roomId));
		List<User> userInfos = new ArrayList<>();
		for (int i = 0; i < existUsers.size(); i++) {
			String existUserId = existUsers.get(i);
			userInfos.add(userService.getUserByWxId(existUserId));
		}
		return userInfos;
	}

	public String getLeaveMsg(String userId, String roomId) {
		return new JsonMessage("leave", userId, roomId, "all", "system", null).toString();
	}

	private String getDescription(int eventName) {
		String[] eventescription = { "误服毒果，减少灵气", "服用灵果，增加灵气" };
		return eventescription[eventName];
	}

	private int getLingChange(int eventName, int eventValue) {
		int[] events = { -1, 1, 2, 3, 5 };
		return eventValue * events[eventName];
	}

}
