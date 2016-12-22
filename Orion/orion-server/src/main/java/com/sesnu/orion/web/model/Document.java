package com.sesnu.orion.web.model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "DOC")
public class Document {
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="doc_seq")
	@SequenceGenerator(name="doc_seq",sequenceName="doc_seq",allocationSize=20)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "order_ref")
	private long orderRef;

	@Column(name = "path")
	private String path;
	
	@Column(name = "type")
	private String type;
	
	@Column(name = "updated_on")
	private String updatedOn;
	
	@Column(name = "remark")
	private String remark;
	
	@Column(name = "df")
	private boolean df;

	
	public Document(){}
	
	
	
	public Document(long orderRef, String path, String type, String updatedOn, String remark) {
		this.orderRef = orderRef;
		this.path = path;
		this.type = type;
		this.updatedOn = updatedOn;
		this.remark = remark;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public long getOrderRef() {
		return orderRef;
	}

	public void setOrderRef(long orderRef) {
		this.orderRef = orderRef;
	}


	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdatedOn(String updatedOn) {
		this.updatedOn = updatedOn;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public boolean isDf() {
		return df;
	}

	public void setDf(boolean df) {
		this.df = df;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	
}
