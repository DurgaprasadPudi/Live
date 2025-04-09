package com.hetero.heteroiconnect.userRepositry;

public class GetLeaveSummary {
	
	
	/*APPLIEDDATE, FROM_DATE, TO_DATE,
	DAYS, LEAVE_TYPE, MESSAGE, MAIL_STATUS, FLAG, COMMENTS, 
	RID_R, FLAG_S, DTFLAG,
	APPROVEDNAME*/
	
	private String APPLIEDDATE;
	
	private String  FROM_DATE;
	 
	private String TO_DATE;
	 
	private String   DAYS;
	
	private String  LEAVE_TYPE;
	
	private String  MESSAGE;
	
	private String  MAIL_STATUS;
	 
	private String   FLAG;
	 
	private String   COMMENTS; 
	
	private String    RID_R;
	
	private String  FLAG_S;
	
	private String   DTFLAG;
	 
	private String APPROVEDNAME;
	
	 private String reasonComment;
	 
	 private String empid;
	 
	 private String empname;
	 
	 
	
 
 
	public String getEmpname() {
		return empname;
	}
	public void setEmpname(String empname) {
		this.empname = empname;
	}
	public String getEmpid() {
		return empid;
	}
	public void setEmpid(String empid) {
		this.empid = empid;
	}
	public String getAPPLIEDDATE() {
		return APPLIEDDATE;
	}
	public String getReasonComment() {
		return reasonComment;
	}
	public void setReasonComment(String reasonComment) {
		this.reasonComment = reasonComment;
	}
	public void setAPPLIEDDATE(String aPPLIEDDATE) {
		APPLIEDDATE = aPPLIEDDATE;
	}
	public String getFROM_DATE() {
		return FROM_DATE;
	}
	public void setFROM_DATE(String fROM_DATE) {
		FROM_DATE = fROM_DATE;
	}
	public String getTO_DATE() {
		return TO_DATE;
	}
	public void setTO_DATE(String tO_DATE) {
		TO_DATE = tO_DATE;
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
	public String getMESSAGE() {
		return MESSAGE;
	}
	public void setMESSAGE(String mESSAGE) {
		MESSAGE = mESSAGE;
	}
	public String getMAIL_STATUS() {
		return MAIL_STATUS;
	}
	public void setMAIL_STATUS(String mAIL_STATUS) {
		MAIL_STATUS = mAIL_STATUS;
	}
	public String getFLAG() {
		return FLAG;
	}
	public void setFLAG(String fLAG) {
		FLAG = fLAG;
	}
	public String getCOMMENTS() {
		return COMMENTS;
	}
	public void setCOMMENTS(String cOMMENTS) {
		COMMENTS = cOMMENTS;
	}
	public String getRID_R() {
		return RID_R;
	}
	public void setRID_R(String rID_R) {
		RID_R = rID_R;
	}
	public String getFLAG_S() {
		return FLAG_S;
	}
	public void setFLAG_S(String fLAG_S) {
		FLAG_S = fLAG_S;
	}
	public String getDTFLAG() {
		return DTFLAG;
	}
	public void setDTFLAG(String dTFLAG) {
		DTFLAG = dTFLAG;
	}
	public String getAPPROVEDNAME() {
		return APPROVEDNAME;
	}
	public void setAPPROVEDNAME(String aPPROVEDNAME) {
		APPROVEDNAME = aPPROVEDNAME;
	}
	 
	
	

}
