package com.niguang.daxianfeng.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import lombok.Data;

@Service("messageService")
public class MessageService {
	@Data
	class JsonRootBean {
		private String stage; // 游戏阶段，如开始阶段，掷骰子阶段，时间阶段
		private String fromUser; // 信息来源用户
		private String fromRoom; // 信息来源房间
		private String scope; // 信息通知范围，单人/全体
		private String channel; // 频道，系统/聊天/游戏
		private JSONObject params; // 不同阶段所需参数
		private String message; // 需要发送的信息

		public JsonRootBean(String stage, String fromUser, String fromRoom, String scope, String channel,
				JSONObject params, String message) {
			super();
			this.stage = stage;
			this.fromUser = fromUser;
			this.fromRoom = fromRoom;
			this.scope = scope;
			this.channel = channel;
			this.params = params;
			this.message = message;
		}

	}

	public String getJoinMsg(String userId, String roomId, int userOrder) {
		JSONObject params = new JSONObject();
		params.put("userOrder", userOrder);
		JsonRootBean jsonRootBean = new JsonRootBean("join", userId, roomId, "all", "system", params, null);
		return JSON.toJSONString(jsonRootBean);
	}

	public String getLeaveMsg(String userId, String roomId) {
		JSONObject params = new JSONObject();
		JsonRootBean jsonRootBean = new JsonRootBean("leave", userId, roomId, "all", "system", params, null);
		return JSON.toJSONString(jsonRootBean);
	}

	public String getDiceMsg(String userId, String roomId, String diceNum, List<String> dicetypes,
			List<Integer> diceResult) {
		JSONObject params = new JSONObject();
		params.put("diceNum", diceNum);
		params.put("diceTypes", dicetypes);
		params.put("diceResult", diceResult);
		JsonRootBean jsonRootBean = new JsonRootBean("dice", userId, roomId, "all", "system", params, null);
		return JSON.toJSONString(jsonRootBean);
	}

	public String getOperationMsg(String userId, String roomId, String operation) {
		JSONObject params = new JSONObject();
		params.put("operation", operation);
		JsonRootBean jsonRootBean = new JsonRootBean("operation", userId, roomId, "all", "system", params, null);
		return JSON.toJSONString(jsonRootBean);
	}

	public String getEventMsg(String userId, String roomId, String eventName, String eventValue) {
		JSONObject params = new JSONObject();
		params.put("eventName", eventName);
		params.put("eventValue", eventValue);
		JsonRootBean jsonRootBean = new JsonRootBean("event", userId, roomId, "all", "system", params, null);
		return JSON.toJSONString(jsonRootBean);
	}

	public String getReadyMsg(String userId, String roomId, String chooseHero, String isReady) {
		JSONObject params = new JSONObject();
		params.put("chooseHero", chooseHero);
		params.put("isReady", isReady);
		JsonRootBean jsonRootBean = new JsonRootBean("ready", userId, roomId, "all", "system", params, null);
		return JSON.toJSONString(jsonRootBean);
	}

	public String getMessage(String userId, String roomId, String stage, JSONObject params) {
		JsonRootBean jsonRootBean = new JsonRootBean(stage, userId, roomId, "all", "system", params, null);
		return JSON.toJSONString(jsonRootBean);

	}
}
