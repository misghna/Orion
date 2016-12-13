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
@Table(name = "BID")
public class Bid {
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="item_seq")
	@SequenceGenerator(name="item_seq",sequenceName="item_seq",allocationSize=20)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "supplier")
	private String supplier;
	
	@Column(name = "fob")
	private double fob;
	
	@Column(name = "cif_cnf")
	private double cifCnf;
	
	@Column(name="total_bid")
	private double totalBid;
	
	@Column(name="order_ref")
	private long orderRef;
	
	@Column(name="payment_method")
	private String paymentMethod;
	
//	@Column(name="payment_terms")
//	private String paymentTerms;
//	
//	@Column(name = "est_transit_days")
//	private Integer estTransitDays;
//	
//	@Column(name = "latest_ship_date")
//	private Date latestShipDate;
	
	@Column(name = "updated_on")
	private String updatedOn;
	
	@Column(name = "first_approver")
	private String firstApprover;
	
	@Column(name = "second_approver")
	private String secondApprover;
	
	@Column(name = "approval")
	private String approval;
	
	@Column(name = "remark")
	private String remark;
	
	@Column(name = "df")
	private boolean df;
	
	@Column(name = "selected")
	private boolean selected;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSupplier() {
		return supplier;
	}

	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}

	public double getFob() {
		return fob;
	}

	public void setFob(double fob) {
		this.fob = fob;
	}

	public double getCifCnf() {
		return cifCnf;
	}

	public void setCifCnf(double cifCnf) {
		this.cifCnf = cifCnf;
	}

	public double getTotalBid() {
		return totalBid;
	}

	public void setTotalBid(double totalBid) {
		this.totalBid = totalBid;
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

//	public String getPaymentTerms() {
//		return paymentTerms;
//	}
//
//	public void setPaymentTerms(String paymentTerms) {
//		this.paymentTerms = paymentTerms;
//	}
//
//	public Integer getEstTransitDays() {
//		return estTransitDays;
//	}
//
//	public void setEstTransitDays(Integer estTransitDays) {
//		this.estTransitDays = estTransitDays;
//	}
//
//	public Date getLatestShipDate() {
//		return latestShipDate;
//	}
//
//	public void setLatestShipDate(Date latestShipDate) {
//		this.latestShipDate = latestShipDate;
//	}

	public String getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdatedOn(String updatedOn) {
		this.updatedOn = updatedOn;
	}

	public String getFirstApprover() {
		return firstApprover;
	}

	public void setFirstApprover(String firstApprover) {
		this.firstApprover = firstApprover;
	}

	public String getApproval() {
		return approval;
	}

	public void setApproval(String approval) {
		this.approval = approval;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public boolean isDf() {
		return df;
	}

	public void setDf(boolean df) {
		this.df = df;
	}

	public String getSecondApprover() {
		return secondApprover;
	}

	public void setSecondApprover(String secondApprover) {
		this.secondApprover = secondApprover;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}


	
	
	
	
	
	
}
