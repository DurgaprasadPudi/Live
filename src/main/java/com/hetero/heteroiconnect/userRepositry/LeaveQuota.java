package com.hetero.heteroiconnect.userRepositry;

public class LeaveQuota {
	
	
	 private String LEAVETYPENAME;
	 private  double QUANTITY;
	 private double AVAILQTY;
	 private double USEDQTY;
	 private String LEAVETYPE;
	 
	 
	 
	 public LeaveQuota(String lEAVETYPENAME, double qUANTITY, double aVAILQTY, double uSEDQTY, String lEAVETYPE) {
		LEAVETYPENAME = lEAVETYPENAME;
		QUANTITY = qUANTITY;
		AVAILQTY = aVAILQTY;
		USEDQTY = uSEDQTY;
		LEAVETYPE = lEAVETYPE;
	}
	public String getLEAVETYPE() {
		return LEAVETYPE;
	}
	public void setLEAVETYPE(String lEAVETYPE) {
		LEAVETYPE = lEAVETYPE;
	}
	public String getLEAVETYPENAME() {
		return LEAVETYPENAME;
	}
	public void setLEAVETYPENAME(String lEAVETYPENAME) {
		LEAVETYPENAME = lEAVETYPENAME;
	}
	public double getQUANTITY() {
		return QUANTITY;
	}
	public void setQUANTITY(double qUANTITY) {
		QUANTITY = qUANTITY;
	}
	public double getAVAILQTY() {
		return AVAILQTY;
	}
	public void setAVAILQTY(double aVAILQTY) {
		AVAILQTY = aVAILQTY;
	}
	public double getUSEDQTY() {
		return USEDQTY;
	}
	public void setUSEDQTY(double uSEDQTY) {
		USEDQTY = uSEDQTY;
	}
	
	  
	 
	 
//	LEAVETYPENAME, USEDQTY, AVAILQTY, USEDQTY

}
