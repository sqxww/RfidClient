package com.rfid.client.handler.cprg;

import com.rfid.client.pojo.MessageType;
import com.rfid.client.pojo.cprg.CMessage;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class CHeartBeatReqHandler extends ChannelInboundHandlerAdapter {
	
	
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		CMessage message = (CMessage) msg;
		if(message!= null
				&& message.getType() == MessageType.HEARTBEAT_RESP.value()){
			System.out.println("Client receive heart beat : " + message);
		}else
			ctx.fireChannelRead(msg);
	}
	
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		ctx.fireExceptionCaught(cause);
	}
}
