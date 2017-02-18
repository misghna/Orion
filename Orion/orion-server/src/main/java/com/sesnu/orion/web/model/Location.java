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
@Table(name = "LOCATIONS")
public class Location {
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="loc_seq")
	@SequenceGenerator(name="loc_seq",sequenceName="loc_seq",allocationSize=20)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "ip")
	private String ip;
	
	@Column(name="lat")
	private String lat;
	
	@Column(name="lng")
	private String lng;

	@Column(name="updated_on")
	private Date updatedOn;

	@Column(name="trials")
	private Integer trials;
	
	
	
	public Location(){}

	
	public Location(String ip, String lat, String lng, Date updatedOn, Integer trials) {
		this.ip = ip;
		this.lat = lat;
		this.lng = lng;
		this.updatedOn = updatedOn;
		this.trials = trials;
	}


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getLng() {
		return lng;
	}

	public void setLng(String lng) {
		this.lng = lng;
	}

	public Date getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}

	public Integer getTrials() {
		return trials;
	}

	public void setTrials(Integer trials) {
		this.trials = trials;
	}
	
	

	
}
