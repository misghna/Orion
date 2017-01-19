package com.sesnu.orion.web.model;

import org.json.simple.JSONObject;

public class Estimate_old {
	
	
	private long item_id;
	private Long order_id;
	private Double portFee;
	private Double terminalFee;
	private Double transportFee;
	private Double licenseFee;
	private Double customs;
	private Double bromongol;
	private Double agentFee;
	private Double legalizationFee;
	private JSONObject details;;
		
	public long getItem_id() {
		return item_id;
	}
	public void setItem_id(long item_id) {
		this.item_id = item_id;
	}
	public Long getOrder_id() {
		return order_id;
	}
	public void setOrder_id(Long order_id) {
		this.order_id = order_id;
	}
	public Double getPortFee() {
		return portFee;
	}
	public void setPortFee(Double portFee) {
		this.portFee = portFee;
	}
	public Double getTerminalFee() {
		return terminalFee;
	}
	public void setTerminalFee(Double terminalFee) {
		this.terminalFee = terminalFee;
	}
	public Double getTransportFee() {
		return transportFee;
	}
	public void setTransportFee(Double transportFee) {
		this.transportFee = transportFee;
	}
	public Double getLicenseFee() {
		return licenseFee;
	}
	public void setLicenseFee(Double licenseFee) {
		this.licenseFee = licenseFee;
	}
	public Double getCustoms() {
		return customs;
	}
	public void setCustoms(Double customs) {
		this.customs = customs;
	}
	public Double getBromongol() {
		return bromongol;
	}
	public void setBromongol(Double bromongol) {
		this.bromongol = bromongol;
	}
	public Double getAgentFee() {
		return agentFee;
	}
	public void setAgentFee(Double agentFee) {
		this.agentFee = agentFee;
	}
	public Double getLegalizationFee() {
		return legalizationFee;
	}
	public void setLegalizationFee(Double legalizationFee) {
		this.legalizationFee = legalizationFee;
	}
	public JSONObject getDetails() {
		return details;
	}
	public void setDetails(JSONObject details) {
		this.details = details;
	}

	
	
	

}
