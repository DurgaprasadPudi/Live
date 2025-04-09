package com.hetero.heteroiconnect.userRepositry;

public class MangerLeaveSummary {
	
	
	private String ID;
	private String NAME;
	private String DEPT;
	private String SUBJECT; 
	private String DURATION; 
	private String DAYS;
	private String LEAVE_TYPE; 
	private String Manager_Status; 
	private String RID;
	private String FLAG;
	private String MESSAGE; 
	private String TODATE;
	private String BUTTACT;
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public String getNAME() {
		return NAME;
	}
	public void setNAME(String nAME) {
		NAME = nAME;
	}
	public String getDEPT() {
		return DEPT;
	}
	public void setDEPT(String dEPT) {
		DEPT = dEPT;
	}
	public String getSUBJECT() {
		return SUBJECT;
	}
	public void setSUBJECT(String sUBJECT) {
		SUBJECT = sUBJECT;
	}
	public String getDURATION() {
		return DURATION;
	}
	public void setDURATION(String dURATION) {
		DURATION = dURATION;
	}
	public String getDAYS() {
		return DAYS;
	}
	public void setDAYS(String dAYS) {
		DAYS = dAYS;
	}
	public String getLEAVE_TYPE() {
		return LEAVE_TYPE;
	}
	public void setLEAVE_TYPE(String lEAVE_TYPE) {
		LEAVE_TYPE = lEAVE_TYPE;
	}
	public String getManager_Status() {
		return Manager_Status;
	}
	public void setManager_Status(String manager_Status) {
		Manager_Status = manager_Status;
	}
	public String getRID() {
		return RID;
	}
	public void setRID(String rID) {
		RID = rID;
	}
	public String getFLAG() {
		return FLAG;
	}
	public void setFLAG(String fLAG) {
		FLAG = fLAG;
	}
	public String getMESSAGE() {
		return MESSAGE;
	}
	public void setMESSAGE(String mESSAGE) {
		MESSAGE = mESSAGE;
	}
	public String getTODATE() {
		return TODATE;
	}
	public void setTODATE(String tODATE) {
		TODATE = tODATE;
	}
	public String getBUTTACT() {
		return BUTTACT;
	}
	public void setBUTTACT(String bUTTACT) {
		BUTTACT = bUTTACT;
	}
	public MangerLeaveSummary(String iD, String nAME, String dEPT, String sUBJECT, String dURATION, String dAYS,
			String lEAVE_TYPE, String manager_Status, String rID, String fLAG, String mESSAGE, String tODATE,
			String bUTTACT) {
		ID = iD;
		NAME = nAME;
		DEPT = dEPT;
		SUBJECT = sUBJECT;
		DURATION = dURATION;
		DAYS = dAYS;
		LEAVE_TYPE = lEAVE_TYPE;
		Manager_Status = manager_Status;
		RID = rID;
		FLAG = fLAG;
		MESSAGE = mESSAGE;
		TODATE = tODATE;
		BUTTACT = bUTTACT;
	}
	
	

}
