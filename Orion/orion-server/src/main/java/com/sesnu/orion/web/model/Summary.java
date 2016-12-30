package com.sesnu.orion.web.model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "SUMMARY_VIEW")
public class Summary {
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="order_seq")
	@SequenceGenerator(name="order_seq",sequenceName="order_seq",allocationSize=20)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "item")
	private String item;
	
	@Column(name = "brand")
	private String brand;
	
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
	
	@Column(name = "importer")
	private String importer;
	
	@Column(name = "destination_port")
	private String destinationPort;
	
	@Column(name = "ordered_by")
	private String orderedBy;
	
	@Column(name = "inv_no")
	private String invNo;
	
	@Column(name = "bl")
	private String bl;
	
	@Column(name = "item_origin")
	private String itemOrigin;

	@Column(name = "etd")
	private String etd;
	
	@Column(name = "eta")
	private String eta;
	
	@Column(name = "ata")
	private String ata;
	
	@Column(name = "ship_agency")
	private String ShippingAgency;
	
	@Column(name = "free_days")
	private String freeDaysLeft;
	
	@Column(name = "updated_on")
	private String updatedOn;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getBl() {
		return bl;
	}

	public void setBl(String bl) {
		this.bl = bl;
	}

	public String getItemOrigin() {
		return itemOrigin;
	}

	public void setItemOrigin(String itemOrigin) {
		this.itemOrigin = itemOrigin;
	}

	public String getEtd() {
		return etd;
	}

	public void setEtd(String etd) {
		this.etd = etd;
	}

	public String getEta() {
		return eta;
	}

	public void setEta(String eta) {
		this.eta = eta;
	}

	public String getAta() {
		return ata;
	}

	public void setAta(String ata) {
		this.ata = ata;
	}

	public String getShippingAgency() {
		return ShippingAgency;
	}

	public void setShippingAgency(String shippingAgency) {
		ShippingAgency = shippingAgency;
	}

	public String getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdatedOn(String updatedOn) {
		this.updatedOn = updatedOn;
	}

	public String getFreeDaysLeft() {
		return freeDaysLeft;
	}

	public void setFreeDaysLeft(String freeDaysLeft) {
		this.freeDaysLeft = freeDaysLeft;
	}

	
	
}
