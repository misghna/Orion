package com.sesnu.orion.web.model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "TERMINAL")
public class Terminal {
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="term_seq")
	@SequenceGenerator(name="term_seq",sequenceName="term_seq",allocationSize=20)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "offload_fee_20ft")
	private double offloadFee20ft;
	
	@Column(name = "offload_fee_40ft")
	private double offloadFee40ft;
	
	@Column(name = "admin_service_charge")
	private double adminServiceCharge;
	
	@Column(name="other_percent")
	private double otherPercent;
	
	@Column(name="free_days")
	private Integer freeDays;		
	
	@Column(name="storage_first_range_days")
	private Integer storageFirstRangeDays;
	
	@Column(name="storage_first_range_fee_20ft")
	private double storageFirstRangeFee20ft;
	
	@Column(name="storage_first_range_fee_40ft")
	private double storageFirstRangeFee40ft;
	
	@Column(name="storage_second_range_days")
	private Integer storageSecondRangeDays;
	
	@Column(name="storage_second_range_fee_20ft")
	private double storageSecondRangeFee20ft;
	
	@Column(name="storage_second_range_fee_40ft")
	private double storageSecondRangeFee40ft;
	
	@Column(name = "transport")
	private double transport;
	
	@Column(name = "import_tarrif")
	private double importTarrif;

	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getOffloadFee20ft() {
		return offloadFee20ft;
	}

	public void setOffloadFee20ft(double offloadFee20ft) {
		this.offloadFee20ft = offloadFee20ft;
	}

	public double getOffloadFee40ft() {
		return offloadFee40ft;
	}

	public void setOffloadFee40ft(double offloadFee40ft) {
		this.offloadFee40ft = offloadFee40ft;
	}

	public double getAdminServiceCharge() {
		return adminServiceCharge;
	}

	public void setAdminServiceCharge(double adminServiceCharge) {
		this.adminServiceCharge = adminServiceCharge;
	}

	public double getOtherPercent() {
		return otherPercent;
	}

	public void setOtherPercent(double otherPercent) {
		this.otherPercent = otherPercent;
	}

	public Integer getFreeDays() {
		return freeDays;
	}

	public void setFreeDays(Integer freeDays) {
		this.freeDays = freeDays;
	}

	public Integer getStorageFirstRangeDays() {
		return storageFirstRangeDays;
	}

	public void setStorageFirstRangeDays(Integer storageFirstRangeDays) {
		this.storageFirstRangeDays = storageFirstRangeDays;
	}

	public double getStorageFirstRangeFee20ft() {
		return storageFirstRangeFee20ft;
	}

	public void setStorageFirstRangeFee20ft(double storageFirstRangeFee20ft) {
		this.storageFirstRangeFee20ft = storageFirstRangeFee20ft;
	}

	public double getStorageFirstRangeFee40ft() {
		return storageFirstRangeFee40ft;
	}

	public void setStorageFirstRangeFee40ft(double storageFirstRangeFee40ft) {
		this.storageFirstRangeFee40ft = storageFirstRangeFee40ft;
	}

	public Integer getStorageSecondRangeDays() {
		return storageSecondRangeDays;
	}

	public void setStorageSecondRangeDays(Integer storageSecondRangeDays) {
		this.storageSecondRangeDays = storageSecondRangeDays;
	}


	public double getStorageSecondRangeFee20ft() {
		return storageSecondRangeFee20ft;
	}

	public void setStorageSecondRangeFee20ft(double storageSecondRangeFee20ft) {
		this.storageSecondRangeFee20ft = storageSecondRangeFee20ft;
	}

	public double getStorageSecondRangeFee40ft() {
		return storageSecondRangeFee40ft;
	}

	public void setStorageSecondRangeFee40ft(double storageSecondRangeFee40ft) {
		this.storageSecondRangeFee40ft = storageSecondRangeFee40ft;
	}

	public double getTransport() {
		return transport;
	}

	public void setTransport(double transport) {
		this.transport = transport;
	}

	public double getImportTarrif() {
		return importTarrif;
	}

	public void setImportTarrif(double importTarrif) {
		this.importTarrif = importTarrif;
	}

	
}
