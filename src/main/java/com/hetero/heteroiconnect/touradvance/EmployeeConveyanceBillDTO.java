package com.hetero.heteroiconnect.touradvance;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class EmployeeConveyanceBillDTO {

    private Integer conveyanceId;  
    private String employeeId;
    private String name;
    private String division;
    private String department;
    private String designation;
    private String depthod;
    private String contactNo;
    private String email;
    private String location;
    private String adharNo;
    private String amountTransfrType;
    private BigDecimal billAmount;
    private String billSubmittedDate;
    private String nameOfBank;
    private String bankAccountNo;
    private String bankIfscNo;
    private String chequeNo;
    private String bankBranch;
     
    private String description;
    private Integer prevBillStatus;
//    private String createdBy;
    private LocalDateTime createdDate;
    private String updatedBy;
    private LocalDateTime lupdated;

    // --- Getters & Setters ---
  
    
    public String getEmployeeId() {
		return employeeId;
	}
	
	public Integer getConveyanceId() {
		return conveyanceId;
	}

	public void setConveyanceId(Integer conveyanceId) {
		this.conveyanceId = conveyanceId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}
	public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
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
    
    public String getDepthod() {
		return depthod;
	}
	public void setDepthod(String depthod) {
		this.depthod = depthod;
	}
	public String getContactNo() {
        return contactNo;
    }
    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }
    public String getAdharNo() {
        return adharNo;
    }
    public void setAdharNo(String adharNo) {
        this.adharNo = adharNo;
    }
    public String getAmountTransfrType() {
        return amountTransfrType;
    }
    public void setAmountTransfrType(String amountTransfrType) {
        this.amountTransfrType = amountTransfrType;
    }
    public BigDecimal getBillAmount() {
        return billAmount;
    }
    public void setBillAmount(BigDecimal billAmount) {
        this.billAmount = billAmount;
    }
    public String getBillSubmittedDate() {
        return billSubmittedDate;
    }
    public void setBillSubmittedDate(String billSubmittedDate) {
        this.billSubmittedDate = billSubmittedDate;
    }
    public String getNameOfBank() {
        return nameOfBank;
    }
    public void setNameOfBank(String nameOfBank) {
        this.nameOfBank = nameOfBank;
    }
    public String getBankAccountNo() {
        return bankAccountNo;
    }
    public void setBankAccountNo(String bankAccountNo) {
        this.bankAccountNo = bankAccountNo;
    }
    public String getBankIfscNo() {
        return bankIfscNo;
    }
    public void setBankIfscNo(String bankIfscNo) {
        this.bankIfscNo = bankIfscNo;
    }
    
    public String getChequeNo() {
		return chequeNo;
	}
	public void setChequeNo(String chequeNo) {
		this.chequeNo = chequeNo;
	}
	public String getBankBranch() {
        return bankBranch;
    }
    public void setBankBranch(String bankBranch) {
        this.bankBranch = bankBranch;
    }
  
	public String getDivision() {
		return division;
	}
	public void setDivision(String division) {
		this.division = division;
	}
	public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public Integer getPrevBillStatus() {
        return prevBillStatus;
    }
    public void setPrevBillStatus(Integer prevBillStatus) {
        this.prevBillStatus = prevBillStatus;
    }
//    public String getCreatedBy() {
//        return createdBy;
//    }
//    public void setCreatedBy(String createdBy) {
//        this.createdBy = createdBy;
//    }
    public LocalDateTime getCreatedDate() {
        return createdDate;
    }
    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }
    public String getUpdatedBy() {
        return updatedBy;
    }
    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }
    public LocalDateTime getLupdated() {
        return lupdated;
    }
    public void setLupdated(LocalDateTime lupdated) {
        this.lupdated = lupdated;
    }
}
