package com.hetero.heteroiconnect.hrassetrequests;

import java.time.LocalDate;

public class HrFilter {
	private int loginId;
	private int bu;
	private int department;
	//private String reportingManager;
	private String assetType;
	private LocalDate tentativeFromDate;
	private LocalDate tentativeToDate;
	private Integer pageSize;
	private Integer pageNo;
	private int status;

	public int getLoginId() {
		return loginId;
	}

	public void setLoginId(int loginId) {
		this.loginId = loginId;
	}

	public int getBu() {
		return bu;
	}

	public void setBu(int bu) {
		this.bu = bu;
	}

	public int getDepartment() {
		return department;
	}

	public void setDepartment(int department) {
		this.department = department;
	}

	/*
	 * public String getReportingManager() { return reportingManager; }
	 * 
	 * public void setReportingManager(String reportingManager) {
	 * this.reportingManager = reportingManager; }
	 */

	public String getAssetType() {
		return assetType;
	}

	public void setAssetType(String assetType) {
		this.assetType = assetType;
	}

	public LocalDate getTentativeFromDate() {
		return tentativeFromDate;
	}

	public void setTentativeFromDate(LocalDate tentativeFromDate) {
		this.tentativeFromDate = tentativeFromDate;
	}

	public LocalDate getTentativeToDate() {
		return tentativeToDate;
	}

	public void setTentativeToDate(LocalDate tentativeToDate) {
		this.tentativeToDate = tentativeToDate;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getPageNo() {
		return pageNo;
	}

	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}
