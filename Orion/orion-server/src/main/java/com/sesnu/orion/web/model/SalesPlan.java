package com.sesnu.orion.web.model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "SALES_PLAN")
public class SalesPlan {
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="sales_seq")
	@SequenceGenerator(name="sales_seq",sequenceName="sales_seq",allocationSize=20)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "item_id")
	private long itemId;
	
	@Column(name = "base_size")
	private int baseSize;
	
	@Column(name = "base_unit")
	private String baseUnit;
	
	@Column(name = "qty_per_pck")
	private int qtyPerPack;
	
	@Column(name = "pck_per_cont")
	private int pckPerCont;
	
	@Column(name = "cont_size")
	private int contSize;
	
	@Column(name = "cont_qnt")
	private int contQnt;
	
	@Column(name = "cif")
	private double cif;
	
	@Column(name = "item_origin")
	private String itemOrigin;
	
	@Column(name = "destination_port")
	private String destinationPort;
	
	@Column(name = "month")
	private String month;
	
	@Column(name = "mon")
	private int mon;
	
	@Column(name = "year")
	private int year;
	
	@Column(name = "updated_on")
	private String updatedOn;
	
	public SalesPlan(){}
	
	public SalesPlan(SalesView sv){
		this.id = sv.getId();
		this.itemId = sv.getItemId();
		this.baseSize = sv.getBaseSize();
		this.baseUnit=sv.getBaseUnit();
		this.qtyPerPack=sv.getQtyPerPack();
		this.pckPerCont=sv.getPckPerCont();
		this.contSize = sv.getContSize();
		this.contQnt = sv.getContQnt();
		this.cif=sv.getCif();
		this.itemOrigin=sv.getItemOrigin();
		this.destinationPort=sv.getDestinationPort();
		this.month = sv.getMonth();
		this.year=sv.getYear();
		this.updatedOn=sv.getUpdatedOn();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public int getPckPerCont() {
		return pckPerCont;
	}

	public void setPckPerCont(int pckPerCont) {
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

	public String getItemOrigin() {
		return itemOrigin;
	}

	public void setItemOrigin(String itemOrigin) {
		this.itemOrigin = itemOrigin;
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
	
	
	
	
}
