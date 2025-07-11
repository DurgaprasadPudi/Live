package com.hetero.heteroiconnect.fuelanddriverhistory;

public class FuelDriverHistoryInsertDTO {
	private String employeeId;
	private String payPeriod;
	private String totalAmount;
	private String claimedAmount;
	private Integer billFlag;
	private String comment;
	private String createdBy;

	public FuelDriverHistoryInsertDTO() {
		super();
	}

	public FuelDriverHistoryInsertDTO(String employeeId, String payPeriod, String totalAmount, String claimedAmount,
			Integer billFlag, String comment, String createdBy) {
		super();
		this.employeeId = employeeId;
		this.payPeriod = payPeriod;
		this.totalAmount = totalAmount;
		this.claimedAmount = claimedAmount;
		this.billFlag = billFlag;
		this.comment = comment;
		this.createdBy = createdBy;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public String getPayPeriod() {
		return payPeriod;
	}

	public void setPayPeriod(String payPeriod) {
		this.payPeriod = payPeriod;
	}

	public String getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getClaimedAmount() {
		return claimedAmount;
	}

	public void setClaimedAmount(String claimedAmount) {
		this.claimedAmount = claimedAmount;
	}

	public Integer getBillFlag() {
		return billFlag;
	}

	public void setBillFlag(Integer billFlag) {
		this.billFlag = billFlag;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

}