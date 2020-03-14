package com.niguang.daxianfeng.model;

import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class DiceParams extends Params {
	private String heroPos;
	private int diceNum;
	private List<String> diceTypes;

	public DiceParams(JSONObject params) {
		super(params);
		heroPos = params.getString("heroPos");
		diceNum = Integer.parseInt(params.getString("diceNum"));
		diceTypes = JSON.parseArray(params.getString("diceTypes"), String.class);
	}

}