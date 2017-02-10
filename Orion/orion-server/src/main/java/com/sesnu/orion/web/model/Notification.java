package com.sesnu.orion.web.model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "NOTIFICATION")
public class Notification {
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="notif_seq")
	@SequenceGenerator(name="notif_seq",sequenceName="notif_seq",allocationSize=20)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "type")
	private String type;

	
	@Column(name="order_ref")
	private long orderRef;
	
//	@Column(name = "bl")
//	private String bl;
//	
//	@Column(name = "inv_now")
//	private String invNo;
	
	
	@Column(name = "notified")
	private boolean notified;


	public Notification(){}

	public Notification(String name, String type, long orderRef) {
		this.name = name;
		this.type = type;
		this.orderRef = orderRef;
//		this.bl = bl;
//		this.invNo = invNo;
	}



	public Long getId() {
		return id;
	}



	public void setId(Long id) {
		this.id = id;
	}



	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
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



	public boolean isNotified() {
		return notified;
	}



	public void setNotified(boolean notified) {
		this.notified = notified;
	}



//	public String getBl() {
//		return bl;
//	}
//
//
//
//	public void setBl(String bl) {
//		this.bl = bl;
//	}
//
//
//
//	public String getInvNo() {
//		return invNo;
//	}
//
//
//
//	public void setInvNo(String invNo) {
//		this.invNo = invNo;
//	}
	
	
	
}
