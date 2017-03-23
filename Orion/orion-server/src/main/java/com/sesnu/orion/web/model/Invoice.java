package com.sesnu.orion.web.model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "INVOICE")
public class Invoice {
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="inv_seq")
	@SequenceGenerator(name="inv_seq",sequenceName="inv_seq",allocationSize=20)
	@Column(name = "id")
	private long id;
	
	@Column(name = "inv_no")
	private String invNo;

	@Column(name = "invoice_type")
	private String invoiceType;
	
	@Column(name = "invoice")
	private String invoice;
	
	@Column(name = "updated_on")
	private String updatedOn;

	public Invoice(){}
	
	public Invoice(String invNo, String invoiceType, String invoice, String updatedOn) {
		this.invNo = invNo;
		this.invoiceType = invoiceType;
		this.invoice = invoice;
		this.updatedOn = updatedOn;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	

	public String getInvNo() {
		return invNo;
	}

	public void setInvNo(String invNo) {
		this.invNo = invNo;
	}

	public String getInvoiceType() {
		return invoiceType;
	}

	public void setInvoiceType(String invoiceType) {
		this.invoiceType = invoiceType;
	}

	public String getInvoice() {
		return invoice;
	}

	public void setInvoice(String invoice) {
		this.invoice = invoice;
	}

	public String getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdatedOn(String updatedOn) {
		this.updatedOn = updatedOn;
	}

	
	
	
}
