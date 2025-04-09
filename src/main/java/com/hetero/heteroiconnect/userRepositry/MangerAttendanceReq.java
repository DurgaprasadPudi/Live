package com.hetero.heteroiconnect.userRepositry;

public class MangerAttendanceReq {
	
	private String ID;
	private String NAME;
	private String DEPT;
	private String SUBJECT;
	private String DURATION;
	private String NET_HOURS;
	private String Manager_Status;
	private String RID;
	private String FLAG;
	private String MESSAGE;
	private String REQ_DATE;
	
	
	public MangerAttendanceReq(String iD, String nAME, String dEPT, String sUBJECT, String dURATION, String nET_HOURS,
			String manager_Status, String rID, String fLAG, String mESSAGE, String rEQ_DATE) {
		ID = iD;
		NAME = nAME;
		DEPT = dEPT;
		SUBJECT = sUBJECT;
		DURATION = dURATION;
		NET_HOURS = nET_HOURS;
		Manager_Status = manager_Status;
		RID = rID;
		FLAG = fLAG;
		MESSAGE = mESSAGE;
		REQ_DATE = rEQ_DATE;
	}
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
	public String getNET_HOURS() {
		return NET_HOURS;
	}
	public void setNET_HOURS(String nET_HOURS) {
		NET_HOURS = nET_HOURS;
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
	public String getREQ_DATE() {
		return REQ_DATE;
	}
	public void setREQ_DATE(String rEQ_DATE) {
		REQ_DATE = rEQ_DATE;
	}
	
	 

}
