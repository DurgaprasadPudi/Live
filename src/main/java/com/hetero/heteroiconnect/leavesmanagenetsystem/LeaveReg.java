package com.hetero.heteroiconnect.leavesmanagenetsystem;

import java.math.BigDecimal;
import java.math.BigInteger;

public class LeaveReg {
	
		private int EMPLOYEEID;
		private BigInteger LEAVETYPEID;
		private BigInteger YEAR;
		private BigDecimal QUANTITY;
		private BigDecimal AVAILABLEQTY;
		private BigInteger USEDQTY;
		private BigInteger HOLD;
		private BigInteger DAYMODE;
		private BigInteger MAXLEAVE;
		private BigInteger BACKDATE;
		private int COUNT_WOFF;
		private int COUNT_HOLIDAY;
		private String STATUS;
		private String LOGID;
		private String CREATEDBY;
		private String DATECREATED;
		private String MODIFIEDBY;
		private String DATEMODIFIED;
		private String VERIFIEDBY;	
		private String DATEVERIFIED;
		private String LUPDATE;
		private String MINIMU_LEAVE;
		private String FOR_MONTH;
		private String LEAVE_MODE;
		public int getEMPLOYEEID() {
			return EMPLOYEEID;
		}
		public void setEMPLOYEEID(int eMPLOYEEID) {
			EMPLOYEEID = eMPLOYEEID;
		}
		public BigInteger getLEAVETYPEID() {
			return LEAVETYPEID;
		}
		public void setLEAVETYPEID(BigInteger lEAVETYPEID) {
			LEAVETYPEID = lEAVETYPEID;
		}
		public BigInteger getYEAR() {
			return YEAR;
		}
		public void setYEAR(BigInteger yEAR) {
			YEAR = yEAR;
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
		public BigInteger getUSEDQTY() {
			return USEDQTY;
		}
		public void setUSEDQTY(BigInteger uSEDQTY) {
			USEDQTY = uSEDQTY;
		}
		public BigInteger getHOLD() {
			return HOLD;
		}
		public void setHOLD(BigInteger hOLD) {
			HOLD = hOLD;
		}
		public BigInteger getDAYMODE() {
			return DAYMODE;
		}
		public void setDAYMODE(BigInteger dAYMODE) {
			DAYMODE = dAYMODE;
		}
		public BigInteger getMAXLEAVE() {
			return MAXLEAVE;
		}
		public void setMAXLEAVE(BigInteger mAXLEAVE) {
			MAXLEAVE = mAXLEAVE;
		}
		public BigInteger getBACKDATE() {
			return BACKDATE;
		}
		public void setBACKDATE(BigInteger bACKDATE) {
			BACKDATE = bACKDATE;
		}
		public int getCOUNT_WOFF() {
			return COUNT_WOFF;
		}
		public void setCOUNT_WOFF(int cOUNT_WOFF) {
			COUNT_WOFF = cOUNT_WOFF;
		}
		public int getCOUNT_HOLIDAY() {
			return COUNT_HOLIDAY;
		}
		public void setCOUNT_HOLIDAY(int cOUNT_HOLIDAY) {
			COUNT_HOLIDAY = cOUNT_HOLIDAY;
		}
		public String getSTATUS() {
			return STATUS;
		}
		public void setSTATUS(String sTATUS) {
			STATUS = sTATUS;
		}
		public String getLOGID() {
			return LOGID;
		}
		public void setLOGID(String lOGID) {
			LOGID = lOGID;
		}
		public String getCREATEDBY() {
			return CREATEDBY;
		}
		public void setCREATEDBY(String cREATEDBY) {
			CREATEDBY = cREATEDBY;
		}
		public String getDATECREATED() {
			return DATECREATED;
		}
		public void setDATECREATED(String dATECREATED) {
			DATECREATED = dATECREATED;
		}
		public String getMODIFIEDBY() {
			return MODIFIEDBY;
		}
		public void setMODIFIEDBY(String mODIFIEDBY) {
			MODIFIEDBY = mODIFIEDBY;
		}
		public String getDATEMODIFIED() {
			return DATEMODIFIED;
		}
		public void setDATEMODIFIED(String dATEMODIFIED) {
			DATEMODIFIED = dATEMODIFIED;
		}
		public String getVERIFIEDBY() {
			return VERIFIEDBY;
		}
		public void setVERIFIEDBY(String vERIFIEDBY) {
			VERIFIEDBY = vERIFIEDBY;
		}
		public String getDATEVERIFIED() {
			return DATEVERIFIED;
		}
		public void setDATEVERIFIED(String dATEVERIFIED) {
			DATEVERIFIED = dATEVERIFIED;
		}
		public String getLUPDATE() {
			return LUPDATE;
		}
		public void setLUPDATE(String lUPDATE) {
			LUPDATE = lUPDATE;
		}
		public String getMINIMU_LEAVE() {
			return MINIMU_LEAVE;
		}
		public void setMINIMU_LEAVE(String mINIMU_LEAVE) {
			MINIMU_LEAVE = mINIMU_LEAVE;
		}
		public String getFOR_MONTH() {
			return FOR_MONTH;
		}
		public void setFOR_MONTH(String fOR_MONTH) {
			FOR_MONTH = fOR_MONTH;
		}
		public String getLEAVE_MODE() {
			return LEAVE_MODE;
		}
		public void setLEAVE_MODE(String lEAVE_MODE) {
			LEAVE_MODE = lEAVE_MODE;
		}
		@Override
		public String toString() {
			return "LeaveReg [EMPLOYEEID=" + EMPLOYEEID + ", LEAVETYPEID=" + LEAVETYPEID + ", YEAR=" + YEAR
					+ ", QUANTITY=" + QUANTITY + ", AVAILABLEQTY=" + AVAILABLEQTY + ", USEDQTY=" + USEDQTY + ", HOLD="
					+ HOLD + ", DAYMODE=" + DAYMODE + ", MAXLEAVE=" + MAXLEAVE + ", BACKDATE=" + BACKDATE
					+ ", COUNT_WOFF=" + COUNT_WOFF + ", COUNT_HOLIDAY=" + COUNT_HOLIDAY + ", STATUS=" + STATUS
					+ ", LOGID=" + LOGID + ", CREATEDBY=" + CREATEDBY + ", DATECREATED=" + DATECREATED + ", MODIFIEDBY="
					+ MODIFIEDBY + ", DATEMODIFIED=" + DATEMODIFIED + ", VERIFIEDBY=" + VERIFIEDBY + ", DATEVERIFIED="
					+ DATEVERIFIED + ", LUPDATE=" + LUPDATE + ", MINIMU_LEAVE=" + MINIMU_LEAVE + ", FOR_MONTH="
					+ FOR_MONTH + ", LEAVE_MODE=" + LEAVE_MODE + "]";
		}
		
		

}
