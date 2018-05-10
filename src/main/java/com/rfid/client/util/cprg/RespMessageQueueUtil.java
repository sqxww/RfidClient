package com.rfid.client.util.cprg;

public class RespMessageQueueUtil {
	private static final RespMessageQueue respMsgQueue = new RespMessageQueue(100);
	
	public static RespMessageQueue getRespMsgQueue(){
		return respMsgQueue;
	}
}
