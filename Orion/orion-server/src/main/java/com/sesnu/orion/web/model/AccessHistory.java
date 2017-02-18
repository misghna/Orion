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
@Table(name = "ACCESS_HISTORY")
public class AccessHistory {
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="access_seq")
	@SequenceGenerator(name="access_seq",sequenceName="access_seq",allocationSize=20)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "user_id")
	private Long userId;
	
	@Column(name = "ip")
	private String ip;
	
	@Column(name = "mac_add")
	private String macAdd;

	@Column(name="access_time")
	private Date accessTime;

	@Column(name="status")
	private String status;
	
	public AccessHistory(){}
	
	
	public AccessHistory( Long userId, String ip,String status) {

		this.userId = userId;
		this.ip = ip;
		this.status = status;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getMacAdd() {
		return macAdd;
	}

	public void setMacAdd(String macAdd) {
		this.macAdd = macAdd;
	}

	public Date getAccessTime() {
		return accessTime;
	}

	public void setAccessTime(Date accessTime) {
		this.accessTime = accessTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	


	
	
}
