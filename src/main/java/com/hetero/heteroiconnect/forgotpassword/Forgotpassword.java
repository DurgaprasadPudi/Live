package com.hetero.heteroiconnect.forgotpassword;

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
public class Forgotpassword implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "EmpID", unique = true, nullable = false)
    private String EmpID;
    
	@JsonInclude(NON_NULL)
    @Column(name = "COUNT")
    private String COUNT;
	
	@JsonInclude(NON_NULL)
    @Column(name = "MOBILE")
    private String MOBILE;
	
	@JsonInclude(NON_NULL)
    @Column(name = "MOBLENGTH")
    private String MOBLENGTH;

	public String getEmpID() {
		return EmpID;
	}

	public void setEmpID(String empID) {
		EmpID = empID;
	}

	public String getCOUNT() {
		return COUNT;
	}

	public void setCOUNT(String cOUNT) {
		COUNT = cOUNT;
	}

	 

	public String getMOBILE() {
		return MOBILE;
	}

	public void setMOBILE(String mOBILE) {
		MOBILE = mOBILE;
	}

	public String getMOBLENGTH() {
		return MOBLENGTH;
	}

	public void setMOBLENGTH(String mOBLENGTH) {
		MOBLENGTH = mOBLENGTH;
	}
	  
	 
 
}
