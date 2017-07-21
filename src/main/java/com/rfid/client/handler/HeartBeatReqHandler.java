package com.rfid.client.handler;

import com.rfid.client.pojo.MessageType;
import com.rfid.client.pojo.NettyMessage;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class HeartBeatReqHandler extends ChannelInboundHandlerAdapter {
	
	
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		NettyMessage message = (NettyMessage) msg;
		if(message.getHeader() != null
				&& message.getHeader().getType() == MessageType.HEARTBEAT_RESP.value()){
			System.out.println("Client receive heart beat : " + message);
		}else
			ctx.fireChannelRead(msg);
	}
	
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		ctx.fireExceptionCaught(cause);
	}
}
