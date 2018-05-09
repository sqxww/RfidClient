package com.rfid.client.codec;

import com.rfid.client.pojo.BodyType;
import com.rfid.client.pojo.cprg.CMessage;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class CMessageEncoder extends MessageToByteEncoder<CMessage> {

	@Override
	protected void encode(ChannelHandlerContext arg0, CMessage msg, ByteBuf out) throws Exception {
		if(msg == null)
			throw new Exception("The encode message is null");
		//写消息头信息
		out.writeInt(msg.getCrcCode());
		out.writeInt(msg.getMessageLength());
		out.writeLong(msg.getSessionId());
		out.writeByte(msg.getType());
		out.writeByte(msg.getBodyType());
		out.writeZero(13);
		
		//写消息体信息
		if(msg.getBody() != null){
			//判断消息体是否是字符串类型
			if(msg.getBodyType() == BodyType.STR.value()){
				String body = (String) msg.getBody();
				byte[] bodyArray = body.getBytes("UTF-8");
				out.writeInt(bodyArray.length);
				out.writeBytes(bodyArray);
			}else{
				byte[] body = (byte[]) msg.getBody();
				out.writeInt(body.length);
				out.writeBytes(body);
			}
		}else
			out.writeInt(0);
		//重新给消息长度赋值
		out.setInt(4, out.readableBytes());
		
	}

}
