package com.sesnu.orion.web.model;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "SHIPPING")
public class Budget {
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="item_seq")
	@SequenceGenerator(name="item_seq",sequenceName="item_seq",allocationSize=20)
	@Column(name = "id")
	private long id;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "brand")
	private double brand;
	
	@Column(name = "hs_code")
	private long hsCode;
	
	@Column(name = "size")
	private double size;
	
	@Column(name = "unit")
	private double unit;
	
	@Column(name="packing")	
	private double packing;
	
	@Column(name="quantity")	
	private double quantity;
	
	@Column(name="cif")
	private double cif;
	
	@Column(name = "updated_on")
	private double updatedOn;
	
	@Column(name = "revision")
	private Date revision;

	
	
	
}
