package com.hetero.heteroiconnect.worksheet.model;

import java.util.List;

public class EmployeeSummaryRequest {
	private Integer id;
	private String fromDate;
	private String toDate;
	private List<Integer> managerIds;
	private Integer employeeId;
	private int pageSize;
	private int pageNo;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public String getToDate() {
		return toDate;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

	public List<Integer> getManagerIds() {
		return managerIds;
	}

	public void setManagerIds(List<Integer> managerIds) {
		this.managerIds = managerIds;
	}

	public Integer getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Integer employeeId) {
		this.employeeId = employeeId;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

}