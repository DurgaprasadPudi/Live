package com.hetero.heteroiconnect.userRepositry;

public class HrDocuments {
	
	private String DISPLAYNAME;
	private String PDF_FILE;
	private String IMAGE_FILE;
	public String getDISPLAYNAME() {
		return DISPLAYNAME;
	}
	public void setDISPLAYNAME(String dISPLAYNAME) {
		DISPLAYNAME = dISPLAYNAME;
	}
	public String getPDF_FILE() {
		return PDF_FILE;
	}
	public void setPDF_FILE(String pDF_FILE) {
		PDF_FILE = pDF_FILE;
	}
	public String getIMAGE_FILE() {
		return IMAGE_FILE;
	}
	public void setIMAGE_FILE(String iMAGE_FILE) {
		IMAGE_FILE = iMAGE_FILE;
	}
	public HrDocuments(String dISPLAYNAME, String pDF_FILE, String iMAGE_FILE) {
		super();
		DISPLAYNAME = dISPLAYNAME;
		PDF_FILE = pDF_FILE;
		IMAGE_FILE = iMAGE_FILE;
	}
	
	

}
