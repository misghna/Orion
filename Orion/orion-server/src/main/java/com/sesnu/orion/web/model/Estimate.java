package com.sesnu.orion.web.model;

import org.json.simple.JSONObject;

public class Estimate {
	
	private double value;
	private JSONObject details;
	private JSONObject summary;
	
	
	
	public Estimate(double value, JSONObject details, JSONObject summary) {
		this.value = value;
		this.details = details;
		this.summary = summary;
	}


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


	public JSONObject getSummary() {
		return summary;
	}


	public void setSummary(JSONObject summary) {
		this.summary = summary;
	}
	
	

}
