package com.niguang.daxianfeng.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

@Service("gameService")
public class GameService {
	public String messageHandler(int roomId, long userId, String message, JSONObject result)
			throws NoSuchMethodException, SecurityException {
//    	Method method = this.getClass().getMethod("getName");
		String type = "personal"; // 默认为仅发送给自己
		JSONObject obj = JSON.parseObject(message);
		if ("all".equals(obj.get("scope"))) {
			type = "all";
		}
		JSONObject params = JSON.parseObject((String) obj.get("params"));
		if ("dice".equals(obj.get("stage"))) {
			diceHandler(params, result);
		} else if ("event".equals(obj.get("stage"))) {
			eventHandler(params, result);
		} else if ("operation".equals(obj.get("stage"))) {
			operationHandler(params, result);
		} else if ("chat".equals(obj.get("stage"))) {
			chatHandler(params, result);
		} else if ("ready".equals(obj.get("stage"))) {
			readyHandler(userId, params, result);
		}
		return type;
	}

	private JSONObject diceHandler(JSONObject params, JSONObject result) {
		List<Integer> list = new ArrayList<>();
		int diceNum = Integer.parseInt((String) params.get("diceNum"));
		for (int i = 0; i < diceNum; i++) {
			int dice = new Random().nextInt(6);
			list.add(dice);
		}
		result.put("diceNums", list);
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

	private JSONObject readyHandler(long userId,JSONObject params, JSONObject result) {
		result.put("userId", userId);
		result.put("hero", params.get("hero"));
		return result;
	}
}
