package com.sesnu.orion.web.model;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.NamedNativeQuery;

@Entity
@Table(name = "ORDER_VIEW")
public class OrderView {
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="order_seq")
	@SequenceGenerator(name="order_seq",sequenceName="order_seq",allocationSize=20)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "item_id")
	private long itemId;
	
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
	private int contSize;
	
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
	
	@Column(name = "bl")
	private String bl;
	
	@Column(name = "terminal")
	private String terminal;
	
	@Column(name = "type")
	private String itemType;
	
	@Column(name = "created_on")
	private Date createdOn;
	
	@Column(name = "updated_on")
	private String updatedOn;

	public OrderView() {}
	



	public OrderView(SalesView sp) {
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
		this.setItemType(sp.getItemType());
		this.setId(0l);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getItem() {
		return item;
	}

	public void setItem(String name) {
		this.item = name;
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

	public int getContSize() {
		return contSize;
	}

	public void setContSize(int contSize) {
		this.contSize = contSize;
	}

	public Integer getContQnt() {
		return contQnt;
	}

	public void setContQnt(Integer contQnt) {
		this.contQnt = contQnt;
	}


	public String getDestinationPort() {
		return destinationPort;
	}

	public void setDestinationPort(String destinationPort) {
		this.destinationPort = destinationPort;
	}

	public String getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdatedOn(String updatedOn) {
		this.updatedOn = updatedOn;
	}

	public Date getLatestETA() {
		return latestETA;
	}

	public void setLatestETA(Date latestETA) {
		this.latestETA = latestETA;
	}

	public String getOrderedBy() {
		return orderedBy;
	}

	public void setOrderedBy(String orderedBy) {
		this.orderedBy = orderedBy;
	}


	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public String getInvNo() {
		return invNo;
	}

	public void setInvNo(String invNo) {
		this.invNo = invNo;
	}

	public String getImporter() {
		return importer;
	}

	public void setImporter(String importer) {
		this.importer = importer;
	}

	public String getBl() {
		return bl;
	}

	public void setBl(String bl) {
		this.bl = bl;
	}

	public String getItemType() {
		return itemType;
	}

	public void setItemType(String itemType) {
		this.itemType = itemType;
	}

	public long getItemId() {
		return itemId;
	}

	public void setItemId(long itemId) {
		this.itemId = itemId;
	}

	public String getTerminal() {
		return terminal;
	}

	public void setTerminal(String terminal) {
		this.terminal = terminal;
	}

	
	

	
	
	
	
	
}
