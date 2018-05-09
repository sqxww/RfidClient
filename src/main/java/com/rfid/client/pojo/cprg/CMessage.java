package com.rfid.client.pojo.cprg;

public class CMessage {
	private Integer crcCode;
	private Integer messageLength;
	private Long sessionId;
	private Byte type;
	private Byte bodyType;
	private String url;
	private Object body;
	private Byte status;
	public Integer getCrcCode() {
		return crcCode;
	}
	public void setCrcCode(Integer crcCode) {
		this.crcCode = crcCode;
	}
	public Integer getMessageLength() {
		return messageLength;
	}
	public void setMessageLength(Integer messageLength) {
		this.messageLength = messageLength;
	}
	public Long getSessionId() {
		return sessionId;
	}
	public void setSessionId(Long sessionId) {
		this.sessionId = sessionId;
	}
	public Byte getType() {
		return type;
	}
	public void setType(Byte type) {
		this.type = type;
	}
	public Byte getBodyType() {
		return bodyType;
	}
	public void setBodyType(Byte bodyType) {
		this.bodyType = bodyType;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Object getBody() {
		return body;
	}
	public void setBody(Object body) {
		this.body = body;
	}
	public Byte getStatus() {
		return status;
	}
	public void setStatus(Byte status) {
		this.status = status;
	}
}
