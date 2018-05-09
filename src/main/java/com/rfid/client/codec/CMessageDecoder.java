package com.rfid.client.codec;

import com.rfid.client.pojo.BodyType;
import com.rfid.client.pojo.cprg.CMessage;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

public class CMessageDecoder extends LengthFieldBasedFrameDecoder {
	public CMessageDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength, int lengthAdjustment) {
		super(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, 0);
	}
	
	@Override
	protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
		//读取整包消息
		ByteBuf frame = (ByteBuf) super.decode(ctx, in);
		if(frame == null)
			return null;
		CMessage message = new CMessage();

		//获取消息头信息
		message.setCrcCode(frame.readInt());
		message.setMessageLength(frame.readInt());
		message.setSessionId(frame.readLong());
		message.setType(frame.readByte());
		message.setBodyType(frame.readByte());
		message.setStatus(frame.readByte());
		frame.skipBytes(13);
		
		
		//获取消息体信息
		if(frame.readableBytes() > 4){
			//消息体长度
			int bodySize = frame.readInt();
			byte[] bodyArray = new byte[bodySize];
			//消息体值
			frame.readBytes(bodyArray);
			if(message.getBodyType() == BodyType.STR.value()){
				String body = new String(bodyArray, "UTF-8");
				message.setBody(body);
			} else{
				message.setBody(bodyArray);
			}
		}

		return message;
	}
}
