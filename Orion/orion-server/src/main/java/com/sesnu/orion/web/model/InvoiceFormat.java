package com.sesnu.orion.web.model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "INVOICE_FORMAT")
public class InvoiceFormat {
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="invfmt_seq")
	@SequenceGenerator(name="invfmt_seq",sequenceName="invfmt_seq",allocationSize=20)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "exporter")
	private String exporter;
	
	@Column(name = "invoice_type")
	private String invoiceType;
	
	@Column(name = "format")
	private String format;

	@Column(name = "updated_on")
	private String updatedOn;
	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getExporter() {
		return exporter;
	}

	public void setExporter(String exporter) {
		this.exporter = exporter;
	}

	public String getInvoiceType() {
		return invoiceType;
	}

	public void setInvoiceType(String invoiceType) {
		this.invoiceType = invoiceType;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdatedOn(String updatedOn) {
		this.updatedOn = updatedOn;
	}
	
	
	
	
}
