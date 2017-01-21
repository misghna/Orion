package com.sesnu.orion.web.model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "BUDGET")
public class Budget {
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="bgt_seq")
	@SequenceGenerator(name="bgt_seq",sequenceName="bgt_seq",allocationSize=20)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "item_id")
	private long itemId;
	
	@Column(name = "base_size")
	private int baseSize;

	@Column(name = "product")
	private String product;
	
	@Column(name = "brand")
	private String brand;
	
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
		
	@Column(name = "total_cif")
	private double totalCif;
	
	@Column(name = "shipping_agency")
	private double shippingAgency;
	
	@Column(name = "customs")
	private double customs;
	
	@Column(name = "bromangol")
	private double bromangol;
	
	@Column(name = "local_phytosanitary")
	private double localPhytosanitary;
	
	@Column(name = "cert_health")
	private double certHealth;
	
	@Column(name = "port")
	private double port;
	
	@Column(name = "terminal")
	private double terminal;
	
	@Column(name = "license")
	private double license;
	
	@Column(name = "transport")
	private double transport;
	
	@Column(name = "cert_quality")
	private double certQuality;
	
	@Column(name = "forward_agency")
	private double forwardAgency;
	
	@Column(name = "total_fees")
	private double totalFees;
	
	@Column(name = "total_cost")
	private double totalCost;
	
	@Column(name = "cost_pack")
	private double costPerPack;
	
	@Column(name = "price_at_12")
	private double priceAt12;
	
	@Column(name = "price_at_12_Usd")
	private double priceAt12usd;
	

		
	
	public Budget(){}
	
	public Budget(SalesView sv){
		this.id = sv.getId();
		this.itemId = sv.getItemId();
		this.baseSize = sv.getBaseSize();
		this.baseUnit=sv.getBaseUnit();
		this.qtyPerPack=sv.getQtyPerPack();
		this.pckPerCont=sv.getPckPerCont();
		this.contSize = sv.getContSize();
		this.contQnt = sv.getContQnt();
		this.cif=sv.getCif();
		this.destinationPort=sv.getDestinationPort();
		this.month = sv.getMonth();
		this.mon = sv.getMon();
		this.year=sv.getYear();
		this.product=sv.getName();
		this.brand=sv.getBrand();
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

	public int getMon() {
		return mon;
	}

	public void setMon(int mon) {
		this.mon = mon;
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

	public double getShippingAgency() {
		return shippingAgency;
	}

	public void setShippingAgency(double shippingAgency) {
		this.shippingAgency = shippingAgency;
	}

	public double getTotalCif() {
		return totalCif;
	}

	public void setTotalCif(double totalCif) {
		this.totalCif = totalCif;
	}

	public double getCustoms() {
		return customs;
	}

	public void setCustoms(double customs) {
		this.customs = customs;
	}

	public double getBromangol() {
		return bromangol;
	}

	public void setBromangol(double bromangol) {
		this.bromangol = bromangol;
	}

	public double getLocalPhytosanitary() {
		return localPhytosanitary;
	}

	public void setLocalPhytosanitary(double localPhytosanitary) {
		this.localPhytosanitary = localPhytosanitary;
	}

	public double getCertHealth() {
		return certHealth;
	}

	public void setCertHealth(double certHealth) {
		this.certHealth = certHealth;
	}

	public double getPort() {
		return port;
	}

	public void setPort(double port) {
		this.port = port;
	}

	public double getTerminal() {
		return terminal;
	}

	public void setTerminal(double terminal) {
		this.terminal = terminal;
	}

	public double getLicense() {
		return license;
	}

	public void setLicense(double license) {
		this.license = license;
	}

	public double getTransport() {
		return transport;
	}

	public void setTransport(double transport) {
		this.transport = transport;
	}

	public double getCertQuality() {
		return certQuality;
	}

	public void setCertQuality(double certQuality) {
		this.certQuality = certQuality;
	}

	public double getForwardAgency() {
		return forwardAgency;
	}

	public void setForwardAgency(double forwardAgency) {
		this.forwardAgency = forwardAgency;
	}

	public double getTotalFees() {
		return totalFees;
	}

	public void setTotalFees(double totalFees) {
		this.totalFees = totalFees;
	}

	public double getTotalCost() {
		return totalCost;
	}

	public void setTotalCost(double totalCost) {
		this.totalCost = totalCost;
	}

	public double getCostPerPack() {
		return costPerPack;
	}

	public void setCostPerPack(double costPerPack) {
		this.costPerPack = costPerPack;
	}

	public double getPriceAt12() {
		return priceAt12;
	}

	public void setPriceAt12(double priceAt12) {
		this.priceAt12 = priceAt12;
	}

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public double getPriceAt12usd() {
		return priceAt12usd;
	}

	public void setPriceAt12usd(double priceAt12usd) {
		this.priceAt12usd = priceAt12usd;
	}

	
	
	
	
}
