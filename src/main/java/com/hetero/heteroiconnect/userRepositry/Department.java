package com.hetero.heteroiconnect.userRepositry;

public class Department {
	
	 private String EMPID;
	 private String EMPNAME;
	 private String BUNAME;
	 private String Department;
	 private String designation;
	 private String EMAIL;
	 private String MOBILE;
	 private String CODE;
	 private String GENDERID;
	 private String PROFILEPIC;
	 
	 
	public String getEMPID() {
		return EMPID;
	}
	public void setEMPID(String eMPID) {
		EMPID = eMPID;
	}
	public String getPROFILEPIC() {
		return PROFILEPIC;
	}
	public void setPROFILEPIC(String pROFILEPIC) {
		PROFILEPIC = pROFILEPIC;
	}
	public String getCODE() {
		return CODE;
	}
	public void setCODE(String cODE) {
		CODE = cODE;
	}
	public String getGENDERID() {
		return GENDERID;
	}
	public void setGENDERID(String gENDERID) {
		GENDERID = gENDERID;
	}
	public String getEMPNAME() {
		return EMPNAME;
	}
	public void setEMPNAME(String eMPNAME) {
		EMPNAME = eMPNAME;
	}
	public String getBUNAME() {
		return BUNAME;
	}
	public void setBUNAME(String bUNAME) {
		BUNAME = bUNAME;
	}
	public String getDepartment() {
		return Department;
	}
	public void setDepartment(String department) {
		Department = department;
	}
	public String getDesignation() {
		return designation;
	}
	public void setDesignation(String designation) {
		this.designation = designation;
	}
	public String getEMAIL() {
		return EMAIL;
	}
	public void setEMAIL(String eMAIL) {
		EMAIL = eMAIL;
	}
	public String getMOBILE() {
		return MOBILE;
	}
	public void setMOBILE(String mOBILE) {
		MOBILE = mOBILE;
	}
	/*public Department(String eMPNAME, String bUNAME, String department, String designation, String eMAIL,
			String mOBILE, String cODE, String gENDERID) {
		EMPNAME = eMPNAME;
		BUNAME = bUNAME;
		Department = department;
		this.designation = designation;
		EMAIL = eMAIL;
		MOBILE = mOBILE;
		CODE = cODE;
		GENDERID = gENDERID;
	}
	 */
	public Department(String eMPID, String eMPNAME, String bUNAME, String department, String designation, String eMAIL,
			String mOBILE, String cODE, String gENDERID, String pROFILEPIC) {
		EMPID = eMPID;
		EMPNAME = eMPNAME;
		BUNAME = bUNAME;
		Department = department;
		this.designation = designation;
		EMAIL = eMAIL;
		MOBILE = mOBILE;
		CODE = cODE;
		GENDERID = gENDERID;
		PROFILEPIC = pROFILEPIC;
	}
	 
	
	 

}
