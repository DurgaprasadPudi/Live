package com.hetero.heteroiconnect.userRepositry;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

public class LeaveSummary  implements Serializable{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/*APPLIEDDATE, FROM_DATE, TO_DATE,
	DAYS, LEAVE_TYPE, MESSAGE, MAIL_STATUS, FLAG, COMMENTS, 
	RID_R, FLAG_S, DTFLAG,
	APPROVEDNAME*/
	 @NotNull
	private String APPLIEDDATE;
	 @NotNull
	private String  FROM_DATE;
	 @NotNull
	private String TO_DATE;
	 @NotNull
	private String   DAYS;
	 @NotNull
	private String  LEAVE_TYPE;
	 @NotNull
	private String  MESSAGE;
	 @NotNull
	private String  MAIL_STATUS;
	 @NotNull
	private String   FLAG;
	 @NotNull
	private String   COMMENTS; 
	 @NotNull
	private String    RID_R;
	 @NotNull
	private String  FLAG_S;
	 @NotNull
	private String   DTFLAG;
	 @NotNull
	private String APPROVEDNAME;
	
	
 
	public String getAPPLIEDDATE() {
		return APPLIEDDATE;
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
	public LeaveSummary(String aPPLIEDDATE, String fROM_DATE, String tO_DATE, String dAYS, String lEAVE_TYPE,
			String mESSAGE, String mAIL_STATUS, String fLAG, String cOMMENTS, String rID_R, String fLAG_S,
			String dTFLAG, String aPPROVEDNAME) {
		APPLIEDDATE = aPPLIEDDATE;
		FROM_DATE = fROM_DATE;
		TO_DATE = tO_DATE;
		DAYS = dAYS;
		LEAVE_TYPE = lEAVE_TYPE;
		MESSAGE = mESSAGE;
		MAIL_STATUS = mAIL_STATUS;
		FLAG = fLAG;
		COMMENTS = cOMMENTS;
		RID_R = rID_R;
		FLAG_S = fLAG_S;
		DTFLAG = dTFLAG;
		APPROVEDNAME = aPPROVEDNAME;
	}
	
	
	
	

}
