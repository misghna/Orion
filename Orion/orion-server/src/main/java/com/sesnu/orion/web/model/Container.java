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
@Table(name = "CONTAINER")
public class Container {
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="cont_seq")
	@SequenceGenerator(name="cont_seq",sequenceName="cont_seq",allocationSize=20)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "order_ref")
	private long orderRef;
	
	@Column(name = "cont_size")
	private Integer contSize;
	
	@Column(name = "cont_no")
	private String contNo;
	
	@Column(name = "remark")
	private String remark;
	
	@Column(name = "updated_on")
	private String updatedOn;
	
	@Column(name = "df")
	private boolean df;

	
	
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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdatedOn(String updatedOn) {
		this.updatedOn = updatedOn;
	}

	public boolean isDf() {
		return df;
	}

	public void setDf(boolean df) {
		this.df = df;
	}
	
	
	
	
	
	
}
