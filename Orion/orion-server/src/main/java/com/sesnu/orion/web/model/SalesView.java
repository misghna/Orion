package com.sesnu.orion.web.model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.NamedNativeQuery;

@Entity
@Table(name = "SALES_PLAN_VIEW")
public class SalesView {
	
	@Id
	private long id;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "brand")
	private String brand;
		
	@Column(name = "item_id")
	private long itemId;
	
	@Column(name = "base_size")
	private int baseSize;
	
	@Column(name = "base_unit")
	private String baseUnit;
	
	@Column(name = "qty_per_pck")
	private int qtyPerPack;
	
	@Column(name = "pck_per_cont")
	private Double pckPerCont;
	
	@Column(name = "cont_size")
	private int contSize;
	
	@Column(name = "cont_qnt")
	private int contQnt;

	@Column(name = "currency")
	private String currency;
	
	@Column(name = "cif")
	private double cif;
	
	@Column(name = "destination_port")
	private String destinationPort;
	
	@Column(name = "type")
	private String itemType;
	
	@Column(name = "month")
	private String month;
	
	@Column(name = "mon")
	private int mon;
	
	@Column(name = "year")
	private int year;
	
	@Column(name = "updated_on")
	private String updatedOn;

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

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public long getItemId() {
		return itemId;
	}

	public void setItemId(long itemId) {
		this.itemId = itemId;
	}

	public int getBaseSize() {
		return baseSize;
	}

	public void setBaseSize(int baseSize) {
		this.baseSize = baseSize;
	}

	public String getBaseUnit() {
		return baseUnit;
	}

	public void setBaseUnit(String baseUnit) {
		this.baseUnit = baseUnit;
	}

	public int getQtyPerPack() {
		return qtyPerPack;
	}

	public void setQtyPerPack(int qtyPerPack) {
		this.qtyPerPack = qtyPerPack;
	}

	public Double getPckPerCont() {
		return pckPerCont;
	}

	public void setPckPerCont(Double pckPerCont) {
		this.pckPerCont = pckPerCont;
	}

	public int getContSize() {
		return contSize;
	}

	public void setContSize(int contSize) {
		this.contSize = contSize;
	}

	public int getContQnt() {
		return contQnt;
	}

	public void setContQnt(int contQnt) {
		this.contQnt = contQnt;
	}

	public double getCif() {
		return cif;
	}

	public void setCif(double cif) {
		this.cif = cif;
	}


	public String getDestinationPort() {
		return destinationPort;
	}

	public void setDestinationPort(String destinationPort) {
		this.destinationPort = destinationPort;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public String getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdatedOn(String updatedOn) {
		this.updatedOn = updatedOn;
	}

	public int getMon() {
		return mon;
	}

	public void setMon(int mon) {
		this.mon = mon;
	}

	public String getItemType() {
		return itemType;
	}

	public void setItemType(String itemType) {
		this.itemType = itemType;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}
	
	
	
	
	
}
