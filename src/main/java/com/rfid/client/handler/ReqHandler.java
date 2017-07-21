package com.rfid.client.handler;

import com.rfid.client.pojo.ErrorType;
import com.rfid.client.pojo.MessageType;
import com.rfid.client.pojo.NettyMessage;
import com.rfid.client.pojo.StatuType;
import com.rfid.client.util.ErrorRespQueue;
import com.rfid.client.util.ReqExecutor;
import com.rfid.client.util.RespMessageQueueUtil;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ReqHandler extends ChannelInboundHandlerAdapter {
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		NettyMessage message = (NettyMessage) msg;
		//判断是否是服务响应消息
		if(message != null && message.getHeader().getType() == MessageType.SERVICE_RESP.value()){
			if(message.getHeader().getStatu() == StatuType.SUB_OK.value()){
				//将响应消息放入响应消息队列中
				RespMessageQueueUtil.getRespMsgQueue().add(message);
				//异处登录
			}else if(message.getHeader().getStatu() == StatuType.OTH_LOG.value()){
				System.out.println("------------------------>otherLog");
				throw new Exception("OTH_LOG");
			}else if(message.getHeader().getStatu() == StatuType.NO_LOG.value()){
				
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
