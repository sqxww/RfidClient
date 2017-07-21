package com.rfid.client.pojo;

public enum StatuType {
	
	SUB_OK((byte) 0),REP_LOG((byte) 1),NO_USER((byte) 2),
	NO_LOG((byte) 3),OTH_LOG((byte) 4),NO_ACT((byte) 5),
	TYP_ERR((byte) 6);
	
	private byte value;
	
	private StatuType(byte value){
		this.value = value;
	}
	
	public byte value(){
		return this.value;
	}
}
