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
public class PortFee {
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="item_seq")
	@SequenceGenerator(name="item_seq",sequenceName="item_seq",allocationSize=20)
	@Column(name = "id")
	private long id;

	@Column(name = "name")
	private String name;
	
	@Column(name = "freight_rate")
	private double freightRate;
	
	@Column(name = "cont_lift_rate20")
	private double contLiftRate20;
	
	@Column(name = "cont_lift_rate40")
	private double contLiftRate40;
	
	@Column(name="deposit_cont20")	
	private double depositCont20;
	
	@Column(name="deposit_cont40")	
	private double depositCont40;
	
	@Column(name="consumer_tax")
	private double consumerTax;
	
	@Column(name="cont_free_days")
	private int contFreeDays;
	
	@Column(name = "daily_penality")
	private double dailyPenality;
	
	@Column(name = "exchange_rate")
	private double exchangeRate;
	
	@Column(name = "updated_on")
	private double updatedOn;
	
	@Column(name = "revision")
	private Date revision;

	
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getFreightRate() {
		return freightRate;
	}

	public void setFreightRate(double freightRate) {
		this.freightRate = freightRate;
	}

	public double getContLiftRate20() {
		return contLiftRate20;
	}

	public void setContLiftRate20(double contLiftRate20) {
		this.contLiftRate20 = contLiftRate20;
	}

	public double getContLiftRate40() {
		return contLiftRate40;
	}

	public void setContLiftRate40(double contLiftRate40) {
		this.contLiftRate40 = contLiftRate40;
	}

	public double getDepositCont20() {
		return depositCont20;
	}

	public void setDepositCont20(double depositCont20) {
		this.depositCont20 = depositCont20;
	}

	public double getDepositCont40() {
		return depositCont40;
	}

	public void setDepositCont40(double depositCont40) {
		this.depositCont40 = depositCont40;
	}

	public double getConsumerTax() {
		return consumerTax;
	}

	public void setConsumerTax(double consumerTax) {
		this.consumerTax = consumerTax;
	}

	public int getContFreeDays() {
		return contFreeDays;
	}

	public void setContFreeDays(int contFreeDays) {
		this.contFreeDays = contFreeDays;
	}

	public double getDailyPenality() {
		return dailyPenality;
	}

	public void setDailyPenality(double dailyPenality) {
		this.dailyPenality = dailyPenality;
	}

	public double getExchangeRate() {
		return exchangeRate;
	}

	public void setExchangeRate(double exchangeRate) {
		this.exchangeRate = exchangeRate;
	}

	public double getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdatedOn(double updatedOn) {
		this.updatedOn = updatedOn;
	}

	public Date getRevision() {
		return revision;
	}

	public void setRevision(Date revision) {
		this.revision = revision;
	}
	
	
	

	
}
