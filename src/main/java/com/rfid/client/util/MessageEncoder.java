package com.rfid.client.util;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Map;

import com.rfid.client.pojo.BodyType;
import com.rfid.client.pojo.NettyMessage;

public class MessageEncoder {
	public byte[] encoder(NettyMessage msg) throws IOException{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		//写校验码
		dos.writeInt(msg.getHeader().getCrcCode());
		//写消息长度
		dos.writeInt(msg.getHeader().getLength());
		//写会话id
		dos.writeLong(msg.getHeader().getSessionID());
		//写消息类型
		dos.writeByte(msg.getHeader().getType());
		//写消息体类型
		dos.writeByte(msg.getHeader().getBodyType());
		
		byte[] keyArray = null;
		String key = null;
		byte[] valueArray = null;
		String value = null;
		
		//写自定义消息头的个数
		dos.writeInt(msg.getHeader().getAttachment().size());
		
		for(Map.Entry<String, String> entry : msg.getHeader().getAttachment().entrySet()){
			key = entry.getKey();
			//用UTF-8编码格式将字符串转成字节数组
			keyArray = key.getBytes("UTF-8");
			//写键的长度
			dos.writeInt(keyArray.length);
			//写键的字节码
			dos.write(keyArray);
			
			value = entry.getValue();
			valueArray = value.getBytes("UTF-8");
			//写值的长度
			dos.writeInt(valueArray.length);
			//写值的字节码
			dos.write(valueArray);
		}
		
		if(msg.getBody() != null){
			//判断消息体是否是字符串类型
			if(msg.getHeader().getBodyType() == BodyType.STR.value()){
				String body = (String) msg.getBody();
				System.out.println(body.length() + " String body");
				byte[] bodyArray = body.getBytes("UTF-8");
				System.out.println(bodyArray.length + " bodyArray");
				//写消息体长度
				dos.writeInt(bodyArray.length);
				//写消息体值
				dos.write(bodyArray);
			}else{
				byte[] body = (byte[]) msg.getBody();
				
				dos.writeInt(body.length);
				
				dos.write(body);
			}
		}else{
			dos.writeInt(0);
		}
		byte[] encodedBytes = baos.toByteArray();
		//重新给消息长度字段赋值
		int length = encodedBytes.length;
		byte[] lengBytes = ByteUtil.intToBytes(length);
		for(int i = 0; i < lengBytes.length; i++){
			encodedBytes[4 + i] = lengBytes[i];
		}
		
		return encodedBytes;
	}
}
