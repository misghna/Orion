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
@Table(name = "ITEMS")
public class Item {
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="item_seq")
	@SequenceGenerator(name="item_seq",sequenceName="item_seq",allocationSize=20)
	@Column(name = "id")
	private long id;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "brand")
	private String brand;
	
	@Column(name = "hscode")
	private long hsCode;
	
	@Column(name="financial_services")
	private double financialServices;
	
	@Column(name="consumer_tax")
	private double consumerTax;
	
	@Column(name="stamp_tax")
	private double stampTax;
	
	@Column(name = "fees")
	private double fees;
	
	@Column(name = "others")
	private double others;
	
	@Column(name = "updated_on")
	private String updatedOn;
	
	@Column(name = "revision")
	private Date revision;
	
	@Column(name = "type")
	private String type;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getHsCode() {
		return hsCode;
	}

	public void setHsCode(long hsCode) {
		this.hsCode = hsCode;
	}

	public double getFinancialServices() {
		return financialServices;
	}

	public void setFinancialServices(double financialServices) {
		this.financialServices = financialServices;
	}

	public double getConsumerTax() {
		return consumerTax;
	}

	public void setConsumerTax(double consumerTax) {
		this.consumerTax = consumerTax;
	}

	public double getStampTax() {
		return stampTax;
	}

	public void setStampTax(double stampTax) {
		this.stampTax = stampTax;
	}

	public double getFees() {
		return fees;
	}

	public void setFees(double fees) {
		this.fees = fees;
	}

	public double getOthers() {
		return others;
	}

	public void setOthers(double others) {
		this.others = others;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
	public String getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdatedOn(String updatedOn) {
		this.updatedOn = updatedOn;
	}
	
	

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public Date getRevision() {
		return revision;
	}

	public void setRevision(Date revision) {
		this.revision = revision;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}



	
	
}
