package com.hetero.heteroiconnect.userRepositry;

public class Holidaylist {
	
	 private String DAYNAME;
	 private String DATE;
	 
	public String getDAYNAME() {
		return DAYNAME;
	}
	public void setDAYNAME(String dAYNAME) {
		DAYNAME = dAYNAME;
	}
	public String getDATE() {
		return DATE;
	}
	public void setDATE(String dATE) {
		DATE = dATE;
	}
	public Holidaylist(String dAYNAME, String dATE) {
		super();
		DAYNAME = dAYNAME;
		DATE = dATE;
	}

}
