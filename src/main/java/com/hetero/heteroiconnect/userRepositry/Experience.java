package com.hetero.heteroiconnect.userRepositry;

public class Experience {

	
	//EXPERIENCE, EMPLOYERNAME, DESIGNATION
	
	private String EXPERIENCE;
	private String EMPLOYERNAME;
	private String DESIGNATION;
	public String getEXPERIENCE() {
		return EXPERIENCE;
	}
	public void setEXPERIENCE(String eXPERIENCE) {
		EXPERIENCE = eXPERIENCE;
	}
	public String getEMPLOYERNAME() {
		return EMPLOYERNAME;
	}
	public void setEMPLOYERNAME(String eMPLOYERNAME) {
		EMPLOYERNAME = eMPLOYERNAME;
	}
	public String getDESIGNATION() {
		return DESIGNATION;
	}
	public void setDESIGNATION(String dESIGNATION) {
		DESIGNATION = dESIGNATION;
	}
	public Experience(String eXPERIENCE, String eMPLOYERNAME, String dESIGNATION) {
		EXPERIENCE = eXPERIENCE;
		EMPLOYERNAME = eMPLOYERNAME;
		DESIGNATION = dESIGNATION;
	}
	
	
}
