package com.rfid.client.handler;

import com.rfid.client.pojo.ErrorType;
import com.rfid.client.pojo.Header;
import com.rfid.client.pojo.MessageType;
import com.rfid.client.pojo.NettyMessage;
import com.rfid.client.pojo.StatuType;
import com.rfid.client.util.ErrorRespQueue;
import com.rfid.client.util.ReqExecutor;
import com.rfid.client.util.ReqMessageQueue;
import com.rfid.client.util.RespMessageQueueUtil;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class LoginAuthReqHandler extends ChannelInboundHandlerAdapter {
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		ctx.writeAndFlush(buildLoginReq());
	}


	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		NettyMessage message = (NettyMessage) msg;
		//如果是握手应答消息，需要判断是否认证成功
		if(message.getHeader() != null && message.getHeader().getType() == MessageType.LOGIN_RESP.value()){
			Header header = message.getHeader();
			RespMessageQueueUtil.getRespMsgQueue().add(message);
			//握手失败
			if(header.getStatu() != StatuType.SUB_OK.value()){
				//获取握手失败状态，进行相对应的处理
				/*
				 * 重复登录
				 * 用户名或密码错误
				 * 会话过期
				 */
			}else {
				System.out.println("login is ok : " + message);
				//业务请求处理线程
				ReqExecutor.exectue(new ReqTask(ctx));
				ctx.fireChannelRead(msg);
			} 
		} else
			ctx.fireChannelRead(msg);
	}

	private NettyMessage buildLoginReq() throws InterruptedException{
		NettyMessage message = ReqMessageQueue.getInstance().remove();
		return message;
	}


	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		ctx.close();
		ReqExecutor.shutdown();
	}

	private class ReqTask implements Runnable{
		private final ReqMessageQueue reqMsgQueue;
		private final ChannelHandlerContext ctx;
		ReqTask(ChannelHandlerContext ctx){
			this.ctx = ctx;
			this.reqMsgQueue = ReqMessageQueue.getInstance();
		}
		public void run() {

			while(!Thread.currentThread().isInterrupted()){
				try {
					NettyMessage reqMsg = reqMsgQueue.remove();
					ctx.writeAndFlush(reqMsg);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}
			System.out.println("reqThread----->destroying.........");

		}

	}

}
