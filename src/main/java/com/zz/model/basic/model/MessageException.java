package com.zz.model.basic.model;

import cn.shengyuan.tools.util.ResourceUtils;

/**
 * 消息类异常
 * 
 * @author Administrator
 *
 */
public class MessageException extends RuntimeException {
	private static final long serialVersionUID = -7307578142902361462L;

	public MessageException() {
		super();
	}

	public MessageException(String message) {
		super(ResourceUtils.getString("message", message));
	}
}
