package com.rfid.client.main;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.Executors;

import com.rfid.client.pojo.BodyType;
import com.rfid.client.pojo.ErrorType;
import com.rfid.client.pojo.Header;
import com.rfid.client.pojo.MessageType;
import com.rfid.client.pojo.NettyMessage;
import com.rfid.client.pojo.StatuType;
import com.rfid.client.util.ErrorRespQueue;
import com.rfid.client.util.HeartBeatExectuor;
import com.rfid.client.util.MessageEncoder;
import com.rfid.client.util.ReqMessageQueue;
import com.rfid.client.util.RespMessageQueueUtil;

public class Main {
	public static void main(String[] args) throws IOException{
		new Thread(new Runnable() {

			public void run() {
				try {
					new RfidClient().connect(8080, "123.207.246.26");
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		}).start();
		NettyMessage loginReq = new NettyMessage();
		Header reqHeader = new Header();
		reqHeader.setType(MessageType.LOGIN_REQ.value());
		reqHeader.setBodyType(BodyType.STR.value());
		String body = "{\"userName\":\"admin\",\"password\":\"123456\"}";
		loginReq.setHeader(reqHeader);
		loginReq.setBody(body);
		try {
			ReqMessageQueue.getInstance().add(loginReq);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		NettyMessage loginResp = null;
		try {
			loginResp = RespMessageQueueUtil.getRespMsgQueue().remove();
		} catch (InterruptedException e) {
			System.out.println(e.getMessage());
		}
		if(loginResp != null 
				&& loginResp.getHeader().getType()
				== MessageType.LOGIN_RESP.value()
				&& loginResp.getHeader().getStatu()
				== StatuType.SUB_OK.value() ){
			final long sessionId = loginResp.getHeader().getSessionID();
			HeartBeatExectuor.execute(new Runnable() {

				public void run() {
					NettyMessage heartBeatMsg = new NettyMessage();
					Header header = new Header();
					header.setType(MessageType.HEARTBEAT_REQ.value());
					header.setSessionID(sessionId);
					heartBeatMsg.setHeader(header);
					while(!Thread.currentThread().isInterrupted()){
						try {
							ReqMessageQueue.getInstance().add(heartBeatMsg);
							System.out.println("client send heart beat : >>>" + heartBeatMsg);
							//获取错误信息
							Byte errorType = ErrorRespQueue.getInstance().remove();
							//异处登录
							if(errorType != null && errorType == ErrorType.OTH_LOG.value()){
								//退出循环
								System.out.println("准备退出循环");
								break;
							}
							//读超时
							/*
							 * 
							 */
							//未知错误
							/*
							 * 
							 */

						} catch (InterruptedException e) {
							if(e.getMessage().equals("getErrorMsgTimeout")){
								continue;
							}else{
								Thread.currentThread().interrupt();
							}
						}

					}
					//清空各队列
					ErrorRespQueue.getInstance().clear();
					ReqMessageQueue.getInstance().clear();
					RespMessageQueueUtil.getRespMsgQueue().clear();
				}
			});
		}
		//创建消息实例
		NettyMessage reqMsg = new NettyMessage();
		//创建消息头实例
		Header header = new Header();
		//设置请求体类型
		header.setBodyType(BodyType.JPG.value());
		//设置请求类型
		header.setType(MessageType.SERVICE_REQ.value());
		//设置会话信息
		header.setSessionID(loginResp.getHeader().getSessionID());
		//创建自定义请求头Map集合
		Map<String, String> attch = header.getAttachment();
		//放入自定义请求头信息
		attch.put("action", "labelimg");
		attch.put("labelcode", "300833B2DDD9014000000008");
		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date d = new Date(System.currentTimeMillis());
		String time = df.format(d);
		//将请求头放入消息实例中
		reqMsg.setHeader(header);
		String reqbody = "{\"labelCode\":\"300833B2DDD9014000000008\","
				+ "\"x\":\"2\",\"y\":\"3\","
				+ "\"lastTime\":\"" + time + "\",\"lastLocal\":\"广东工业大学教学4号楼\","
				+ "\"found\":\"0\"}";
//		reqMsg.setBody(reqbody);
		FileInputStream fi = new FileInputStream("C://Users//liruxia//Pictures//Camera Roll//WIN_20170502_20_58_52_Pro.jpg");
		byte[] bytes = new byte[fi.available()];
		fi.read(bytes);
		System.out.println(bytes.length);
		reqMsg.setBody(bytes);
		NettyMessage respMsg = null;
		try {
			//将请求消息放入请求队列中
			ReqMessageQueue.getInstance().add(reqMsg);
			respMsg = RespMessageQueueUtil.getRespMsgQueue().remove();
		} catch (InterruptedException e) {
			System.out.println(e);
		}
		System.out.println("main ---->" + respMsg.getBody());
	}
}
