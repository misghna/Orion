package com.sesnu.orion.web.model;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "DOC_TRACK_VIEW")
public class DocTrackingView {
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="dlcns_seq")
	@SequenceGenerator(name="dlcns_seq",sequenceName="dlcns_seq",allocationSize=20)
	@Column(name = "id")
	private Long id;
		
	@Column(name="order_ref")
	private long orderRef;
	
	@Column(name = "inv_No")
	private String invNo;
	
	@Column(name = "bl")
	private String bl;
	
	@Column(name = "documents")
	private String documents;
	
	@Column(name = "courier")
	private String courier;
	
	@Column(name = "tracking_id")
	private String trackingId;
	
	@Column(name = "received_on")
	private Date receivedOn;
	
	@Column(name = "received_by")
	private Date receivedBy;
	
	@Column(name = "remark")
	private String remark;
	
	@Column(name = "updated_on")
	private String updatedOn;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public long getOrderRef() {
		return orderRef;
	}

	public void setOrderRef(long orderRef) {
		this.orderRef = orderRef;
	}

	public String getBl() {
		return bl;
	}

	public void setBl(String bl) {
		this.bl = bl;
	}

	public String getCourier() {
		return courier;
	}

	public void setCourier(String courier) {
		this.courier = courier;
	}

	public String getTrackingId() {
		return trackingId;
	}

	public void setTrackingId(String trackingId) {
		this.trackingId = trackingId;
	}

	public Date getReceivedOn() {
		return receivedOn;
	}

	public void setReceivedOn(Date receivedOn) {
		this.receivedOn = receivedOn;
	}

	public Date getReceivedBy() {
		return receivedBy;
	}

	public void setReceivedBy(Date receivedBy) {
		this.receivedBy = receivedBy;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdatedOn(String updatedOn) {
		this.updatedOn = updatedOn;
	}

	public String getInvNo() {
		return invNo;
	}

	public void setInvNo(String invNo) {
		this.invNo = invNo;
	}

	public String getDocuments() {
		return documents;
	}

	public void setDocuments(String documents) {
		this.documents = documents;
	}

	
	
	
	
}
