package com.hetero.heteroiconnect.userRepositry;

public class Colorcode {
	
	private String COLORCODE;
	private String COLORCODENAME;
	private String DISPLAYNAME;
	public String getDISPLAYNAME() {
		return DISPLAYNAME;
	}
	public void setDISPLAYNAME(String dISPLAYNAME) {
		DISPLAYNAME = dISPLAYNAME;
	}
	public String getCOLORCODE() {
		return COLORCODE;
	}
	public void setCOLORCODE(String cOLORCODE) {
		COLORCODE = cOLORCODE;
	}
	public String getCOLORCODENAME() {
		return COLORCODENAME;
	}
	public void setCOLORCODENAME(String cOLORCODENAME) {
		COLORCODENAME = cOLORCODENAME;
	}
	public Colorcode(String cOLORCODE, String cOLORCODENAME, String dISPLAYNAME) {
		COLORCODE = cOLORCODE;
		COLORCODENAME = cOLORCODENAME;
		DISPLAYNAME = dISPLAYNAME;
	}
 
	

}
