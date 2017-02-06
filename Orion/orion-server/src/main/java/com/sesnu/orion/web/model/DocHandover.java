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
@Table(name = "DOC_HANDOVER")
public class DocHandover {
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="doc_hdo_seq")
	@SequenceGenerator(name="doc_hdo_seq",sequenceName="doc_hdo_seq",allocationSize=20)
	@Column(name = "id")
	private Long id;

	@Column(name = "bl")
	private String bl;
	
	@Column(name = "inv_no")
	private String invNo;
	
	@Column(name = "received_from")
	private String receivedFrom;
	
	@Column(name = "received_by")
	private String receivedBy;
	
	@Column(name = "docs")
	private String docs;
	
	@Column(name = "received_on")
	private Date receivedOn;
	
	@Column(name = "returned_to")
	private String returnedTo;
	
	@Column(name = "returned_on")
	private Date returnedOn;
	
	@Column(name = "status")
	private String status;
	
	@Column(name = "submitted_on")
	private Date submittedOn;
	
	
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getBl() {
		return bl;
	}

	public void setBl(String bl) {
		this.bl = bl;
	}

	public String getReceivedFrom() {
		return receivedFrom;
	}

	public void setReceivedFrom(String receivedFrom) {
		this.receivedFrom = receivedFrom;
	}

	public String getReceivedBy() {
		return receivedBy;
	}

	public void setReceivedBy(String receivedBy) {
		this.receivedBy = receivedBy;
	}

	public String getDocs() {
		return docs;
	}

	public void setDocs(String docs) {
		this.docs = docs;
	}

	public Date getReceivedOn() {
		return receivedOn;
	}

	public void setReceivedOn(Date receivedOn) {
		this.receivedOn = receivedOn;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getSubmittedOn() {
		return submittedOn;
	}

	public void setSubmittedOn(Date submittedOn) {
		this.submittedOn = submittedOn;
	}

	public String getReturnedTo() {
		return returnedTo;
	}

	public void setReturnedTo(String returnedTo) {
		this.returnedTo = returnedTo;
	}

	public Date getReturnedOn() {
		return returnedOn;
	}

	public void setReturnedOn(Date returnedOn) {
		this.returnedOn = returnedOn;
	}

	public String getInvNo() {
		return invNo;
	}

	public void setInvNo(String invNo) {
		this.invNo = invNo;
	}




	
}
