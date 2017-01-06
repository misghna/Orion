package com.sesnu.orion.web.model;


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
	
	@Column(name = "type")
	private String type;
		
	@Column(name = "bl")
	private String bl;

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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getBl() {
		return bl;
	}

	public void setBl(String bl) {
		this.bl = bl;
	}
	
	
	
}
