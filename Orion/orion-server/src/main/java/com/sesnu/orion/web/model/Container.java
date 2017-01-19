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
	
	@Column(name = "transporter")
	private String transporter;
	
	@Column(name = "destination")
	private String destination;
	
	@Column(name = "receive_voucher_no")
	private String recvVoucherNo;
	
	
	@Column(name = "cont_return_date")
	private Date contReturnDate;
	
	@Column(name = "total_days")
	private Integer totalDays;
	
	@Column(name = "offload_date")
	private Date offloadDate;
	
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

	public Integer getTotalDays() {
		return totalDays;
	}

	public void setTotalDays(Integer totalDays) {
		this.totalDays = totalDays;
	}

	public Date getOffloadDate() {
		return offloadDate;
	}

	public void setOffloadDate(Date offloadDate) {
		this.offloadDate = offloadDate;
	}

	public String getRecvVoucherNo() {
		return recvVoucherNo;
	}

	public void setRecvVoucherNo(String recvVoucherNo) {
		this.recvVoucherNo = recvVoucherNo;
	}
	
	
	
	
	
	
}
