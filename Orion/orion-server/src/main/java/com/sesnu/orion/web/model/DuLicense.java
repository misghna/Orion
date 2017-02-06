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
@Table(name = "DU_LICENSE")
public class DuLicense {
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="dlcns_seq")
	@SequenceGenerator(name="dlcns_seq",sequenceName="dlcns_seq",allocationSize=20)
	@Column(name = "id")
	private Long id;
		
	@Column(name="order_ref")
	private long orderRef;
	
	@Column(name = "git")
	private String git;
	
	@Column(name = "du_no")
	private String duNo;
	
	@Column(name="pedidu_no")
	private String pediduNo;
	
	@Column(name="issue_date")
	private Date issueDate;

	@Column(name="expire_date")
	private Date expireDate;
		
	
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

	public String getGit() {
		return git;
	}

	public void setGit(String git) {
		this.git = git;
	}

	public String getDuNo() {
		return duNo;
	}

	public void setDuNo(String duNo) {
		this.duNo = duNo;
	}

	public String getPediduNo() {
		return pediduNo;
	}

	public void setPediduNo(String pediduNo) {
		this.pediduNo = pediduNo;
	}

	public Date getIssueDate() {
		return issueDate;
	}

	public void setIssueDate(Date issueDate) {
		this.issueDate = issueDate;
	}

	public Date getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
	}


	
	
	
}
