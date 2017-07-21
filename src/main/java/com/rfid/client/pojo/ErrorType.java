package com.rfid.client.pojo;

public enum ErrorType {
	OTH_LOG((byte) 1),READ_TIMEOUT((byte) 2),OTH_ERROR((byte) 3);
	
	private byte value;
	
	private ErrorType(byte value){
		this.value = value;
	}
	public byte value(){
		return this.value;
	}
}
