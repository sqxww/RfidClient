package com.rfid.client.pojo;

import java.util.HashMap;
import java.util.Map;

public class Header {
	private int crcCode;
	private int length;
	private long sessionID;
	private byte type;
	private byte bodyType;
	private byte statu;
	private Map<String, String> attachment = new HashMap<String, String>();
	public final int getCrcCode() {
		return crcCode;
	}
	public final void setCrcCode(int crcCode) {
		this.crcCode = crcCode;
	}
	public final int getLength() {
		return length;
	}
	public final void setLength(int length) {
		this.length = length;
	}
	public final long getSessionID() {
		return sessionID;
	}
	public final void setSessionID(long sessionID) {
		this.sessionID = sessionID;
	}
	public final byte getType() {
		return type;
	}
	public final void setType(byte type) {
		this.type = type;
	}
	public final Map<String, String> getAttachment() {
		return attachment;
	}
	public final void setAttachment(Map<String, String> attachment) {
		this.attachment = attachment;
	}
	public final byte getBodyType() {
		return bodyType;
	}
	public final void setBodyType(byte bodyType) {
		this.bodyType = bodyType;
	}
	public final byte getStatu() {
		return statu;
	}
	public final void setStatu(byte statu) {
		this.statu = statu;
	}
	@Override
	public String toString() {
		return "Header [crcCode=" + crcCode + ", length=" + length + ", sessionID=" + sessionID + ", type=" + type
				+ ", bodyType=" + bodyType + ", statu=" + statu + ", attachment=" + attachment + "]";
	}
	
	
}
