package com.niguang.daxianfeng.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import lombok.Getter;

public class JsonMessage {
	private @Getter String stage;
	private @Getter String userId;
	private @Getter String roomId;
	private @Getter String scope;
	private @Getter String channel;
	private JSONObject params;

	public JsonMessage(String message) {
		this(JSON.parseObject(message));
	}

	public JsonMessage(JSONObject obj) {
		super();
		stage = (String) obj.get("stage");
		userId = (String) obj.get("userId");
		roomId = (String) obj.get("roomId");
		scope = (String) obj.get("scope");
		channel = (String) obj.get("channel");
		params = (JSONObject) obj.get("params");
	}

	public JsonMessage(String stage, String userId, String roomId, String scope, String channel, JSONObject params) {
		this.stage = stage;
		this.userId = userId;
		this.roomId = roomId;
		this.scope = scope;
		this.channel = channel;
		this.params = params;
	}

	public void setStage(String stage) {
		this.stage = stage;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public void setParam(String paramName, String paramValue) {
		this.params.put(paramName, paramValue);
	}

	public String toString() {
		JSONObject obj = new JSONObject();
		obj.put("stage", stage);
		obj.put("userId", userId);
		obj.put("roomId", roomId);
		obj.put("scope", scope);
		obj.put("channel", channel);
		obj.put("params", params);
		return obj.toString();
	}

	public String getParam(String paramName) {
		return params.getString(paramName);
	}
}
