package com.sesnu.orion.web.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name ="currency")
public class Currency {
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="curr_seq")
	@SequenceGenerator(name="curr_seq",sequenceName="curr_seq",allocationSize=20)
	
	@Column(name="id")
	private Long id;
	
	@Column(name="country")
	private String country;
	
	@Column(name="currency")
	private String currency;
	
	@Column(name="abrevation")
	private String abrevation;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getAbrevation() {
		return abrevation;
	}

	public void setAbrevation(String abrevation) {
		this.abrevation = abrevation;
	}
	
	
	
	
	

}
