package com.hetero.heteroiconnect.userRepositry;

public class Birthdaylist {
	
	private String EMPID;
	private String ENAME;
	private String BUNAME;
	private String DEPARTMENT;
	private String Email;
	private String Mobile;
	private String DATEOFBIRTH;
	private String GENDERID;
	private String BIRTHDAYDATEFORMAT;
	private String FILTERMONTH;
	private String DAYNAME;
	private String PROFILEPIC;
	
	
	
	
	
	
//	EMPID, ENAME, NAME, DEPARTMENT, Email, Mobile, DATEOFBIRTH
	 
	public String getDAYNAME() {
		return DAYNAME;
	}
	public void setDAYNAME(String dAYNAME) {
		DAYNAME = dAYNAME;
	}
 
	
	public String getPROFILEPIC() {
		return PROFILEPIC;
	}
	public void setPROFILEPIC(String pROFILEPIC) {
		PROFILEPIC = pROFILEPIC;
	}
	public String getFILTERMONTH() {
		return FILTERMONTH;
	}
	public void setFILTERMONTH(String fILTERMONTH) {
		FILTERMONTH = fILTERMONTH;
	}
	 
	public String getGENDERID() {
		return GENDERID;
	}
	public void setGENDERID(String gENDERID) {
		GENDERID = gENDERID;
	}
	public String getBIRTHDAYDATEFORMAT() {
		return BIRTHDAYDATEFORMAT;
	}
	public void setBIRTHDAYDATEFORMAT(String bIRTHDAYDATEFORMAT) {
		BIRTHDAYDATEFORMAT = bIRTHDAYDATEFORMAT;
	}
	public String getEMPID() {
		return EMPID;
	}
	
	 
	
	public String getBUNAME() {
		return BUNAME;
	}
	public void setBUNAME(String bUNAME) {
		BUNAME = bUNAME;
	}
	public void setEMPID(String eMPID) {
		EMPID = eMPID;
	}
	public String getENAME() {
		return ENAME;
	}
	public void setENAME(String eNAME) {
		ENAME = eNAME;
	}
	public String getDEPARTMENT() {
		return DEPARTMENT;
	}
	public void setDEPARTMENT(String dEPARTMENT) {
		DEPARTMENT = dEPARTMENT;
	}
	public String getEmail() {
		return Email;
	}
	public void setEmail(String email) {
		Email = email;
	}
	public String getMobile() {
		return Mobile;
	}
	public void setMobile(String mobile) {
		Mobile = mobile;
	}
	public String getDATEOFBIRTH() {
		return DATEOFBIRTH;
	}
	public void setDATEOFBIRTH(String dATEOFBIRTH) {
		DATEOFBIRTH = dATEOFBIRTH;
	}
	public Birthdaylist(String eMPID, String eNAME, String bUNAME, String dEPARTMENT, String email, String mobile,
			String dATEOFBIRTH, String gENDERID, String bIRTHDAYDATEFORMAT, String fILTERMONTH, String dAYNAME,
			String pROFILEPIC) {
		EMPID = eMPID;
		ENAME = eNAME;
		BUNAME = bUNAME;
		DEPARTMENT = dEPARTMENT;
		Email = email;
		Mobile = mobile;
		DATEOFBIRTH = dATEOFBIRTH;
		GENDERID = gENDERID;
		BIRTHDAYDATEFORMAT = bIRTHDAYDATEFORMAT;
		FILTERMONTH = fILTERMONTH;
		DAYNAME = dAYNAME;
		PROFILEPIC = pROFILEPIC;
	}
	
	
	 
	
	
	 

}
