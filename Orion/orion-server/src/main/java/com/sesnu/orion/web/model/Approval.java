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
@Table(name = "	APPROVALS")
public class Approval {
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="aprv_seq")
	@SequenceGenerator(name="aprv_seq",sequenceName="aprv_seq",allocationSize=20)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "type")
	private String type;
	
	@Column(name = "for_name")
	private String forName;
	
	@Column(name = "for_id")
	private long forId;
	
	@Column(name="order_ref")
	private long orderRef;
	
	@Column(name="requested_by")
	private String requestedBy;

	@Column(name = "approver")
	private String approver;
	
	@Column(name = "requested_on")
	private Date requestedOn;
	
	@Column(name = "approved_on")
	private Date approvedOn;

	@Column(name = "status")
	private String status;
	
	@Column(name = "remark")
	private String remark;
	
	@Column(name = "df")
	private boolean df;

	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public long getOrderRef() {
		return orderRef;
	}

	public void setOrderRef(long orderRef) {
		this.orderRef = orderRef;
	}

	public String getRequestedBy() {
		return requestedBy;
	}

	public void setRequestedBy(String requestedBy) {
		this.requestedBy = requestedBy;
	}

	public String getApprover() {
		return approver;
	}

	public void setApprover(String approver) {
		this.approver = approver;
	}

	public Date getRequestedOn() {
		return requestedOn;
	}

	public void setRequestedOn(Date requestedOn) {
		this.requestedOn = requestedOn;
	}

	public Date getApprovedOn() {
		return approvedOn;
	}

	public void setApprovedOn(Date approvedOn) {
		this.approvedOn = approvedOn;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public boolean isDf() {
		return df;
	}

	public void setDf(boolean df) {
		this.df = df;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public long getForId() {
		return forId;
	}

	public void setForId(long forId) {
		this.forId = forId;
	}

	public String getForName() {
		return forName;
	}

	public void setForName(String forName) {
		this.forName = forName;
	}
	


	
}
