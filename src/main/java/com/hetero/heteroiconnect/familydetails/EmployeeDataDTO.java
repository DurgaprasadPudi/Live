package com.hetero.heteroiconnect.familydetails;

public class EmployeeDataDTO {
	private String empCode;
	private String pan;
	private String aadhar;
	private String mobile;
	private String whatsappNumber;
	private String maritalStatus;
	// private String filePath;

	private byte[] filePath; // Changed to byte[]

	private String originalFilePath;

	public String getOriginalFilePath() {
		return originalFilePath;
	}

	public String setOriginalFilePath(String originalFilePath) {
		return this.originalFilePath = originalFilePath;
	}

	// Getters and Setters
	public byte[] getFilePath() {
		return filePath;
	}

	public void setFilePath(byte[] filePath) {
		this.filePath = filePath;
	}

	public String getEmpCode() {
		return empCode;
	}

	public void setEmpCode(String empCode) {
		this.empCode = empCode;
	}

	public String getPan() {
		return pan;
	}

	public void setPan(String pan) {
		this.pan = pan;
	}

	public String getAadhar() {
		return aadhar;
	}

	public void setAadhar(String aadhar) {
		this.aadhar = aadhar;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getWhatsappNumber() {
		return whatsappNumber;
	}

	public void setWhatsappNumber(String whatsappNumber) {
		this.whatsappNumber = whatsappNumber;
	}

	public String getMaritalStatus() {
		return maritalStatus;
	}

	public void setMaritalStatus(String maritalStatus) {
		this.maritalStatus = maritalStatus;
	}

}
