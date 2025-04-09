package com.hetero.heteroiconnect.leavesmanagenetsystem;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import java.math.BigDecimal;
import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "tbl_emp_leave_quota")
//@Table(name = "tbl_emp_leave_quota", schema = "hclhrm_prod_others")
public class Leaves_Registration {
	
// EMPLOYEEID, LEAVETYPEID, YEAR, QUANTITY, AVAILABLEQTY, USEDQTY, HOLD, DAYMODE, MAXLEAVE, BACKDATE, 
//COUNT_WOFF, COUNT_HOLIDAY, STATUS, LOGID, CREATEDBY, DATECREATED, MODIFIEDBY, DATEMODIFIED, VERIFIEDBY,
//DATEVERIFIED, LUPDATE, MINIMU_LEAVE, FOR_MONTH, LEAVE_MODE	
	
	// unique = true
	 @Id
	// @GeneratedValue(strategy = GenerationType.IDENTITY)
	//  @Column(name = "EMPLOYEE_ID")
   //@GeneratedValue(strategy = GenerationType.SEQUENCE)
	// @GeneratedValue(generator = "EMPLOYEEID", strategy = GenerationType.SEQUENCE)
	 
	// @SequenceGenerator(name = "EMPLOYEEID", sequenceName ="EMPLOYEEID")
	
		@JsonInclude(NON_NULL)
		@JsonProperty
   @Column(name = "EMPLOYEEID")
	private int EMPLOYEEID;
	@JsonInclude(NON_NULL)
	@JsonProperty
    @Column(name = "LEAVETYPEID")
	private BigInteger LEAVETYPEID;
	@JsonInclude(NON_NULL)
	@JsonProperty
    @Column(name = "YEAR")
	private BigInteger YEAR;
	@JsonInclude(NON_NULL)
	@JsonProperty
    @Column(name = "QUANTITY")
	private BigDecimal QUANTITY;
	@JsonInclude(NON_NULL)
	@JsonProperty
    @Column(name = "AVAILABLEQTY")
	private BigDecimal AVAILABLEQTY;
	@JsonInclude(NON_NULL)
	@JsonProperty
    @Column(name = "USEDQTY")
	private BigInteger USEDQTY;
	@JsonInclude(NON_NULL)
	@JsonProperty
    @Column(name = "HOLD")
	private BigInteger HOLD;
	@JsonInclude(NON_NULL)
	@JsonProperty
    @Column(name = "DAYMODE")
	private BigInteger DAYMODE;
	@JsonInclude(NON_NULL)
	@JsonProperty
    @Column(name = "MAXLEAVE")
	private BigInteger MAXLEAVE;
	@JsonInclude(NON_NULL)
	@JsonProperty
    @Column(name = "BACKDATE")
	private BigInteger BACKDATE;
	@JsonInclude(NON_NULL)
	@JsonProperty
    @Column(name = "COUNT_WOFF")
	private int COUNT_WOFF;
	@JsonInclude(NON_NULL)
	@JsonProperty
    @Column(name = "COUNT_HOLIDAY")
	private int COUNT_HOLIDAY;
	@JsonInclude(NON_NULL)
	@JsonProperty
    @Column(name = "STATUS")
	private String STATUS;
	@JsonInclude(NON_NULL)
	@JsonProperty
    @Column(name = "LOGID")
	private String LOGID;
	@JsonInclude(NON_NULL)
	@JsonProperty
    @Column(name = "CREATEDBY")
	private String CREATEDBY;
	@JsonInclude(NON_NULL)
	@JsonProperty
    @Column(name = "DATECREATED")
	private String DATECREATED;
	@JsonInclude(NON_NULL)
	@JsonProperty
    @Column(name = "MODIFIEDBY")
	private String MODIFIEDBY;
	@JsonInclude(NON_NULL)
	@JsonProperty
    @Column(name = "DATEMODIFIED")
	private String DATEMODIFIED;
	@JsonInclude(NON_NULL)
	@JsonProperty
    @Column(name = "VERIFIEDBY")
	private String VERIFIEDBY;
	@JsonInclude(NON_NULL)
	@JsonProperty
    @Column(name = "DATEVERIFIED")
	private String DATEVERIFIED;
	@JsonInclude(NON_NULL)
	@JsonProperty
    @Column(name = "LUPDATE")
	private String LUPDATE;
	@JsonInclude(NON_NULL)
	@JsonProperty
    @Column(name = "MINIMU_LEAVE")
	private String MINIMU_LEAVE;
	@JsonInclude(NON_NULL)
	@JsonProperty
    @Column(name = "FOR_MONTH")
	private String FOR_MONTH;
	@JsonInclude(NON_NULL)
	@JsonProperty
    @Column(name = "LEAVE_MODE")
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
	
	

}
