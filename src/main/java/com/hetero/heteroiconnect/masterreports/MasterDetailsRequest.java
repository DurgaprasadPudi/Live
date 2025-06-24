package com.hetero.heteroiconnect.masterreports;

import java.util.List;

public class MasterDetailsRequest {
	private String location;
	private List<String> bu;
	private List<String> status;

	public MasterDetailsRequest() {
	}

	public MasterDetailsRequest(String location, List<String> bu, List<String> status) {
		super();
		this.location = location;
		this.bu = bu;
		this.status = status;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public List<String> getBu() {
		return bu;
	}

	public void setBu(List<String> bu) {
		this.bu = bu;
	}

	public List<String> getStatus() {
		return status;
	}

	public void setStatus(List<String> status) {
		this.status = status;
	}

}