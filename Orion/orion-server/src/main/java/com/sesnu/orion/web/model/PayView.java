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
@Table(name = "PAY_VIEW")
public class PayView {
	
	@Id
	@Column(name = "id")
	private Long id;
	
	@Column(name = "order_ref")
	private long orderRef;

	@Column(name = "payment_method")
	private String paymentMethod;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "cont_id")
	private String contId;
	
	@Column(name = "transaction_id")
	private String transactionId;
	
	@Column(name = "payment_date")
	private Date paymentDate;
	
	@Column(name = "payment_amount")
	private double paymentAmount;
	
	@Column(name = "estimate")
	private double estimate;
	
	@Column(name = "deposit")
	private double deposit;
	
	@Column(name = "updated_on")
	private String updatedOn;
	
	@Column(name = "status")
	private String status;
	
	@Column(name = "inv_no")
	private String InvNo;
	
	@Column(name = "bl")
	private String bl;
	
	@Column(name = "remark")
	private String remark;
	
	@Column(name = "df")
	private boolean df;
	
	@Column(name = "currency")
	private String curr;

	
	
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

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public Date getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(Date paymentDate) {
		this.paymentDate = paymentDate;
	}

	public double getPaymentAmount() {
		return paymentAmount;
	}

	public void setPaymentAmount(double paymentAmount) {
		this.paymentAmount = paymentAmount;
	}

	public String getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdatedOn(String updatedOn) {
		this.updatedOn = updatedOn;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getInvNo() {
		return InvNo;
	}

	public void setInvNo(String invNo) {
		InvNo = invNo;
	}

	public String getBl() {
		return bl;
	}

	public void setBl(String bl) {
		this.bl = bl;
	}

	public String getCurr() {
		return curr;
	}

	public void setCurr(String curr) {
		this.curr = curr;
	}

	public double getEstimate() {
		return estimate;
	}

	public void setEstimate(double estimate) {
		this.estimate = estimate;
	}

	public double getDeposit() {
		return deposit;
	}

	public void setDeposit(double deposit) {
		this.deposit = deposit;
	}

	public String getContId() {
		return contId;
	}

	public void setContId(String contId) {
		this.contId = contId;
	}


	
	

	


	
	
}
