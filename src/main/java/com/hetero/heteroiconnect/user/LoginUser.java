package com.hetero.heteroiconnect.user;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonInclude;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "tbl_employee_primary", schema = "hclhrm_prod")
public class LoginUser implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "EmpID", unique = true, nullable = false)
    private String EmpID;
    
    @JsonInclude(NON_NULL)
    @Column(name = "HRMSEMPLOYEEID")
    private String HRMSEMPLOYEEID;
    
    
	@JsonInclude(NON_NULL)
    @Column(name = "NAME")
    private String NAME;
	
	@JsonInclude(NON_NULL)
    @Column(name = "FIRSTNAME")
    private String FIRSTNAME;
	
	public String getICE_FIRSTNAME() {
		return ICE_FIRSTNAME;
	}

	public void setICE_FIRSTNAME(String iCE_FIRSTNAME) {
		ICE_FIRSTNAME = iCE_FIRSTNAME;
	}

	public String getICE_LASTNAME() {
		return ICE_LASTNAME;
	}

	public void setICE_LASTNAME(String iCE_LASTNAME) {
		ICE_LASTNAME = iCE_LASTNAME;
	}
	
	 

	public String getRELATIONID() {
		return RELATIONID;
	}

	public void setRELATIONID(String rELATIONID) {
		RELATIONID = rELATIONID;
	}

	public String getRELATIONNAME() {
		return RELATIONNAME;
	}

	public void setRELATIONNAME(String rELATIONNAME) {
		RELATIONNAME = rELATIONNAME;
	}



	@JsonInclude(NON_NULL)
    @Column(name = "RELATIONID")
    private String RELATIONID;
	

	@JsonInclude(NON_NULL)
    @Column(name = "RELATIONNAME")
    private String RELATIONNAME;


	@JsonInclude(NON_NULL)
    @Column(name = "BUID")
    private String BUID;
	  
	public String getBUID() {
		return BUID;
	}

	public void setBUID(String bUID) {
		BUID = bUID;
	}
  

	@JsonInclude(NON_NULL)
    @Column(name = "LASTNAME")
    private String LASTNAME;
	
	public String getFIRSTNAME() {
		return FIRSTNAME;
	}

	public void setFIRSTNAME(String fIRSTNAME) {
		FIRSTNAME = fIRSTNAME;
	}

	public String getLASTNAME() {
		return LASTNAME;
	}

	public void setLASTNAME(String lASTNAME) {
		LASTNAME = lASTNAME;
	}


	
	
	@JsonInclude(NON_NULL)
    @Column(name = "iconnectpassword")
    private String iconnectpassword;
	
	
	@JsonInclude(NON_NULL)
    @Column(name = "PWD")
    private String PWD;
	
	
	/*EmpID, PWD,NAME, STATUS, COSTCENTER, GENDER,GENDERID, EMPLOYMENT TYPE, INCREMENT TYPE, LWD,
	 *  DOB, DOJ, DIVISION, DESIGNATION, DEPARTMENT, Reportee, 
	Reportee_Status, BANKNAME, IFSC, ACCOUNTNO, PROEMAIL, PEMAIL, PFNO, ESINO, PFUAN, STATE, HQ, REGION, PROFESSIONAL_MOBILE, 
	PERSONAL_MOBILE, PERSONAL PHONE, COMMUNICATIONADDRESS, COMMUNICATIONADDRESS2, COMMUNICATIONADDRESS3, COMMUNICATIONADDRESS4, 
	COMMCITY, COMMSTATE, COMM_ZIP, PERMANENTADDRESS, PERMANENTADDRESS2, PERMANENTADDRESS3, PERMANENTADDRESS4, 
	PCITY, PSTATE, PZIPCODE, PASSPORTNO, AADHAARCARDNO, AADHAARUID, AADHAARNAME, PAN, IMPRESTAMT, 
	Education Details, Prev.Exp, Cur.Exp*/
	
	
	@JsonInclude(NON_NULL)
    @Column(name = "STATUS")
    private String STATUS;
	
	@JsonInclude(NON_NULL)
    @Column(name = "COSTCENTER")
    private String COSTCENTER;
	
	@JsonInclude(NON_NULL)
    @Column(name = "GENDER")
    private String GENDER;
	
	@JsonInclude(NON_NULL)
    @Column(name = "GENDERID")
    private String GENDERID;
	
	@JsonInclude(NON_NULL)
    @Column(name = "EMPLOYMENT TYPE")
    private String EMPLOYMENTTYPE;
	
	
	@JsonInclude(NON_NULL)
    @Column(name = "DOB")
    private String DOB;
	
	
	@JsonInclude(NON_NULL)
    @Column(name = "DOJ")
    private String DOJ;
	
	@JsonInclude(NON_NULL)
    @Column(name = "DIVISION")
    private String DIVISION;
	
	@JsonInclude(NON_NULL)
    @Column(name = "DESIGNATION")
    private String DESIGNATION;
	
	@JsonInclude(NON_NULL)
    @Column(name = "DEPARTMENT")
    private String DEPARTMENT;
	
	@JsonInclude(NON_NULL)
    @Column(name = "Reportee")
    private String Reportee;
	
	
	@JsonInclude(NON_NULL)
    @Column(name = "ICEADDRESS")
    private String ICEADDRESS;
	
	@JsonInclude(NON_NULL)
    @Column(name = "ICEADDRESS2")
    private String ICEADDRESS2;
	
	@JsonInclude(NON_NULL)
    @Column(name = "ICEADDRESS3")
    private String ICEADDRESS3;
	
	@JsonInclude(NON_NULL)
    @Column(name = "ICEADDRESS4")
    private String ICEADDRESS4;
	
	@JsonInclude(NON_NULL)
    @Column(name = "ICEMOBILE")
    private String ICEMOBILE;
	
	
	@JsonInclude(NON_NULL)
    @Column(name = "ICESTATE")
    private String ICESTATE;
	
	
	@JsonInclude(NON_NULL)
    @Column(name = "ICECITY")
    private String ICECITY;
	
	@JsonInclude(NON_NULL)
    @Column(name = "CONTACTPERSONNAME")
    private String CONTACTPERSONNAME;
	 
	@JsonInclude(NON_NULL)
    @Column(name = "ICE_STATE_ID")
    private String ICE_STATE_ID;
	
	@JsonInclude(NON_NULL)
    @Column(name = "ICE_CITY_ID")
    private String ICE_CITY_ID;
	
	
	@JsonInclude(NON_NULL)
    @Column(name = "ICE_FIRSTNAME")
    private String ICE_FIRSTNAME;
	
	@JsonInclude(NON_NULL)
    @Column(name = "ICE_LASTNAME")
    private String ICE_LASTNAME;
	
	/*
	 ice.ADDRESS ICEADDRESS,ice.ADDRESS2 ICEADDRESS2,
	  ice.ADDRESS3 ICEADDRESS3,ice.ADDRESS4 ICEADDRESS4,ice.MOBILE ICEMOBILE,
	  CONCAT(ice.FIRSTNAME,'',ice.LASTNAME) CONTACTPERSONNAME,
	 ICESTATELOC.NAME 'ICESTATE',ICELOC.NAME 'ICECITY'*/
	
	
	
 
	public String getCONTACTPERSONNAME() {
		return CONTACTPERSONNAME;
	}

	public String getICE_STATE_ID() {
		return ICE_STATE_ID;
	}

	public void setICE_STATE_ID(String iCE_STATE_ID) {
		ICE_STATE_ID = iCE_STATE_ID;
	}

	public String getICE_CITY_ID() {
		return ICE_CITY_ID;
	}

	public void setICE_CITY_ID(String iCE_CITY_ID) {
		ICE_CITY_ID = iCE_CITY_ID;
	}

	public void setCONTACTPERSONNAME(String cONTACTPERSONNAME) {
		CONTACTPERSONNAME = cONTACTPERSONNAME;
	}

	public String getICEADDRESS() {
		return ICEADDRESS;
	}

	public void setICEADDRESS(String iCEADDRESS) {
		ICEADDRESS = iCEADDRESS;
	}

	public String getICEADDRESS2() {
		return ICEADDRESS2;
	}

	public void setICEADDRESS2(String iCEADDRESS2) {
		ICEADDRESS2 = iCEADDRESS2;
	}

	public String getICEADDRESS3() {
		return ICEADDRESS3;
	}

	public void setICEADDRESS3(String iCEADDRESS3) {
		ICEADDRESS3 = iCEADDRESS3;
	}

	public String getICEADDRESS4() {
		return ICEADDRESS4;
	}

	public void setICEADDRESS4(String iCEADDRESS4) {
		ICEADDRESS4 = iCEADDRESS4;
	}

	public String getICEMOBILE() {
		return ICEMOBILE;
	}

	public void setICEMOBILE(String iCEMOBILE) {
		ICEMOBILE = iCEMOBILE;
	}

	public String getICESTATE() {
		return ICESTATE;
	}

	public void setICESTATE(String iCESTATE) {
		ICESTATE = iCESTATE;
	}

	public String getICECITY() {
		return ICECITY;
	}

	public void setICECITY(String iCECITY) {
		ICECITY = iCECITY;
	}

	 

	public String getEmpID() {
		return EmpID;
	}

	public void setEmpID(String empID) {
		EmpID = empID;
	}

	public String getNAME() {
		return NAME;
	}

	public void setNAME(String nAME) {
		NAME = nAME;
	}

	public String getIconnectpassword() {
		return iconnectpassword;
	}

	public void setIconnectpassword(String iconnectpassword) {
		this.iconnectpassword = iconnectpassword;
	}

	public String getPWD() {
		return PWD;
	}

	public void setPWD(String pWD) {
		PWD = pWD;
	}

	public String getSTATUS() {
		return STATUS;
	}

	public void setSTATUS(String sTATUS) {
		STATUS = sTATUS;
	}

	public String getCOSTCENTER() {
		return COSTCENTER;
	}

	public void setCOSTCENTER(String cOSTCENTER) {
		COSTCENTER = cOSTCENTER;
	}

	public String getGENDER() {
		return GENDER;
	}

	public void setGENDER(String gENDER) {
		GENDER = gENDER;
	}

	public String getGENDERID() {
		return GENDERID;
	}

	public void setGENDERID(String gENDERID) {
		GENDERID = gENDERID;
	}

	public String getEMPLOYMENTTYPE() {
		return EMPLOYMENTTYPE;
	}

	public void setEMPLOYMENTTYPE(String eMPLOYMENTTYPE) {
		EMPLOYMENTTYPE = eMPLOYMENTTYPE;
	}

	public String getDOB() {
		return DOB;
	}

	public void setDOB(String dOB) {
		DOB = dOB;
	}

	public String getDOJ() {
		return DOJ;
	}

	public void setDOJ(String dOJ) {
		DOJ = dOJ;
	}

	public String getDIVISION() {
		return DIVISION;
	}

	public void setDIVISION(String dIVISION) {
		DIVISION = dIVISION;
	}

	public String getDESIGNATION() {
		return DESIGNATION;
	}

	public void setDESIGNATION(String dESIGNATION) {
		DESIGNATION = dESIGNATION;
	}

	public String getDEPARTMENT() {
		return DEPARTMENT;
	}

	public void setDEPARTMENT(String dEPARTMENT) {
		DEPARTMENT = dEPARTMENT;
	}

	public String getReportee() {
		return Reportee;
	}

	public void setReportee(String reportee) {
		Reportee = reportee;
	}
 
	@JsonInclude(NON_NULL)
    @Column(name = "BANKID")
    private String BANKID;
	
	public String getBANKID() {
		return BANKID;
	}

	public void setBANKID(String bANKID) {
		BANKID = bANKID;
	}


	@JsonInclude(NON_NULL)
    @Column(name = "BANKNAME")
    private String BANKNAME;
	
	@JsonInclude(NON_NULL)
    @Column(name = "IFSC")
    private String IFSC;
	
	@JsonInclude(NON_NULL)
    @Column(name = "ACCOUNTNO")
    private String ACCOUNTNO;
	
	@JsonInclude(NON_NULL)
    @Column(name = "PROEMAIL")
    private String PROEMAIL;
	
	@JsonInclude(NON_NULL)
    @Column(name = "PEMAIL")
    private String PEMAIL;
	
	@JsonInclude(NON_NULL)
    @Column(name = "PFNO")
    private String PFNO;
	
	@JsonInclude(NON_NULL)
    @Column(name = "ESINO")
    private String ESINO;

	@JsonInclude(NON_NULL)
    @Column(name = "PFUAN")
    private String PFUAN;
	
	@JsonInclude(NON_NULL)
    @Column(name = "STATE")
    private String STATE;
	
	@JsonInclude(NON_NULL)
    @Column(name = "HQ")
    private String HQ;
	
	@JsonInclude(NON_NULL)
    @Column(name = "REGION")
    private String REGION;
	
	@JsonInclude(NON_NULL)
    @Column(name = "PROFESSIONAL_MOBILE")
    private String PROFESSIONAL_MOBILE;
	
	
	@JsonInclude(NON_NULL)
    @Column(name = "PERSONAL_MOBILE")
    private String PERSONAL_MOBILE;
	
	
	@JsonInclude(NON_NULL)
    @Column(name = "PERSONAL_PHONE")
    private String PERSONAL_PHONE;
	
 
	
	@JsonInclude(NON_NULL)
    @Column(name = "COMMUNICATIONADDRESS")
    private String COMMUNICATIONADDRESS;
	
	@JsonInclude(NON_NULL)
    @Column(name = "COMMUNICATIONADDRESS2")
    private String COMMUNICATIONADDRESS2;
	
	@JsonInclude(NON_NULL)
    @Column(name = "COMMUNICATIONADDRESS3")
    private String COMMUNICATIONADDRESS3;
	
	@JsonInclude(NON_NULL)
    @Column(name = "COMMUNICATIONADDRESS4")
    private String COMMUNICATIONADDRESS4;
	
	
	@JsonInclude(NON_NULL)
    @Column(name = "COMMCITY")
    private String COMMCITY;
	
	
	@JsonInclude(NON_NULL)
    @Column(name = "COMMSTATE")
    private String COMMSTATE;
	
	
	
	@JsonInclude(NON_NULL)
    @Column(name = "COMMSTATE_LOCATIONID")
    private String COMMSTATE_LOCATIONID;
	
	
	@JsonInclude(NON_NULL)
    @Column(name = "COMMCITY_LOCATIONID")
    private String COMMCITY_LOCATIONID;
	
	


	@JsonInclude(NON_NULL)
    @Column(name = "COMM_ZIP")
    private String COMM_ZIP;
	
	@JsonInclude(NON_NULL)
    @Column(name = "PERMANENTADDRESS")
    private String PERMANENTADDRESS;
	
	@JsonInclude(NON_NULL)
    @Column(name = "PERMANENTADDRESS2")
    private String PERMANENTADDRESS2;
	
	@JsonInclude(NON_NULL)
    @Column(name = "PERMANENTADDRESS3")
    private String PERMANENTADDRESS3;
	
	@JsonInclude(NON_NULL)
    @Column(name = "PERMANENTADDRESS4")
    private String PERMANENTADDRESS4;
	
	@JsonInclude(NON_NULL)
    @Column(name = "PCITY")
    private String PCITY;
	
	
	@JsonInclude(NON_NULL)
    @Column(name = "PSTATE")
    private String PSTATE;
	
	@JsonInclude(NON_NULL)
    @Column(name = "PZIPCODE")
    private String PZIPCODE;
	

	@JsonInclude(NON_NULL)
    @Column(name = "PERSTATE_LOCATIONID")
    private String PERSTATE_LOCATIONID;
	

	@JsonInclude(NON_NULL)
    @Column(name = "PERCITY_LOCATIONID")
    private String PERCITY_LOCATIONID;
	
	
	
	
	@JsonInclude(NON_NULL)
    @Column(name = "PASSPORTNO")
    private String PASSPORTNO;
	
	
	@JsonInclude(NON_NULL)
    @Column(name = "AADHAARCARDNO")
    private String AADHAARCARDNO;
	
	
	
	@JsonInclude(NON_NULL)
    @Column(name = "AADHAARUID")
    private String AADHAARUID;
	
	@JsonInclude(NON_NULL)
    @Column(name = "AADHAARNAME")
    private String AADHAARNAME;
	
	
	@JsonInclude(NON_NULL)
    @Column(name = "PAN")
    private String PAN;
	
	@JsonInclude(NON_NULL)
    @Column(name = "Education_Details")
    private String Education_Details;
	
	
	@JsonInclude(NON_NULL)
    @Column(name = "Prev_Exp")
    private String PrevExp;
	
	



	@JsonInclude(NON_NULL)
    @Column(name = "Cur_Exp")
    private String CurExp;
	
	@JsonInclude(NON_NULL)
    @Column(name = "TOTALEXP")
    private String TOTALEXP;
	
	@JsonInclude(NON_NULL)
    @Column(name = "TODAYDATE")
    private String TODAYDATE;
	

	@JsonInclude(NON_NULL)
    @Column(name = "LOCATION")
    private String LOCATION;
	
	@JsonInclude(NON_NULL)
    @Column(name = "SUBLOCATION")
    private String SUBLOCATION;
	
	@JsonInclude(NON_NULL)
    @Column(name = "WORKINGDAYS")
    private String WORKINGDAYS;
	
	@JsonInclude(NON_NULL)
    @Column(name = "NOTICE")
    private String NOTICE;
	
	@JsonInclude(NON_NULL)
    @Column(name = "WORKSHEET")
    private String WORKSHEET;
	
	
	
	public String getWORKSHEET() {
		return WORKSHEET;
	}

	public void setWORKSHEET(String wORKSHEET) {
		WORKSHEET = wORKSHEET;
	}

	public String getHRMSEMPLOYEEID() {
		return HRMSEMPLOYEEID;
	}

	public void setHRMSEMPLOYEEID(String hRMSEMPLOYEEID) {
		HRMSEMPLOYEEID = hRMSEMPLOYEEID;
	}
	public String getWORKINGDAYS() {
		return WORKINGDAYS;
	}

	public String getNOTICE() {
		return NOTICE;
	}

	public void setNOTICE(String nOTICE) {
		NOTICE = nOTICE;
	}

	public void setWORKINGDAYS(String wORKINGDAYS) {
		WORKINGDAYS = wORKINGDAYS;
	}

	public String getLOCATION() {
		return LOCATION;
	}

	public void setLOCATION(String lOCATION) {
		LOCATION = lOCATION;
	}

	public String getSUBLOCATION() {
		return SUBLOCATION;
	}

	public void setSUBLOCATION(String sUBLOCATION) {
		SUBLOCATION = sUBLOCATION;
	}

	public String getTODAYDATE() {
		return TODAYDATE;
	}

	public void setTODAYDATE(String tODAYDATE) {
		TODAYDATE = tODAYDATE;
	}

	public String getTOTALEXP() {
		return TOTALEXP;
	}

	public void setTOTALEXP(String tOTALEXP) {
		TOTALEXP = tOTALEXP;
	}

	public String getBANKNAME() {
		return BANKNAME;
	}

	public void setBANKNAME(String bANKNAME) {
		BANKNAME = bANKNAME;
	}

	public String getIFSC() {
		return IFSC;
	}

	public void setIFSC(String iFSC) {
		IFSC = iFSC;
	}

	public String getACCOUNTNO() {
		return ACCOUNTNO;
	}

	public void setACCOUNTNO(String aCCOUNTNO) {
		ACCOUNTNO = aCCOUNTNO;
	}

	public String getPROEMAIL() {
		return PROEMAIL;
	}

	public void setPROEMAIL(String pROEMAIL) {
		PROEMAIL = pROEMAIL;
	}

	public String getPEMAIL() {
		return PEMAIL;
	}

	public void setPEMAIL(String pEMAIL) {
		PEMAIL = pEMAIL;
	}

	public String getPFNO() {
		return PFNO;
	}

	public void setPFNO(String pFNO) {
		PFNO = pFNO;
	}

	public String getESINO() {
		return ESINO;
	}

	public void setESINO(String eSINO) {
		ESINO = eSINO;
	}

	public String getPFUAN() {
		return PFUAN;
	}

	public void setPFUAN(String pFUAN) {
		PFUAN = pFUAN;
	}

	public String getSTATE() {
		return STATE;
	}

	public void setSTATE(String sTATE) {
		STATE = sTATE;
	}

	public String getHQ() {
		return HQ;
	}

	public void setHQ(String hQ) {
		HQ = hQ;
	}

	public String getREGION() {
		return REGION;
	}

	public void setREGION(String rEGION) {
		REGION = rEGION;
	}

	public String getPROFESSIONAL_MOBILE() {
		return PROFESSIONAL_MOBILE;
	}

	public void setPROFESSIONAL_MOBILE(String pROFESSIONAL_MOBILE) {
		PROFESSIONAL_MOBILE = pROFESSIONAL_MOBILE;
	}

	public String getPERSONAL_MOBILE() {
		return PERSONAL_MOBILE;
	}

	public void setPERSONAL_MOBILE(String pERSONAL_MOBILE) {
		PERSONAL_MOBILE = pERSONAL_MOBILE;
	}

	public String getPERSONAL_PHONE() {
		return PERSONAL_PHONE;
	}

	public void setPERSONAL_PHONE(String pERSONAL_PHONE) {
		PERSONAL_PHONE = pERSONAL_PHONE;
	}

	public String getCOMMUNICATIONADDRESS() {
		return COMMUNICATIONADDRESS;
	}

	public void setCOMMUNICATIONADDRESS(String cOMMUNICATIONADDRESS) {
		COMMUNICATIONADDRESS = cOMMUNICATIONADDRESS;
	}

	public String getCOMMUNICATIONADDRESS2() {
		return COMMUNICATIONADDRESS2;
	}

	public void setCOMMUNICATIONADDRESS2(String cOMMUNICATIONADDRESS2) {
		COMMUNICATIONADDRESS2 = cOMMUNICATIONADDRESS2;
	}

	public String getCOMMUNICATIONADDRESS3() {
		return COMMUNICATIONADDRESS3;
	}

	public void setCOMMUNICATIONADDRESS3(String cOMMUNICATIONADDRESS3) {
		COMMUNICATIONADDRESS3 = cOMMUNICATIONADDRESS3;
	}

	public String getCOMMUNICATIONADDRESS4() {
		return COMMUNICATIONADDRESS4;
	}

	public void setCOMMUNICATIONADDRESS4(String cOMMUNICATIONADDRESS4) {
		COMMUNICATIONADDRESS4 = cOMMUNICATIONADDRESS4;
	}

	public String getCOMMCITY() {
		return COMMCITY;
	}

	public void setCOMMCITY(String cOMMCITY) {
		COMMCITY = cOMMCITY;
	}

	public String getCOMMSTATE() {
		return COMMSTATE;
	}

	public void setCOMMSTATE(String cOMMSTATE) {
		COMMSTATE = cOMMSTATE;
	}
	
	
	public String getCOMMSTATE_LOCATIONID() {
		return COMMSTATE_LOCATIONID;
	}

	public void setCOMMSTATE_LOCATIONID(String cOMMSTATE_LOCATIONID) {
		COMMSTATE_LOCATIONID = cOMMSTATE_LOCATIONID;
	}

	public String getCOMMCITY_LOCATIONID() {
		return COMMCITY_LOCATIONID;
	}

	public void setCOMMCITY_LOCATIONID(String cOMMCITY_LOCATIONID) {
		COMMCITY_LOCATIONID = cOMMCITY_LOCATIONID;
	}

	public String getCOMM_ZIP() {
		return COMM_ZIP;
	}

	public void setCOMM_ZIP(String cOMM_ZIP) {
		COMM_ZIP = cOMM_ZIP;
	}

	public String getPERMANENTADDRESS() {
		return PERMANENTADDRESS;
	}

	public void setPERMANENTADDRESS(String pERMANENTADDRESS) {
		PERMANENTADDRESS = pERMANENTADDRESS;
	}

	public String getPERMANENTADDRESS2() {
		return PERMANENTADDRESS2;
	}

	public void setPERMANENTADDRESS2(String pERMANENTADDRESS2) {
		PERMANENTADDRESS2 = pERMANENTADDRESS2;
	}

	public String getPERMANENTADDRESS3() {
		return PERMANENTADDRESS3;
	}

	public void setPERMANENTADDRESS3(String pERMANENTADDRESS3) {
		PERMANENTADDRESS3 = pERMANENTADDRESS3;
	}

	public String getPERMANENTADDRESS4() {
		return PERMANENTADDRESS4;
	}

	public void setPERMANENTADDRESS4(String pERMANENTADDRESS4) {
		PERMANENTADDRESS4 = pERMANENTADDRESS4;
	}

	public String getPCITY() {
		return PCITY;
	}

	public void setPCITY(String pCITY) {
		PCITY = pCITY;
	}

	public String getPSTATE() {
		return PSTATE;
	}

	public void setPSTATE(String pSTATE) {
		PSTATE = pSTATE;
	}

	public String getPZIPCODE() {
		return PZIPCODE;
	}

	public void setPZIPCODE(String pZIPCODE) {
		PZIPCODE = pZIPCODE;
	}

	public String getPERSTATE_LOCATIONID() {
		return PERSTATE_LOCATIONID;
	}

	public void setPERSTATE_LOCATIONID(String pERSTATE_LOCATIONID) {
		PERSTATE_LOCATIONID = pERSTATE_LOCATIONID;
	}

	public String getPERCITY_LOCATIONID() {
		return PERCITY_LOCATIONID;
	}

	public void setPERCITY_LOCATIONID(String pERCITY_LOCATIONID) {
		PERCITY_LOCATIONID = pERCITY_LOCATIONID;
	}

	public String getPASSPORTNO() {
		return PASSPORTNO;
	}

	public void setPASSPORTNO(String pASSPORTNO) {
		PASSPORTNO = pASSPORTNO;
	}

	public String getAADHAARCARDNO() {
		return AADHAARCARDNO;
	}

	public void setAADHAARCARDNO(String aADHAARCARDNO) {
		AADHAARCARDNO = aADHAARCARDNO;
	}

	public String getAADHAARUID() {
		return AADHAARUID;
	}

	public void setAADHAARUID(String aADHAARUID) {
		AADHAARUID = aADHAARUID;
	}

	public String getAADHAARNAME() {
		return AADHAARNAME;
	}

	public void setAADHAARNAME(String aADHAARNAME) {
		AADHAARNAME = aADHAARNAME;
	}

	public String getPAN() {
		return PAN;
	}

	public void setPAN(String pAN) {
		PAN = pAN;
	}

	public String getEducation_Details() {
		return Education_Details;
	}

	public void setEducation_Details(String education_Details) {
		Education_Details = education_Details;
	}

	public String getPrevExp() {
		return PrevExp;
	}

	public void setPrevExp(String prevExp) {
		PrevExp = prevExp;
	}

	public String getCurExp() {
		return CurExp;
	}

	public void setCurExp(String curExp) {
		CurExp = curExp;
	}

	// BANKNAME;
	//IFSC;
