package com.niguang.daxianfeng.model;

import com.alibaba.fastjson.JSONObject;

public interface ParamFatory {
	public Params makeParamsByJsonMsg(JSONObject params) throws Exception;
}
