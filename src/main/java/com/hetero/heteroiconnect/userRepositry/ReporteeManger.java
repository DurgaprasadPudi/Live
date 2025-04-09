package com.hetero.heteroiconnect.userRepositry;

public class ReporteeManger {
	

	 
    private String IS_MANAGER;
    private String MANGERID;
    private String MANGEREMAIL;
    private String MANGERCODE;
    private String MANGERSTATUS;
    
	public String getMANGERCODE() {
		return MANGERCODE;
	}
	public void setMANGERCODE(String mANGERCODE) {
		MANGERCODE = mANGERCODE;
	}
	public String getMANGERSTATUS() {
		return MANGERSTATUS;
	}
	public void setMANGERSTATUS(String mANGERSTATUS) {
		MANGERSTATUS = mANGERSTATUS;
	}
	public String getIS_MANAGER() {
		return IS_MANAGER;
	}
	public void setIS_MANAGER(String iS_MANAGER) {
		IS_MANAGER = iS_MANAGER;
	}
	public String getMANGERID() {
		return MANGERID;
	}
	public void setMANGERID(String mANGERID) {
		MANGERID = mANGERID;
	}
	public String getMANGEREMAIL() {
		return MANGEREMAIL;
	}
	public void setMANGEREMAIL(String mANGEREMAIL) {
		MANGEREMAIL = mANGEREMAIL;
	}
	public ReporteeManger(String iS_MANAGER, String mANGERID, String mANGEREMAIL, String mANGERCODE,
			String mANGERSTATUS) {
		IS_MANAGER = iS_MANAGER;
		MANGERID = mANGERID;
		MANGEREMAIL = mANGEREMAIL;
		MANGERCODE = mANGERCODE;
		MANGERSTATUS = mANGERSTATUS;
	}
	 
 

}
