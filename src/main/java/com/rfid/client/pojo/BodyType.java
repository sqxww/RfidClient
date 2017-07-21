package com.rfid.client.pojo;

public enum BodyType {
	
	STR((byte) 0),JPG((byte) 1);
	
	private byte value;
	
	private BodyType(byte value){
		this.value = value;
	}
	
	public byte value(){
		return this.value;
	}
}
