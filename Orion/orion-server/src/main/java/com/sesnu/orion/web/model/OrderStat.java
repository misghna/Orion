package com.sesnu.orion.web.model;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "order_stat")
public class OrderStat {
	
	@Id
	@Column(name = "no")
	private Long id;
	
	@Column(name = "orderef")
	private Long orderRef;
	
	@Column(name = "bid")
	private Long bid;
		
	@Column(name = "bl")
	private String bl;
	
	@Column(name = "bid_approval")
	private String bidApproval;
	
	@Column(name = "margin_stat")
	private String marginStat;
	
	@Column(name = "ata")
	private Date ata;
	
	@Column(name = "cont_qnt")
	private Integer contQnt;
	
	@Column(name = "returned_count")
	private Integer contReturnedCount;
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getOrderRef() {
		return orderRef;
	}

	public void setOrderRef(Long orderRef) {
		this.orderRef = orderRef;
	}

	public Long getBid() {
		return bid;
	}

	public void setBid(Long bid) {
		this.bid = bid;
	}

	public String getBl() {
		return bl;
	}

	public void setBl(String bl) {
		this.bl = bl;
	}

	public String getBidApproval() {
		return bidApproval;
	}

	public void setBidApproval(String bidApproval) {
		this.bidApproval = bidApproval;
	}

	public String getMarginStat() {
		return marginStat;
	}

	public void setMarginStat(String marginStat) {
		this.marginStat = marginStat;
	}

	public Date getAta() {
		return ata;
	}

	public void setAta(Date ata) {
		this.ata = ata;
	}

	public Integer getContReturnedCount() {
		return contReturnedCount;
	}

	public void setContReturnedCount(Integer contReturnedCount) {
		this.contReturnedCount = contReturnedCount;
	}

	public Integer getContQnt() {
		return contQnt;
	}

	public void setContQnt(Integer contQnt) {
		this.contQnt = contQnt;
	}
	
	
	
	
}
