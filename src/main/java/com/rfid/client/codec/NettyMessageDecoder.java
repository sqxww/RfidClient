package com.rfid.client.codec;

import java.util.HashMap;
import java.util.Map;

import com.rfid.client.pojo.BodyType;
import com.rfid.client.pojo.Header;
import com.rfid.client.pojo.NettyMessage;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

public class NettyMessageDecoder extends LengthFieldBasedFrameDecoder {

	public NettyMessageDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength, int lengthAdjustment) {
		super(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, 0);
		
	}
	
	@Override
	protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
		//读取整包消息
		ByteBuf frame = (ByteBuf) super.decode(ctx, in);
		if(frame == null)
			return null;
		NettyMessage message = new NettyMessage();
		Header header = new Header();
		//获取消息头信息
		header.setCrcCode(frame.readInt());
		header.setLength(frame.readInt());
		header.setSessionID(frame.readLong());
		header.setType(frame.readByte());
		header.setBodyType(frame.readByte());
		header.setStatu(frame.readByte());
		
		//获取自定义的消息头信息
		int size = frame.readInt();
		if(size > 0){
			Map<String, String> attch = new HashMap<String, String>(size);
			int keySize = 0;
			byte[] keyArray = null;
			String key = null;
			int valueSize = 0;
			byte[] valueArray = null;
			String value = null;
			for(int i = 0; i < size; i++){
				//获取键长度
				keySize = frame.readInt();
				keyArray = new byte[keySize];
				//获取键的字节值
				frame.readBytes(keyArray);
				//转为字符串
				key = new String(keyArray, "UTF-8");
				
				//获取值长度
				valueSize = frame.readInt();
				valueArray = new byte[valueSize];
				//获取值的字节值
				frame.readBytes(valueArray);
				//转为字符串
				value = new String(valueArray, "UTF-8");
				attch.put(key, value);
			}
			keyArray = null;
			key = null;
			valueArray = null;
			value = null;
			header.setAttachment(attch);
		}
		//获取消息体信息
		if(frame.readableBytes() > 4){
			//消息体长度
			int bodySize = frame.readInt();
			byte[] bodyArray = new byte[bodySize];
			//消息体值
			frame.readBytes(bodyArray);
			if(header.getBodyType() == BodyType.STR.value()){
				String body = new String(bodyArray, "UTF-8");
				message.setBody(body);
			} else{
				message.setBody(bodyArray);
			}
		}
		message.setHeader(header);
		return message;
	}

}
