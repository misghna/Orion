package com.sesnu.orion.web.model;

public class TCPResponse {

	
	private String msg;
	private Integer code;
	private Object response;
	
	
	public TCPResponse(String msg, Integer code, Object response) {
		super();
		this.msg = msg;
		this.code = code;
		this.response = response;
	}
	
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public Integer getCode() {
		return code;
	}
	public void setCode(Integer code) {
		this.code = code;
	}

	public Object getResponse() {
		return response;
	}

	public void setResponse(Object response) {
		this.response = response;
	}
	
	
}
