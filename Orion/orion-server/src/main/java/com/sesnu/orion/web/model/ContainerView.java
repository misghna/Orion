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
@Table(name = "CONTAINER_VIEW")
public class ContainerView {
	
	@Id
	@Column(name = "id")
	private Long id;
	
	@Column(name = "order_ref")
	private long orderRef;
	
	@Column(name = "bl")
	private String bl;
	
	@Column(name = "cont_size")
	private Integer contSize;
	
	@Column(name = "cont_no")
	private String contNo;
	
	@Column(name = "transporter")
	private String transporter;
	
	@Column(name = "destination")
	private String destination;
	
	@Column(name = "cont_return_date")
	private Date contReturnDate;;
	
	@Column(name = "remark")
	private String remark;
	
	@Column(name = "updated_on")
	private String updatedOn;
	
	
	@Column(name = "df")
	private boolean df;

	
	public ContainerView(){}
	
	
	
//	public ContainerView(long orderRef, String path, String type, String updatedOn, String remark) {
//		this.orderRef = orderRef;
//		this.updatedOn = updatedOn;
//		this.remark = remark;
//	}

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



	public Integer getContSize() {
		return contSize;
	}



	public void setContSize(Integer contSize) {
		this.contSize = contSize;
	}



	public String getContNo() {
		return contNo;
	}



	public void setContNo(String contNo) {
		this.contNo = contNo;
	}


	public String getBl() {
		return bl;
	}



	public void setBl(String bl) {
		this.bl = bl;
	}



	public String getTransporter() {
		return transporter;
	}



	public void setTransporter(String transporter) {
		this.transporter = transporter;
	}



	public String getDestination() {
		return destination;
	}



	public void setDestination(String destination) {
		this.destination = destination;
	}



	public Date getContReturnDate() {
		return contReturnDate;
	}



	public void setContReturnDate(Date contReturnDate) {
		this.contReturnDate = contReturnDate;
	}


	
	
}
