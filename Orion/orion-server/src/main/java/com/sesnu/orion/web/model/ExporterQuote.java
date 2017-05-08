package com.sesnu.orion.web.model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "EXPORTER_QUOTE")
public class ExporterQuote {
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="expmrg_seq")
	@SequenceGenerator(name="expmrg_seq",sequenceName="expmrg_seq",allocationSize=20)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "inv_no")
	private String invNo;
	
	@Column(name="descr")
	private String descr;
	
	@Column(name="source")
	private String source;
	
	@Column(name="total_packs")
	private String totalPacks;
	
	@Column(name = "fob")
	private Double fob;
	
	@Column(name = "fob_margin")
	private Double fobMargin;
	
	@Column(name = "adj_fob")
	private Double adjFob;
	
	@Column(name = "freight")
	private Double freight;
	
	@Column(name = "freight_margin")
	private Double freightMargin;
	
	@Column(name = "adj_freight")
	private Double adjFreight;
	
	@Column(name = "unit_price")
	private Double unitPrice;
	
	@Column(name = "total_price")
	private Double totalPrice;
	
	@Column(name="status")
	private String status;
	
	@Column(name = "updated_on")
	private String updatedOn;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getInvNo() {
		return invNo;
	}

	public void setInvNo(String invNo) {
		this.invNo = invNo;
	}

	public String getDescr() {
		return descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getTotalPacks() {
		return totalPacks;
	}

	public void setTotalPacks(String totalPacks) {
		this.totalPacks = totalPacks;
	}

	public Double getFob() {
		return fob;
	}

	public void setFob(Double fob) {
		this.fob = fob;
	}

	public Double getFobMargin() {
		return fobMargin;
	}

	public void setFobMargin(Double fobMargin) {
		this.fobMargin = fobMargin;
	}

	public Double getAdjFob() {
		return adjFob;
	}

	public void setAdjFob(Double adjFob) {
		this.adjFob = adjFob;
	}

	public Double getFreight() {
		return freight;
	}

	public void setFreight(Double freight) {
		this.freight = freight;
	}

	public Double getFreightMargin() {
		return freightMargin;
	}

	public void setFreightMargin(Double freightMargin) {
		this.freightMargin = freightMargin;
	}

	public Double getAdjFreight() {
		return adjFreight;
	}

	public void setAdjFreight(Double adjFreight) {
		this.adjFreight = adjFreight;
	}

	public Double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}

	public Double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(Double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdatedOn(String updatedOn) {
		this.updatedOn = updatedOn;
	}




	
}
