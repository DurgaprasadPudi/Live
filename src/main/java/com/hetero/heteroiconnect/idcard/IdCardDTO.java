package com.hetero.heteroiconnect.idcard;

import java.sql.Timestamp;

public class IdCardDTO {
	private Long id;
	private String companyName;
	private String address;
	private Timestamp lastUpdated;
	private int status;

	// Constructor
	public IdCardDTO(Long id, String companyName, String address, Timestamp lastUpdated, int status) {
		this.id = id;
		this.companyName = companyName;
		this.address = address;
		this.lastUpdated = lastUpdated;
		this.status = status;
	}

	// Getters and Setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Timestamp getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Timestamp lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
}
