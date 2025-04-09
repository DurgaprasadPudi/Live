package com.hetero.heteroiconnect.userRepositry;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

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
@Table(name = "tbl_emp_attn_req",schema = "hclhrm_prod_others")
public class AttendanceLeaveRequest {
	 
	    private static final long serialVersionUID = 1L;
	    @Id
	    @GeneratedValue(strategy = GenerationType.AUTO)
	    @Column(name = "RID", unique = true, nullable = false)
	    private long RID;
	    
		@JsonInclude(NON_NULL)
	    @Column(name = "EMPLOYEEID")
	    private String EMPLOYEEID;
		
		@JsonInclude(NON_NULL)
	    @Column(name = "SUBJECT")
	    private String SUBJECT;
		
		@JsonInclude(NON_NULL)
	    @Column(name = "RANDOMID")
	    private String RANDOMID;
		
		@JsonInclude(NON_NULL)
	    @Column(name = "FROM_DATE")
	    private String FROM_DATE;
		
		@JsonInclude(NON_NULL)
	    @Column(name = "TO_DATE")
	    private String TO_DATE;
		
		@JsonInclude(NON_NULL)
	    @Column(name = "TOTA_HOURS")
	    private String TOTA_HOURS;
		
		@JsonInclude(NON_NULL)
	    @Column(name = "TO_EMAIL")
	    private String TO_EMAIL;
		
		@JsonInclude(NON_NULL)
	    @Column(name = "REQ_DATE")
	    private String REQ_DATE;
		
		

	 

		public String getRANDOMID() {
			return RANDOMID;
		}

		public void setRANDOMID(String rANDOMID) {
			RANDOMID = rANDOMID;
		}

		public String getEMPLOYEEID() {
			return EMPLOYEEID;
		}

		 
		 

		public long getRID() {
			return RID;
		}

		public void setRID(long rID) {
			RID = rID;
		}

		public void setEMPLOYEEID(String eMPLOYEEID) {
			EMPLOYEEID = eMPLOYEEID;
		}

		public String getSUBJECT() {
			return SUBJECT;
		}

		public void setSUBJECT(String sUBJECT) {
			SUBJECT = sUBJECT;
		}

		 

		public String getFROM_DATE() {
			return FROM_DATE;
		}

		public void setFROM_DATE(String fROM_DATE) {
			FROM_DATE = fROM_DATE;
		}

		public String getTO_DATE() {
			return TO_DATE;
		}

		public void setTO_DATE(String tO_DATE) {
			TO_DATE = tO_DATE;
		}

		public String getTOTA_HOURS() {
			return TOTA_HOURS;
		}

		public void setTOTA_HOURS(String tOTA_HOURS) {
			TOTA_HOURS = tOTA_HOURS;
		}

		public String getTO_EMAIL() {
			return TO_EMAIL;
		}

		public void setTO_EMAIL(String tO_EMAIL) {
			TO_EMAIL = tO_EMAIL;
		}
		
		 
}
