package com.rfid.client.codec;

import java.util.Map;

import com.rfid.client.pojo.BodyType;
import com.rfid.client.pojo.NettyMessage;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class NettyMessageEncoder extends MessageToByteEncoder<NettyMessage> {

	@Override
	protected void encode(ChannelHandlerContext ctx, NettyMessage msg, ByteBuf out) throws Exception {
		if(msg == null)
			throw new Exception("The encode message is null");
		//写消息头信息
		out.writeInt(msg.getHeader().getCrcCode());
		out.writeInt(msg.getHeader().getLength());
		out.writeLong(msg.getHeader().getSessionID());
		out.writeByte(msg.getHeader().getType());
		out.writeByte(msg.getHeader().getBodyType());
		
		//写消息头自定义消息
		out.writeInt(msg.getHeader().getAttachment().size());
		
		byte[] keyArray = null;
		String key = null;
		byte[] valueArray = null;
		String value = null;
		for(Map.Entry<String, String> params
				: msg.getHeader().getAttachment().entrySet()){
			key = params.getKey();
			//字符串消息用UTF-8编码
			keyArray = key.getBytes("UTF-8");
			out.writeInt(keyArray.length);
			out.writeBytes(keyArray);
			value = params.getValue();
			valueArray = value.getBytes("UTF-8");
			out.writeInt(valueArray.length);
			out.writeBytes(valueArray);
		}
		key = null;
		value = null;
		keyArray = null;
		valueArray = null;
		
		//写消息体信息
		if(msg.getBody() != null){
			//判断消息体是否是字符串类型
			if(msg.getHeader().getBodyType() == BodyType.STR.value()){
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
