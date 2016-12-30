package com.sesnu.orion.web.model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "PORT_FEES")
public class PortFee {
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="item_seq")
	@SequenceGenerator(name="item_seq",sequenceName="item_seq",allocationSize=20)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "agency")
	private String agency;
	
	@Column(name = "legalization_fee")
	private double legalizationFee;
	
	@Column(name = "cont_lift_fee")
	private double contLiftFee;
	
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
	private String updatedOn;
	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public double getLegalizationFee() {
		return legalizationFee;
	}

	public void setLegalizationFee(double legalizationFee) {
		this.legalizationFee = legalizationFee;
	}

	public double getContLiftFee() {
		return contLiftFee;
	}

	public void setContLiftFee(double contListFee) {
		this.contLiftFee = contListFee;
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

	public String getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdatedOn(String updatedOn) {
		this.updatedOn = updatedOn;
	}

	public String getAgency() {
		return agency;
	}

	public void setAgency(String agency) {
		this.agency = agency;
	}


	
}
