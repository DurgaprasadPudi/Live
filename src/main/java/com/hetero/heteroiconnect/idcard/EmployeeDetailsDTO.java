package com.hetero.heteroiconnect.idcard;

import java.util.Date;

public class EmployeeDetailsDTO {
	private Long employeeId;
	private String name;
	private String buName;
	private Date dateOfJoin;
	private String costCenter;
	private String code;
	private Boolean designationCheck;
	private Boolean departmentCheck;
	private String department;
	private String designation;
	private String addressId;
	

	private String bloodGroupId;
	private String imagePath;
	private byte[] imageBytes;
	private String companyName;
	private String address;
	private String bloodGroupName;
	private String BackgroundImagename;

	public byte[] getImageBytes() {
		return imageBytes;
	}

	public void setImageBytes(byte[] imageBytes) {
		this.imageBytes = imageBytes;
	}

	// Getters and Setters
	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
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

	public String getBloodGroupName() {
		return bloodGroupName;
	}

	public void setBloodGroupName(String bloodGroupName) {
		this.bloodGroupName = bloodGroupName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBuName() {
		return buName;
	}

	public void setBuName(String buName) {
		this.buName = buName;
	}

	public Date getDateOfJoin() {
		return dateOfJoin;
	}

	public void setDateOfJoin(Date dateOfJoin) {
		this.dateOfJoin = dateOfJoin;
	}

	public String getCostCenter() {
		return costCenter;
	}

	public void setCostCenter(String costCenter) {
		this.costCenter = costCenter;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Boolean getDesignationCheck() {
		return designationCheck;
	}

	public void setDesignationCheck(Boolean designationCheck) {
		this.designationCheck = designationCheck;
	}

	public Boolean getDepartmentCheck() {
		return departmentCheck;
	}

	public void setDepartmentCheck(Boolean departmentCheck) {
		this.departmentCheck = departmentCheck;
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

	public String getAddressId() {
		return addressId;
	}

	public void setAddressId(String addressId) {
		this.addressId = addressId;
	}

	public String getBloodGroupId() {
		return bloodGroupId;
	}

	public void setBloodGroupId(String bloodGroupId) {
		this.bloodGroupId = bloodGroupId;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
	
	public String getBackgroundImagename() {
		return BackgroundImagename;
	}

	public void setBackgroundImagename(String backgroundImagename) {
		BackgroundImagename = backgroundImagename;
	}
}
