package com.niguang.daxianfeng.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.niguang.daxianfeng.common.Constant;

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

	public String websocketHandler(int roomId, String userId, String message, JSONObject result)
			throws NoSuchMethodException, SecurityException {
		String type = WS_SCOPE_ALL; // 默认为发送给全部
		JSONObject obj;
		if ("test".equals(message)) {
			obj = getTestJson();
		} else {
			System.out.println("from vue:"+message);
			System.out.println("from server:"+getTestJson().toString());
			obj = JSON.parseObject(message);
		}
		JSONObject params = JSON.parseObject((String) obj.get(WS_PARAMS));
		result.put(WS_CHANNEL, obj.get(WS_CHANNEL));
		result.put(WS_STAGE, obj.get(WS_STAGE));
		switch (obj.get(WS_STAGE).toString()) {
		case WS_STAGE_DICE:
			diceHandler(params, result);
			break;
		case WS_STAGE_EVENT:
			eventHandler(params, result);
			break;
		case WS_STAGE_OPERATION:
			operationHandler(params, result);
			break;
		case WS_STAGE_READY:
			readyHandler(userId, params, result);
			break;
		default:

		}
		return type;
	}

	private JSONObject getTestJson() {
		JSONObject obj = new JSONObject();
		obj.put(WS_CHANNEL, WS_CHANNEL_SYSTEM);
		obj.put(WS_STAGE, WS_STAGE_DICE);
		JSONObject params = new JSONObject();
		params.put("diceNum","3");
		List<String> list = new ArrayList<>();
		list.add(Constant.DICE_NORMAL);
		list.add(Constant.DICE_MINUS_ONE);
		list.add(Constant.DICE_PLUS_ONE);
		params.put("diceType", list);
		obj.put(WS_PARAMS, params.toString());
		return obj;
	}

	private JSONObject diceHandler(JSONObject params, JSONObject result) {
		List<Integer> list = new ArrayList<>();
		int diceNum = Integer.parseInt((String) params.get("diceNum"));
		System.out.println(params.get("diceType"));
		List<String> diceTypes =  JSON.parseArray(params.get("diceType").toString(), String.class);
		for (int i = 0; i < diceNum; i++) {
			System.out.println(diceTypes.get(i));
			Integer[] dice = DiceService.get(diceTypes.get(i));
			System.out.println(dice);
			int diceCur = dice[new Random().nextInt(6)];
			list.add(diceCur);
		}
		result.put("diceNums", JSON.toJSONString(list));
		return result;
	}

	private JSONObject eventHandler(JSONObject params, JSONObject result) {
		return result;
	}

	private JSONObject operationHandler(JSONObject params, JSONObject result) {
		return result;
	}

	private JSONObject chatHandler(JSONObject object, JSONObject result) {
		// TODO Auto-generated method stub
		return result;
	}

	private JSONObject readyHandler(String userId, JSONObject params, JSONObject result) {
		result.put("userId", userId);
		result.put("hero", params.get("hero"));
		return result;
	}
}
