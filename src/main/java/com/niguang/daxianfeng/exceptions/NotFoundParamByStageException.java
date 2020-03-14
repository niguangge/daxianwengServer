package com.niguang.daxianfeng.exceptions;

public class NotFoundParamByStageException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5753704523714843163L;
	/**
	 * 
	 */
	private String stage;

	public NotFoundParamByStageException(String stage) {
		super("Can't found params which stage is " + stage);
		this.stage = stage;
	}

	public String getStage() {
		return stage;
	}
}
