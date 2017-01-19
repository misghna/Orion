package com.sesnu.orion.web.model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "DESTINATION")
public class Client {
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="dest_seq")
	@SequenceGenerator(name="dest_seq",sequenceName="dest_seq",allocationSize=20)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "name")
	private String name;
		
	@Column(name="address")
	private String address;

	@Column(name="phone")
	private String phone;
	
	@Column(name="city")
	private String city;
	
	@Column(name="manager")
	private String manager;

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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getManager() {
		return manager;
	}

	public void setManager(String manager) {
		this.manager = manager;
	}
	
	
}
