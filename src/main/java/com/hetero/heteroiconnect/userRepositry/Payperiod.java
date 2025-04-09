package com.hetero.heteroiconnect.userRepositry;

public class Payperiod {
	
	
	
	private String PAYPERIOD; 
	private String MONTHNAME;
	private String YEAR;
	private String FROMDATE;
	private String TODATE;
	
	private String PAYPERIODMONTHNAME;
	private String PAYPERIODYEAR;
	private String PAYPERIODFROMDATE;
	private String PAYPERIODTODATE;
	 
	
	public String getPAYPERIODMONTHNAME() {
		return PAYPERIODMONTHNAME;
	}
	public void setPAYPERIODMONTHNAME(String pAYPERIODMONTHNAME) {
		PAYPERIODMONTHNAME = pAYPERIODMONTHNAME;
	}
	public String getPAYPERIODYEAR() {
		return PAYPERIODYEAR;
	}
	public void setPAYPERIODYEAR(String pAYPERIODYEAR) {
		PAYPERIODYEAR = pAYPERIODYEAR;
	}
	public String getPAYPERIODFROMDATE() {
		return PAYPERIODFROMDATE;
	}
	public void setPAYPERIODFROMDATE(String pAYPERIODFROMDATE) {
		PAYPERIODFROMDATE = pAYPERIODFROMDATE;
	}
	public String getPAYPERIODTODATE() {
		return PAYPERIODTODATE;
	}
	public void setPAYPERIODTODATE(String pAYPERIODTODATE) {
		PAYPERIODTODATE = pAYPERIODTODATE;
	}
	public String getPAYPERIOD() {
		return PAYPERIOD;
	}
	public void setPAYPERIOD(String pAYPERIOD) {
		PAYPERIOD = pAYPERIOD;
	}
	public String getMONTHNAME() {
		return MONTHNAME;
	}
	public void setMONTHNAME(String mONTHNAME) {
		MONTHNAME = mONTHNAME;
	}
	public String getYEAR() {
		return YEAR;
	}
	public void setYEAR(String yEAR) {
		YEAR = yEAR;
	}
	public String getFROMDATE() {
		return FROMDATE;
	}
	public void setFROMDATE(String fROMDATE) {
		FROMDATE = fROMDATE;
	}
	public String getTODATE() {
		return TODATE;
	}
	public void setTODATE(String tODATE) {
		TODATE = tODATE;
	}
	public Payperiod(String pAYPERIOD, 
			String pAYPERIODMONTHNAME, String pAYPERIODYEAR, String pAYPERIODFROMDATE, String pAYPERIODTODATE,String mONTHNAME, String yEAR, String fROMDATE, String tODATE) {
		PAYPERIOD = pAYPERIOD;
		MONTHNAME = mONTHNAME;
		YEAR = yEAR;
		FROMDATE = fROMDATE; 
		TODATE = tODATE;
		PAYPERIODMONTHNAME = pAYPERIODMONTHNAME;
		PAYPERIODYEAR = pAYPERIODYEAR;
		PAYPERIODFROMDATE = pAYPERIODFROMDATE;
		PAYPERIODTODATE = pAYPERIODTODATE;
	}
	 
	
	
	

}
