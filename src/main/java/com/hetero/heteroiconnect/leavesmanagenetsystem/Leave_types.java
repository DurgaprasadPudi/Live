package com.hetero.heteroiconnect.leavesmanagenetsystem;

import java.math.BigDecimal;

public class Leave_types {

	private int LEAVETYPEID;
	private String NAME;
	private String SHORTNAME;
	private BigDecimal QUANTITY;
	private BigDecimal AVAILABLEQTY;
	private BigDecimal USEDQTY;
	private BigDecimal Hold;
	private short DAYMODE;
	private int MAXLEAVE;
	
	//private String ISAGAINSTDATE;
	public int getLEAVETYPEID() {
		return LEAVETYPEID;
	}
	public void setLEAVETYPEID(int lEAVETYPEID) {
		LEAVETYPEID = lEAVETYPEID;
	}
	public String getNAME() {
		return NAME;
	}
	public void setNAME(String nAME) {
		NAME = nAME;
	}
	public String getSHORTNAME() {
		return SHORTNAME;
	}
	public void setSHORTNAME(String sHORTNAME) {
		SHORTNAME = sHORTNAME;
	}
	public BigDecimal getQUANTITY() {
		return QUANTITY;
	}
	public void setQUANTITY(BigDecimal qUANTITY) {
		QUANTITY = qUANTITY;
	}
	public BigDecimal getAVAILABLEQTY() {
		return AVAILABLEQTY;
	}
	public void setAVAILABLEQTY(BigDecimal aVAILABLEQTY) {
		AVAILABLEQTY = aVAILABLEQTY;
	}
	public BigDecimal getUSEDQTY() {
		return USEDQTY;
	}
	public void setUSEDQTY(BigDecimal uSEDQTY) {
		USEDQTY = uSEDQTY;
	}
	public BigDecimal getHold() {
		return Hold;
	}
	public void setHold(BigDecimal hold) {
		Hold = hold;
	}
	public short getDAYMODE() {
		return DAYMODE;
	}
	public void setDAYMODE(short dAYMODE) {
		DAYMODE = dAYMODE;
	}
	public int getMAXLEAVE() {
		return MAXLEAVE;
	}
	public void setMAXLEAVE(int mAXLEAVE) {
		MAXLEAVE = mAXLEAVE;
	}
	
	 
	 
	
	

}
