package com.niguang.daxianfeng.model;

import com.alibaba.fastjson.JSONObject;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class OperationParams extends Params {
	private String eventName;
	private int eventValue;
	private int operation;

	public OperationParams(JSONObject params) {
		super(params);
		eventName = params.getString("eventName");
		eventValue = params.getIntValue("eventValue");
		eventValue = params.getIntValue("operation");
	}

}
