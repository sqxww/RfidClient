package com.rfid.client.handler.cprg;

import com.rfid.client.pojo.ErrorType;
import com.rfid.client.pojo.MessageType;
import com.rfid.client.pojo.StatuType;
import com.rfid.client.pojo.cprg.CMessage;
import com.rfid.client.util.ErrorRespQueue;
import com.rfid.client.util.ReqExecutor;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class CReqHandler extends ChannelInboundHandlerAdapter {
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		CMessage message = (CMessage) msg;
		//判断是否是服务响应消息
		if(message != null && message.getType() == MessageType.SERVICE_RESP.value()){
			if(message.getStatus() == StatuType.SUB_OK.value()){
				//将响应消息放入响应消息队列中
//				RespMessageQueueUtil.getRespMsgQueue().add(message);
				//异处登录
			}else if(message.getStatus() == StatuType.OTH_LOG.value()){
				System.out.println("------------------------>otherLog");
				throw new Exception("OTH_LOG");
			}else if(message.getStatus() == StatuType.NO_LOG.value()){
				
			}
		}else{
			//错误响应
		}
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		ctx.close();
		ReqExecutor.shutdown();
		if(cause.getMessage().equals("OTH_LOG")){
			ErrorRespQueue.getInstance().add(ErrorType.OTH_LOG.value());
		}
	}
}
