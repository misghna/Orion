package com.sesnu.orion.web.model;

import org.json.simple.JSONObject;

public class Estimate {
	
	private double value;
	private JSONObject details;
	
	
	
	public Estimate(double value, JSONObject details) {
		this.value = value;
		this.details = details;
	}
	
	
	public double getValue() {
		return value;
	}
	public void setValue(double value) {
		this.value = value;
	}
	public JSONObject getDetails() {
		return details;
	}
	public void setDetails(JSONObject details) {
		this.details = details;
	}
	
	

}
