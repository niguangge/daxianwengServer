package com.niguang.daxianfeng.service;

import java.util.HashMap;
import java.util.Map;

import com.niguang.daxianfeng.common.Constant;

public class DiceService {
	private static Map<String, Integer[]> DICE_MAP = new HashMap<>();
	private static Integer[] normal = { 1, 2, 3, 4, 5, 6 };
	private static Integer[] plusOne = { 0, 1, 1, 1, 1, 1 };
	private static Integer[] minusOne = { 0, -1, -1, -1, -1, -1 };

	static {
		DICE_MAP.put(Constant.DICE_NORMAL, normal);
		DICE_MAP.put(Constant.DICE_PLUS_ONE, plusOne);
		DICE_MAP.put(Constant.DICE_MINUS_ONE, minusOne);
	}

	public static Integer[] get(String name) {
		return DICE_MAP.get(name);
	}
}
