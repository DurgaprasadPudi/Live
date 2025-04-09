package com.hetero.heteroiconnect.familydetails;

import java.sql.Date;

public class FamilyDataDTO {
	private String sno;
	private String name;
	private String relation;
	private String gender;
	private Date dob;
	private String bloodGroup;
	private String aadhar;
	private String occupation;
	// private String filePath;
	private String empCode;
	private byte[] filePath;
	private String originalFilePath;

	public String getOriginalFilePath() {
		return originalFilePath;
	}

	public String setOriginalFilePath(String originalFilePath) {
		return this.originalFilePath = originalFilePath;
	}

	public byte[] getFilePath() {
		return filePath;
	}

	public void setFilePath(byte[] filePath) {
		this.filePath = filePath;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRelation() {
		return relation;
	}

	public void setRelation(String relation) {
		this.relation = relation;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	public String getBloodGroup() {
		return bloodGroup;
	}

	public void setBloodGroup(String bloodGroup) {
		this.bloodGroup = bloodGroup;
	}

	public String getAadhar() {
		return aadhar;
	}

	public void setAadhar(String aadhar) {
		this.aadhar = aadhar;
	}

	public String getOccupation() {
		return occupation;
	}

	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}

	public String getEmpCode() {
		return empCode;
	}

	public void setEmpCode(String empCode) {
		this.empCode = empCode;
	}

	public String getSno() {
		return sno;
	}

	public void setSno(String sno) {
		this.sno = sno;
	}

}