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
@Table(name = "SHIPPING")
public class Shipping {
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="ship_seq")
	@SequenceGenerator(name="ship_seq",sequenceName="ship_seq",allocationSize=20)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "order_ref")
	private long orderRef;

	@Column(name = "bl")
	private String bl;
	
	@Column(name = "ship_agency")
	private String shipAgency;
	
	@Column(name = "item_origin")
	private String itemOrigin;
	
	@Column(name = "loading_port")
	private String loadingPort;
	
	@Column(name = "terminal")
	private String terminal;
	
	@Column(name = "etd")
	private Date etd;
	
	@Column(name = "eta")
	private Date eta;
	
	@Column(name = "ata")
	private Date ata;
	
	@Column(name = "updated_on")
	private String updatedOn;
	
	@Column(name = "remark")
	private String remark;
	
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

	public String getBl() {
		return bl;
	}

	public void setBl(String bl) {
		this.bl = bl;
	}

	public String getShipAgency() {
		return shipAgency;
	}

	public void setShipAgency(String shipAgency) {
		this.shipAgency = shipAgency;
	}

	public String getItemOrigin() {
		return itemOrigin;
	}

	public void setItemOrigin(String itemOrigin) {
		this.itemOrigin = itemOrigin;
	}

	public Date getEtd() {
		return etd;
	}

	public void setEtd(Date etd) {
		this.etd = etd;
	}

	public Date getEta() {
		return eta;
	}

	public void setEta(Date eta) {
		this.eta = eta;
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

	public String getTerminal() {
		return terminal;
	}

	public void setTerminal(String terminal) {
		this.terminal = terminal;
	}

	public Date getAta() {
		return ata;
	}

	public void setAta(Date ata) {
		this.ata = ata;
	}

	public String getLoadingPort() {
		return loadingPort;
	}

	public void setLoadingPort(String loadingPort) {
		this.loadingPort = loadingPort;
	}
	
	


	
	
}
