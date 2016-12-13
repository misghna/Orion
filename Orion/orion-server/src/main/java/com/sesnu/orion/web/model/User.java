package com.sesnu.orion.web.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "USERS")
public class User {
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="user_seq")
	@SequenceGenerator(name="user_seq",sequenceName="user_seq",allocationSize=20)
	private long id;
	private String email;
	private String phone;
	private String passphrase;
	private String role;
	private String fullname;
	private String status;
	private String department;
	private boolean approver;
	
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}



	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassphrase() {
		return passphrase;
	}

	public void setPassphrase(String passphrase) {
		this.passphrase = passphrase;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public boolean getApprover() {
		return approver;
	}

	public void setApprover(boolean approver) {
		this.approver = approver;
	}




	
}
