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

	public JSONObject websocketHandler(String roomId, String userId, String message)
			throws NoSuchMethodException, SecurityException {
		String type = WS_SCOPE_ALL; // 默认为发送给全部
		JSONObject obj = JSON.parseObject(message);
		JSONObject params = JSON.parseObject((String) obj.get(WS_PARAMS));
		switch (obj.get(WS_STAGE).toString()) {
		case WS_STAGE_DICE:
			diceHandler(params);
			break;
		case WS_STAGE_EVENT:
			eventHandler(params);
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

	private JSONObject getTestJson() {
		JSONObject obj = new JSONObject();
		obj.put(WS_CHANNEL, WS_CHANNEL_SYSTEM);
		obj.put(WS_STAGE, WS_STAGE_DICE);
		JSONObject params = new JSONObject();
		params.put("diceNum", "3");
		List<String> list = new ArrayList<>();
		list.add(Constant.DICE_NORMAL);
		list.add(Constant.DICE_MINUS_ONE);
		list.add(Constant.DICE_PLUS_ONE);
		params.put("diceType", list);
		obj.put(WS_PARAMS, params.toString());
		return obj;
	}

	private void diceHandler(JSONObject params) {
		List<Integer> diceResult = new ArrayList<>();
		int diceNum = Integer.parseInt((String) params.get("diceNum"));
		System.out.println(params.get("diceType"));
		List<String> diceTypes = JSON.parseArray(params.get("diceType").toString(), String.class);
		for (int i = 0; i < diceNum; i++) {
			System.out.println(diceTypes.get(i));
			Integer[] dice = DiceService.get(diceTypes.get(i));
			System.out.println(dice);
			int diceCur = dice[new Random().nextInt(6)];
			diceResult.add(diceCur);
		}
		params.put("diceResult", JSON.toJSONString(diceResult));
	}

	private void eventHandler(JSONObject params) {
	}

	private void operationHandler(JSONObject params) {
	}

	private void chatHandler(JSONObject object) {
	}

	private void readyHandler(String userId, JSONObject params) {
		params.put("userId", userId);
		params.put("hero", params.get("hero"));
	}
}
