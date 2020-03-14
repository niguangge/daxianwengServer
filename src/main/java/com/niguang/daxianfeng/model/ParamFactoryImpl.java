package com.niguang.daxianfeng.model;

import com.alibaba.fastjson.JSONObject;
import com.niguang.daxianfeng.exceptions.NotFoundParamByStageException;

public class ParamFactoryImpl implements ParamFatory {

	@Override
	public Params makeParamsByJsonMsg(JSONObject params) throws Exception {
		switch ((String) params.get("stage")) {
		case "dice":
			return new DiceParams(params);
		case "operation":
			return new OperationParams(params);
		default:
			throw new NotFoundParamByStageException(params.getString("stage"));
		}
	}

}
