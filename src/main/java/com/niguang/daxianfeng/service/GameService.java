package com.niguang.daxianfeng.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

@Service("gameService")
public class GameService {
	public static final String WS_SCOPE = "scope";
	public static final String WS_SCOPE_PERSONAL = "personal";
	public static final String WS_SCOPE_ALL = "all";
	public static final String WS_STAGE = "stage";
	public static final String WS_STAGE_READY = "ready";
	public static final String WS_STAGE_DICE = "dice";
	public static final String WS_STAGE_EVENT = "event";
	public static final String WS_STAGE_OPERATION = "operation";
	public static final String WS_CHANNEL = "channel";
	public static final String WS_CHANNEL_SYSTEM = "system";
	public static final String WS_CHANNEL_CHAT = "chat";
	public static final String WS_PARAMS = "params";

	public JSONObject websocketHandler(String roomId, String userId, String message)
			throws NoSuchMethodException, SecurityException {
		JSONObject obj = JSON.parseObject(message);
		JSONObject params = (JSONObject) obj.get(WS_PARAMS);
		switch (obj.get(WS_STAGE).toString()) {
		case WS_STAGE_DICE:
			diceHandler(params);
//			eventHandler(params);
			break;
		case WS_STAGE_OPERATION:
			operationHandler(params);
			break;
		case WS_STAGE_READY:
			readyHandler(userId, params);
			break;
		default:
		}
		return params;
	}

	private void diceHandler(JSONObject params) {
		List<Integer> diceResult = new ArrayList<>();
		int diceNum = Integer.parseInt((String) params.get("diceNums"));
		List<String> diceTypes = JSON.parseArray(params.get("diceTypes").toString(), String.class);
		int diceSum = 0;
		for (int i = 0; i < diceNum; i++) {
			Integer[] dice = DiceService.get(diceTypes.get(i));
			int diceCur = dice[new Random().nextInt(6)];
			diceResult.add(diceCur);
			diceSum += diceCur;
		}
		params.put("diceResult", JSON.toJSONString(diceResult));
		params.put("diceSum", diceSum);
	}

	private void eventHandler(JSONObject params) {
//		int heroPos = Integer.parseInt((String) params.get("heroPos"));
//		int diceSum = Integer.parseInt((String) params.get("diceSum"));
//		String[] events = { "金", "木", "水", "火", "土", "时" };
//		String eventType = events[(heroPos + diceSum) % 6];
//		Map<String, Integer> event = getEvent(eventType);
//		params.put("eventName", JSON.toJSONString(diceResult));
//		params.put("diceSum", diceSum);
	}

//	private Map<String, Integer> getEvent(String eventType) {
//		Map<String, Integer> events = new HashMap<String, Integer>();
//		events.put("误服毒果，减少灵气", -1);
//		events.put("服用灵果，增加灵气", 1);
//		return null;
//	}

	private void operationHandler(JSONObject params) {
		int eventName = Integer.parseInt((String) params.get("eventName"));
		int eventValue = Integer.parseInt((String) params.get("eventValue"));
		int operation = Integer.parseInt((String) params.get("operation"));
		int lingChange = 0;
		if (operation == 0) {
			lingChange = getLingChange(eventName, eventValue);
		}
		params.put("lingChange", lingChange);
		params.put("description", getDescription(eventName));
	}

	private Object getDescription(int eventName) {
		String[] eventescription = { "误服毒果，减少灵气", "服用灵果，增加灵气" };
		return eventescription[eventName];
	}

	private int getLingChange(int eventName, int eventValue) {
		int[] events = { -1, 1, 2, 3, 5 };

		return eventValue * events[eventName];
	}

//	private void chatHandler(JSONObject object) {
//	}

	private void readyHandler(String userId, JSONObject params) {
		params.put("userId", userId);
		params.put("hero", params.get("hero"));
	}
}