//	ACCOUNTNO;
	//PROEMAIL;
	//PEMAIL;
//	PFNO;
	//ESINO;
	//PFUAN;
	//STATE;
	//HQ;
	//REGION;
	//PROFESSIONAL_MOBILE;
	//PERSONAL_MOBILE;
	//PERSONAL_PHONE;
	//COMMUNICATIONADDRESS;
	//COMMUNICATIONADDRESS2;
	//COMMUNICATIONADDRESS3;
	//COMMUNICATIONADDRESS4;
	//COMMCITY;
	//COMMSTATE;
	//COMM_ZIP;
	//PERMANENTADDRESS;
	//PERMANENTADDRESS2;
	//PERMANENTADDRESS3;
	//PERMANENTADDRESS4;
	//PCITY;
	//PSTATE;
	//PZIPCODE;
	//PASSPORTNO;
	//AADHAARCARDNO;
	//AADHAARUID;
	//AADHAARNAME;
	//PAN;
	//Education_Details;
	//Prev.Exp;
	//Cur.Exp;
	
	
	
	
	
	
	
	
	
	
	 
	 

	/*EmpID, PWD,NAME, STATUS, COSTCENTER, GENDER,GENDERID, EMPLOYMENT TYPE, INCREMENT TYPE, LWD,
	 *  DOB, DOJ, DIVISION, DESIGNATION, DEPARTMENT, Reportee, 
	Reportee_Status, BANKNAME, IFSC, ACCOUNTNO, PROEMAIL, PEMAIL, PFNO, ESINO, PFUAN, STATE, HQ, REGION, PROFESSIONAL_MOBILE, 
	PERSONAL_MOBILE, PERSONAL PHONE, COMMUNICATIONADDRESS, COMMUNICATIONADDRESS2, COMMUNICATIONADDRESS3, COMMUNICATIONADDRESS4, 
	COMMCITY, COMMSTATE, COMM_ZIP, PERMANENTADDRESS, PERMANENTADDRESS2, PERMANENTADDRESS3, PERMANENTADDRESS4, 
	PCITY, PSTATE, PZIPCODE, PASSPORTNO, AADHAARCARDNO, AADHAARUID, AADHAARNAME, PAN, IMPRESTAMT, 
	Education Details, Prev.Exp, Cur.Exp*/
		 
		
	  /* @OneToMany(targetEntity = MstUser.class)
	   @JoinColumn(name = "employeeid")
	     private List<LeaveQuota> LeaveQuota;*/
	 
	
	
	/*@Where(clause = "year = '2020' and status='1001' and  leavetypeid in(1,2,4,14,15,16)")
	@OneToMany(cascade = CascadeType.ALL)  
	@JoinColumn(name="employeeid") 
	
	private List<LeaveQuota> LeaveQuota;
	
 // @OneToMany(mappedBy = "tbl_employee_login", cascade = CascadeType.ALL,targetEntity=LeaveQuota.class,fetch=FetchType.LAZY)
  public List<LeaveQuota> getLeaveQuota() {
		return LeaveQuota;
	}

	  
	 
 
	  

	
	public void setLeaveQuota(List<LeaveQuota> leaveQuota) {
		LeaveQuota = leaveQuota;
	}

	public String getEmployeecode() {
		return employeecode;
	}*/

	   
 
 
    

    /*public MstTitle getUserTitleId() {
        return userTitleId;
    }

    public void setUserTitleId(MstTitle userTitleId) {
        this.userTitleId = userTitleId;
    }*/

   
 

     
 
 

    
  

  /*  public MstUnit getUserUnitId() {
        return userUnitId;
    }

    public void setUserUnitId(MstUnit userUnitId) {
        this.userUnitId = userUnitId;
    }*/

    
    
}
