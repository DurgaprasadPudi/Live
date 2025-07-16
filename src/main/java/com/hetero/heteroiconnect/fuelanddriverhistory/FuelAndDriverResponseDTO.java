package com.hetero.heteroiconnect.fuelanddriverhistory;

public class FuelAndDriverResponseDTO {
	private Integer fuelDriverId;
	private String employeeSequenceNo;
	private String payperiod;
	private String employeeName;
	private String deptName;
	private String desgName;
	private String effectiveDate;
	private String fuelAndMaintenance;
	private String driverSalary;
	private String totalAmount;
	private String claimedAmount;
	private String billFlag;
	private String comments;
	private String processedFlag;
	private String receivedDate;
	private String processedDate;

	public FuelAndDriverResponseDTO() {
		super();
	}

	public Integer getFuelDriverId() {
		return fuelDriverId;
	}

	public void setFuelDriverId(Integer fuelDriverId) {
		this.fuelDriverId = fuelDriverId;
	}

	public String getEmployeeSequenceNo() {
		return employeeSequenceNo;
	}

	public void setEmployeeSequenceNo(String employeeSequenceNo) {
		this.employeeSequenceNo = employeeSequenceNo;
	}

	public String getPayperiod() {
		return payperiod;
	}

	public void setPayperiod(String payperiod) {
		this.payperiod = payperiod;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getDesgName() {
		return desgName;
	}

	public void setDesgName(String desgName) {
		this.desgName = desgName;
	}

	public String getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(String effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	public String getFuelAndMaintenance() {
		return fuelAndMaintenance;
	}

	public void setFuelAndMaintenance(String fuelAndMaintenance) {
		this.fuelAndMaintenance = fuelAndMaintenance;
	}

	public String getDriverSalary() {
		return driverSalary;
	}

	public void setDriverSalary(String driverSalary) {
		this.driverSalary = driverSalary;
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

	public String getBillFlag() {
		return billFlag;
	}

	public void setBillFlag(String billFlag) {
		this.billFlag = billFlag;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getProcessedFlag() {
		return processedFlag;
	}

	public void setProcessedFlag(String processedFlag) {
		this.processedFlag = processedFlag;
	}

	public String getReceivedDate() {
		return receivedDate;
	}

	public void setReceivedDate(String receivedDate) {
		this.receivedDate = receivedDate;
	}

	public String getProcessedDate() {
		return processedDate;
	}

	public void setProcessedDate(String processedDate) {
		this.processedDate = processedDate;
	}

	public FuelAndDriverResponseDTO(Integer fuelDriverId, String employeeSequenceNo, String payperiod,
			String employeeName, String deptName, String desgName, String effectiveDate, String fuelAndMaintenance,
			String driverSalary, String totalAmount, String claimedAmount, String billFlag, String comments,
			String processedFlag, String receivedDate, String processedDate) {
		super();
		this.fuelDriverId = fuelDriverId;
		this.employeeSequenceNo = employeeSequenceNo;
		this.payperiod = payperiod;
		this.employeeName = employeeName;
		this.deptName = deptName;
		this.desgName = desgName;
		this.effectiveDate = effectiveDate;
		this.fuelAndMaintenance = fuelAndMaintenance;
		this.driverSalary = driverSalary;
		this.totalAmount = totalAmount;
		this.claimedAmount = claimedAmount;
		this.billFlag = billFlag;
		this.comments = comments;
		this.processedFlag = processedFlag;
		this.receivedDate = receivedDate;
		this.processedDate = processedDate;
	}

}
