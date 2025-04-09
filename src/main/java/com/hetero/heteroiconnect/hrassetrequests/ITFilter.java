package com.hetero.heteroiconnect.hrassetrequests;

import java.time.LocalDate;

public class ITFilter {
	private String loginId;
	private int bu;
	private int department;
	private String assetType;
	private LocalDate tentativeFromDate;
	private LocalDate tentativeToDate;
	private Integer pageSize;
	private Integer pageNo;

	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
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

}
