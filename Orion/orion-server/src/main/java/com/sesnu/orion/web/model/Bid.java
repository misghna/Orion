package com.sesnu.orion.web.model;


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
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="bid_seq")
	@SequenceGenerator(name="bid_seq",sequenceName="bid_seq",allocationSize=20)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "supplier")
	private String supplier;
	
	@Column(name = "fob")
	private Double fob;
	
	@Column(name = "cif_cnf")
	private double cifCnf;
	
	@Column(name="total_bid")
	private Double totalBid;
	
	@Column(name="order_ref")
	private long orderRef;
	
	@Column(name="payment_method")
	private String paymentMethod;

	@Column(name="currency")
	private String currency;
	
	@Column(name="item_origin")
	private String itemOrigin;
	
	@Column(name="proforma_inv_no")
	private String proformaInvNo;
	
	@Column(name = "updated_on")
	private String updatedOn;
	
	@Column(name = "est_transit_days")
	private Integer estTransitDays;
	
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

	public Double getFob() {
		return fob;
	}

	public void setFob(Double fob) {
		this.fob = fob;
	}

	public double getCifCnf() {
		return cifCnf;
	}

	public void setCifCnf(double cifCnf) {
		this.cifCnf = cifCnf;
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


	public String getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdatedOn(String updatedOn) {
		this.updatedOn = updatedOn;
	}


	public Integer getEstTransitDays() {
		return estTransitDays;
	}

	public void setEstTransitDays(Integer estTransitDays) {
		this.estTransitDays = estTransitDays;
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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getProformaInvNo() {
		return proformaInvNo;
	}

	public void setProformaInvNo(String proformaInvNo) {
		this.proformaInvNo = proformaInvNo;
	}

	public Double getTotalBid() {
		return totalBid;
	}

	public void setTotalBid(Double totalBid) {
		this.totalBid = totalBid;
	}

	public String getItemOrigin() {
		return itemOrigin;
	}

	public void setItemOrigin(String itemOrigin) {
		this.itemOrigin = itemOrigin;
	}
	
	
	
}
