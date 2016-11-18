package com.sesnu.orion.web.model;

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
	
	@Column(name = "product")
	private String product;
	
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

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}


	



	
}
