package com.hetero.heteroiconnect.attendancereports;

import java.time.LocalDate;
import java.util.List;

public class AttendanceFilterPojo {
	private Integer location;
	private List<Integer> bu;
	private Integer empId;
	private boolean isDataBetween;
	private boolean isPayPeriod;
	private LocalDate fromDate;
	private LocalDate toDate;
	private Integer year;
	private Integer month;
	private String callName;

	public Integer getLocation() {
		return location;
	}

	public void setLocation(Integer location) {
		this.location = location;
	}

	public List<Integer> getBu() {
		return bu;
	}

	public void setBu(List<Integer> bu) {
		this.bu = bu;
	}

	public Integer getEmpId() {
		return empId;
	}

	public void setEmpId(Integer empId) {
		this.empId = empId;
	}

	public boolean getIsDataBetween() {
		return isDataBetween;
	}

	public void setIsDataBetween(boolean isDataBetween) {
		this.isDataBetween = isDataBetween;
	}

	public boolean getIsPayPeriod() {
		return isPayPeriod;
	}

	public void setIsPayPeriod(boolean isPayPeriod) {
		this.isPayPeriod = isPayPeriod;
	}

	public LocalDate getFromDate() {
		return fromDate;
	}

	public void setFromdate(LocalDate fromDate) {
		this.fromDate = fromDate;
	}

	public LocalDate getToDate() {
		return toDate;
	}

	public void setTodate(LocalDate toDate) {
		this.toDate = toDate;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public Integer getMonth() {
		return month;
	}

	public void setMonth(Integer month) {
		this.month = month;
	}

	public String getCallName() {
		return callName;
	}

	public void setCallName(String callName) {
		this.callName = callName;
	}

	@Override
	public String toString() {
		return "AttendanceFilterPojo [location=" + location + ", bu=" + bu + ", empId=" + empId + ", isDataBetween="
				+ isDataBetween + ", isPayPeriod=" + isPayPeriod + ", fromDate=" + fromDate + ", toDate=" + toDate
				+ ", year=" + year + ", month=" + month + ", callName=" + callName + "]";
	}
}
