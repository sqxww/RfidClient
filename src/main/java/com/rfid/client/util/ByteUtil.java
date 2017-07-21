package com.rfid.client.util;

import java.io.IOException;
import java.util.Arrays;

public class ByteUtil {
	
	public static void main(String[] args) throws IOException{
//		byte[] length = new byte[]{27,0,0,0};
//		byte[] length1 = new byte[]{66,0,0,0};
//		byte[] newlength = new byte[4];
//		for(int i = 0; i < newlength.length; i++){
//			newlength[i] = length1[3  - i];
//		}
//		System.out.println("字节数组{27,0,0,0}:" + bytesToInt(length));
//		System.out.println("字节数组{66,0,0,0}:" + bytesToInt(newlength));
		int a = 0xabef0101;
		byte[] ab = intToBytes(a);
		byte[] n = new byte[4];
		for(int i = 0; i < ab.length; i++){
			n[n.length - 1 - i] = ab[i]; 
		}
		System.out.println(a);
		System.out.println(bytesToInt(n));
	}
	
	public static byte[] intToBytes(int v){
		byte[] result = new byte[4];
		result[0] = (byte) ((v >>> 24) & 0xFF);
		result[1] = (byte) ((v >>> 16) & 0xFF);
		result[2] = (byte) ((v >>> 8) & 0xFF);
		result[3] = (byte) ((v >>> 0) & 0xFF);
		return result;
	}
	
	public static int bytesToInt(byte[] bytes){
		int ch1 = (bytes[0] & 0xff);
		int ch2 = (bytes[1] & 0xff);
		int ch3 = (bytes[2] & 0xff);
		int ch4 = (bytes[3] & 0xff);
		return ((ch1 << 24) + (ch2 << 16) + (ch3 << 8) + (ch4 << 0));
	}
	
	public static int bytesToShort(byte[] bytes){
		int ch1 = bytes[0];
		int ch2 = bytes[1];
		return (short)((ch1 << 8) + (ch2 << 0));
	}
	
	public static long bytesToLong(byte[] bytes){
		return (((long)bytes[0] << 56) +
                ((long)(bytes[1] & 255) << 48) +
                ((long)(bytes[2] & 255) << 40) +
                ((long)(bytes[3] & 255) << 32) +
                ((long)(bytes[4] & 255) << 24) +
                ((bytes[5] & 255) << 16) +
                ((bytes[6] & 255) <<  8) +
                ((bytes[7] & 255) <<  0));
	}
}
