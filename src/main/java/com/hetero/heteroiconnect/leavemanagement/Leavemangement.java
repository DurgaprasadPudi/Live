package com.hetero.heteroiconnect.leavemanagement;

public class Leavemangement {
	
	 
    private String employeesequenceno;
    private String SHORTNAME;
    private String quantity;
    private String AVAILABLEQTY;
    private String AVAILABLEQTY1;
    private String HOLD;
    private String totalavl;
    private String USEDQTY;
    private String Fullname;
    private String DAYMODE;
    private String MAXLEAVE_C;
    private String COUNT_WOFF;
    private String bkdays;
    private String BACKDATE;
    
    
	public String getEmployeesequenceno() {
		return employeesequenceno;
	}
	public void setEmployeesequenceno(String employeesequenceno) {
		this.employeesequenceno = employeesequenceno;
	}
	public String getSHORTNAME() {
		return SHORTNAME;
	}
	public void setSHORTNAME(String sHORTNAME) {
		SHORTNAME = sHORTNAME;
	}
	public String getQuantity() {
		return quantity;
	}
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
	public String getAVAILABLEQTY() {
		return AVAILABLEQTY;
	}
	public void setAVAILABLEQTY(String aVAILABLEQTY) {
		AVAILABLEQTY = aVAILABLEQTY;
	}
	public String getAVAILABLEQTY1() {
		return AVAILABLEQTY1;
	}
	public void setAVAILABLEQTY1(String aVAILABLEQTY1) {
		AVAILABLEQTY1 = aVAILABLEQTY1;
	}
	public String getHOLD() {
		return HOLD;
	}
	public void setHOLD(String hOLD) {
		HOLD = hOLD;
	}
	public String getTotalavl() {
		return totalavl;
	}
	public void setTotalavl(String totalavl) {
		this.totalavl = totalavl;
	}
	public String getUSEDQTY() {
		return USEDQTY;
	}
	public void setUSEDQTY(String uSEDQTY) {
		USEDQTY = uSEDQTY;
	}
	public String getFullname() {
		return Fullname;
	}
	public void setFullname(String fullname) {
		Fullname = fullname;
	}
	public String getDAYMODE() {
		return DAYMODE;
	}
	public void setDAYMODE(String dAYMODE) {
		DAYMODE = dAYMODE;
	}
	public String getMAXLEAVE_C() {
		return MAXLEAVE_C;
	}
	public void setMAXLEAVE_C(String mAXLEAVE_C) {
		MAXLEAVE_C = mAXLEAVE_C;
	}
	public String getCOUNT_WOFF() {
		return COUNT_WOFF;
	}
	public void setCOUNT_WOFF(String cOUNT_WOFF) {
		COUNT_WOFF = cOUNT_WOFF;
	}
	public String getBkdays() {
		return bkdays;
	}
	public void setBkdays(String bkdays) {
		this.bkdays = bkdays;
	}
	public String getBACKDATE() {
		return BACKDATE;
	}
	public void setBACKDATE(String bACKDATE) {
		BACKDATE = bACKDATE;
	}
	public Leavemangement(String employeesequenceno, String sHORTNAME, String quantity, String aVAILABLEQTY,
			String aVAILABLEQTY1, String hOLD, String totalavl, String uSEDQTY, String fullname, String dAYMODE,
			String mAXLEAVE_C, String cOUNT_WOFF, String bkdays, String bACKDATE) {
		this.employeesequenceno = employeesequenceno;
		SHORTNAME = sHORTNAME;
		this.quantity = quantity;
		AVAILABLEQTY = aVAILABLEQTY;
		AVAILABLEQTY1 = aVAILABLEQTY1;
		HOLD = hOLD;
		this.totalavl = totalavl;
		USEDQTY = uSEDQTY;
		Fullname = fullname;
		DAYMODE = dAYMODE;
		MAXLEAVE_C = mAXLEAVE_C;
		COUNT_WOFF = cOUNT_WOFF;
		this.bkdays = bkdays;
		BACKDATE = bACKDATE;
	}
    
    
    



 
}
