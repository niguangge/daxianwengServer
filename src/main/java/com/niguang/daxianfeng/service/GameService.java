package com.niguang.daxianfeng.service;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;


@Service("gameService")
public class GameService {
    public String messageHandler(int roomId,long userId,String message) throws NoSuchMethodException, SecurityException{
    	Method method = this.getClass().getMethod("getName");
    	String type = "personal";
    	JSONObject obj = JSON.parseObject(message);
    	if("all".equals(obj.get("type"))){
    		type = "all";
    	}
    	if("dice".equals(obj.get("stage"))) {
    		diceHandler(obj.get("dice"));
    	}else if("event".equals(obj.get("stage"))){
    		eventHandler(obj.get("event"));
    	}else if("operation".equals(obj.get("stage"))){
    		operationHandler(obj.get("operation"));
    	}
        return type;
    }
    private void diceHandler(Object params) {
    	
    }

	private void eventHandler(Object params) {

	}

    private void operationHandler(Object params) {
    	
    }
}
