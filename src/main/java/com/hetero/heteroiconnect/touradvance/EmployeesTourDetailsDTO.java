package com.hetero.heteroiconnect.touradvance;

import java.math.BigDecimal;
import java.time.LocalDate;

public class EmployeesTourDetailsDTO {

	private Integer tourId;
	private Integer employeeId;
	private String employeeName;
	private String department;
	private String designation;
	private String modeOfTravel; 
	private String purposeOfTravel;
	private String travelFrom;
	private String travelTo;
	private LocalDate dateOfTravel;
	private LocalDate dateOfReturn;
	private BigDecimal requestedAmount;
	private String currentLocation;
	private String contactNo;
	private String email;
	private String bankAccountNo;
	private String bankName;
	private String ifscCode;
	private String cashRecipientName;
	private String chequeNumber;
	private Integer hodEmpid;
	private String advanceBillFile;
	private Double utilizedAmount;
	private Double adjustment;
	private String cashMode;
	private int CashModeid;
	private BigDecimal fareAmount;
	private String tourType;
	private String comment;
	private String uploadedBy;
	private String createdBy;
	private String modeOfBooking;
	private String modeOfBookingName;
	private String adharNo;
	private BigDecimal serviceCharge;
	private String billStatus;

	private int booking_id;
	// Getters and Setters

	public Integer getTourId() {
		return tourId;
	}

	public void setTourId(Integer tourId) {
		this.tourId = tourId;
	}

	public Integer getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Integer employeeId) {
		this.employeeId = employeeId;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public String getModeOfTravel() {
		return modeOfTravel;
	}

	public void setModeOfTravel(String modeOfTravel) {
		this.modeOfTravel = modeOfTravel;
	}

	public String getPurposeOfTravel() {
		return purposeOfTravel;
	}

	public void setPurposeOfTravel(String purposeOfTravel) {
		this.purposeOfTravel = purposeOfTravel;
	}

	public String getTravelFrom() {
		return travelFrom;
	}

	public void setTravelFrom(String travelFrom) {
		this.travelFrom = travelFrom;
	}

	public String getTravelTo() {
		return travelTo;
	}

	public void setTravelTo(String travelTo) {
		this.travelTo = travelTo;
	}

	public LocalDate getDateOfTravel() {
		return dateOfTravel;
	}

	public void setDateOfTravel(LocalDate dateOfTravel) {
		this.dateOfTravel = dateOfTravel;
	}

	public LocalDate getDateOfReturn() {
		return dateOfReturn;
	}

	public void setDateOfReturn(LocalDate dateOfReturn) {
		this.dateOfReturn = dateOfReturn;
	}

	public BigDecimal getRequestedAmount() {
		return requestedAmount;
	}

	public void setRequestedAmount(BigDecimal requestedAmount) {
		this.requestedAmount = requestedAmount;
	}

	public String getCurrentLocation() {
		return currentLocation;
	}

	public void setCurrentLocation(String currentLocation) {
		this.currentLocation = currentLocation;
	}

	public String getContactNo() {
		return contactNo;
	}

	public void setContactNo(String contactNo) {
		this.contactNo = contactNo;
	}

	public BigDecimal getFareAmount() {
		return fareAmount;
	}

	public void setFareAmount(BigDecimal fareAmount) {
		this.fareAmount = fareAmount;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getBankAccountNo() {
		return bankAccountNo;
	}

	public void setBankAccountNo(String bankAccountNo) {
		this.bankAccountNo = bankAccountNo;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getIfscCode() {
		return ifscCode;
	}

	public void setIfscCode(String ifscCode) {
		this.ifscCode = ifscCode;
	}

	public String getCashRecipientName() {
		return cashRecipientName;
	}

	public void setCashRecipientName(String cashRecipientName) {
		this.cashRecipientName = cashRecipientName;
	}

	public Integer getHodEmpid() {
		return hodEmpid;
	}

	public void setHodEmpid(Integer hodEmpid) {
		this.hodEmpid = hodEmpid;
	}

	public Double getUtilizedAmount() {
		return utilizedAmount;
	}

	public void setUtilizedAmount(Double utilizedAmount) {
		this.utilizedAmount = utilizedAmount;
	}

	public Double getAdjustment() {
		return adjustment;
	}

	public void setAdjustment(Double adjustment) {
		this.adjustment = adjustment;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getUploadedBy() {
		return uploadedBy;
	}

	public void setUploadedBy(String uploadedBy) {
		this.uploadedBy = uploadedBy;
	}

	public String getAdvanceBillFile() {
		return advanceBillFile;
	}

	public void setAdvanceBillFile(String advanceBillFile) {
		this.advanceBillFile = advanceBillFile;
	}

	public String getCashMode() {
		return cashMode;
	}

	public void setCashMode(String cashMode) {
		this.cashMode = cashMode;
	}

	public int getCashModeid() {
		return CashModeid;
	}

	public void setCashModeid(int cashModeid) {
		CashModeid = cashModeid;
	}

	public String getTourType() {
		return tourType;
	}

	public void setTourType(String tourType) {
		this.tourType = tourType;
	}
	



	public String getChequeNumber() {
		return chequeNumber;
	}

	public void setChequeNumber(String chequeNumber) {
		this.chequeNumber = chequeNumber;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	

	public String getModeOfBooking() {
		return modeOfBooking;
	}

	public void setModeOfBooking(String modeOfBooking) {
		this.modeOfBooking = modeOfBooking;
	}

	public String getModeOfBookingName() {
		return modeOfBookingName;
	}

	public void setModeOfBookingName(String modeOfBookingName) {
		this.modeOfBookingName = modeOfBookingName;
	}



	public String getAdharNo() {
		return adharNo;
	}

	public void setAdharNo(String adharNo) {
		this.adharNo = adharNo;
	}

	
	public int getBooking_id() {
		return booking_id;
	}

	public void setBooking_id(int booking_id) {
		this.booking_id = booking_id;
	}

	public BigDecimal getServiceCharge() {
		return serviceCharge;
	}

	public void setServiceCharge(BigDecimal serviceCharge) {
		this.serviceCharge = serviceCharge;
	}

	public String getBillStatus() {
		return billStatus;
	}

	public void setBillStatus(String billStatus) {
		this.billStatus = billStatus;
	}

	

}