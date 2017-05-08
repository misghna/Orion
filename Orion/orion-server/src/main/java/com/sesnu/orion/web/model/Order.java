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
@Table(name = "ORDERS")
public class Order {
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="order_seq")
	@SequenceGenerator(name="order_seq",sequenceName="order_seq",allocationSize=20)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "item_id")
	private Long itemId;
	
	@Column(name = "item")
	private String item;
	
	@Column(name = "brand")
	private String brand;
		
	@Column(name = "budget_ref")
	private String budgetRef;
	
	@Column(name = "base_size")
	private Integer baseSize;
	
	@Column(name = "base_unit")
	private String baseUnit;
	
	@Column(name = "qty_per_pck")
	private Integer qtyPerPack;
	
	@Column(name = "pck_per_cont")
	private Integer pckPerCont;
	
	@Column(name = "cont_size")
	private Integer contSize;
	
	@Column(name = "cont_qnt")
	private Integer contQnt;
	
	@Column(name = "latest_eta")
	private Date latestETA;
	
	@Column(name = "importer")
	private String importer;
	
	@Column(name = "destination_port")
	private String destinationPort;
	
	@Column(name = "ordered_by")
	private String orderedBy;
	
	@Column(name = "inv_no")
	private String invNo;
	
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
	
	@Column(name="source")
	private String source;
	
	@Column(name="origin")
	private String origin;
	
	@Column(name="exporter")
	private String exporter;
	
	@Column(name="currency")
	private String currency;
	
	@Column(name = "created_on")
	private Date createdOn;
	
	@Column(name = "updated_on")
	private String updatedOn;
	
	public Order(){}
	
	public Order(SalesView sp) {
		this.setItemId(sp.getItemId());
		this.setItem(sp.getName());
		this.setBaseSize(sp.getBaseSize());
		this.setBaseUnit(sp.getBaseUnit());
		this.setBrand(sp.getBrand());
		this.setContQnt(sp.getContQnt());
		this.setContSize(sp.getContSize());
		this.setDestinationPort(sp.getDestinationPort());
		this.setQtyPerPack(sp.getQtyPerPack());
		this.setPckPerCont(sp.getPckPerCont());
		this.setId(0l);
		this.setCurrency(sp.getCurrency());
		this.setTotalPrice(sp.getPckPerCont()*sp.getContQnt()*sp.getCif());
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getBudgetRef() {
		return budgetRef;
	}

	public void setBudgetRef(String budgetRef) {
		this.budgetRef = budgetRef;
	}

	public Integer getBaseSize() {
		return baseSize;
	}

	public void setBaseSize(Integer baseSize) {
		this.baseSize = baseSize;
	}

	public String getBaseUnit() {
		return baseUnit;
	}

	public void setBaseUnit(String baseUnit) {
		this.baseUnit = baseUnit;
	}

	public Integer getQtyPerPack() {
		return qtyPerPack;
	}

	public void setQtyPerPack(Integer qtyPerPack) {
		this.qtyPerPack = qtyPerPack;
	}

	public Integer getPckPerCont() {
		return pckPerCont;
	}

	public void setPckPerCont(Integer pckPerCont) {
		this.pckPerCont = pckPerCont;
	}

	public Integer getContSize() {
		return contSize;
	}

	public void setContSize(Integer contSize) {
		this.contSize = contSize;
	}

	public Integer getContQnt() {
		return contQnt;
	}

	public void setContQnt(Integer contQnt) {
		this.contQnt = contQnt;
	}

	public Date getLatestETA() {
		return latestETA;
	}

	public void setLatestETA(Date latestETA) {
		this.latestETA = latestETA;
	}

	public String getImporter() {
		return importer;
	}

	public void setImporter(String importer) {
		this.importer = importer;
	}

	public String getDestinationPort() {
		return destinationPort;
	}

	public void setDestinationPort(String destinationPort) {
		this.destinationPort = destinationPort;
	}

	public String getOrderedBy() {
		return orderedBy;
	}

	public void setOrderedBy(String orderedBy) {
		this.orderedBy = orderedBy;
	}

	public String getInvNo() {
		return invNo;
	}

	public void setInvNo(String invNo) {
		this.invNo = invNo;
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

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public String getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdatedOn(String updatedOn) {
		this.updatedOn = updatedOn;
	}

	public String getExporter() {
		return exporter;
	}

	public void setExporter(String exporter) {
		this.exporter = exporter;
	}


	

	
	
	
	
	
}
