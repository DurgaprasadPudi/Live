package com.hetero.heteroiconnect.userRepositry;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonInclude;

@Entity
public class INOUT implements Serializable {
 
	private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "employeeid", unique = true, nullable = false)
    private long employeeid;
 
    
    @JsonInclude(NON_NULL)
    @Column(name = "DATE")
    private String DATE;
    
	@JsonInclude(NON_NULL)
    @Column(name = "In")
    private String In;
	
	@JsonInclude(NON_NULL)
    @Column(name = "Out")
    private String Out;
	

	@JsonInclude(NON_NULL)
    @Column(name = "Net Hours")
    private String NetHours;
	
	@JsonInclude(NON_NULL)
    @Column(name = "WDAY")
    private String WDAY;
	
	@JsonInclude(NON_NULL)
    @Column(name = "Day Type")
    private String DayType;
	
	
	
/*	a.DATEON 'DATE',a.ATT_IN 'In',a.Att_out 'Out',a.Net_Hours 'Net Hours', 
	IF(a.ATT_IN='00:00:00'&&a.Att_out='00:00:00'&&a.Daytype='WDAY',
	IFNULL(b.Leave_Type,'A'),a.Daytype)'Day Type'*/
    
  /*  @JsonInclude(NON_NULL)
    @Column(name = "CL_AVAILABLEQTY")
    private String CL_AVAILABLEQTY;
    
    @JsonInclude(NON_NULL)
    @Column(name = "CL_USEDQTY")
    private String CL_USEDQTY;*/
    
   
/*
	@JsonInclude(NON_NULL)
    @Column(name = "SL_QUANTITY")
    private String SL_QUANTITY;
    
    @JsonInclude(NON_NULL)
    @Column(name = "SL_AVAILABLEQTY")
    private String SL_AVAILABLEQTY;
    
    @JsonInclude(NON_NULL)
    @Column(name = "SL_USEDQTY")
    private String SL_USEDQTY;*/
	
	/*@JsonInclude(NON_NULL)
    @Column(name = "availableqty")
    private String AVAILABLEQTY;
	
	
	
	@JsonInclude(NON_NULL)
    @Column(name = "Leavetypeid")
    private String Leavetypeid;*/
	
	 

    /*@Column(name = "bloodgroupName")
    private String bloodgroupName;

    @JsonIgnore
    @Column(name = "isActive", columnDefinition = "binary(1) default true", nullable = false)
    private Boolean isActive = true;

    @JsonIgnore
    @Column(name = "isDeleted", columnDefinition = "binary(1) default false", nullable = false)
    private Boolean isDeleted = false;

    @JsonIgnore
    @CreatedBy
    @Column(nullable = false, updatable = false)
    private String createdBy;

    @JsonIgnore
    @LastModifiedBy
    private String lastModifiedBy;

    @JsonIgnore
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private Date createdDate;

    @JsonIgnore
    @LastModifiedDate
    private Date lastModifiedDate;*/

    

  
	 
  

 
 

	public String getDATE() {
		return DATE;
	}

	 

	public INOUT(long employeeid, String dATE, String in, String out, String netHours,
			String dayType) {
		super();
		this.employeeid = employeeid;
		DATE = dATE;
		In = in;
		Out = out;
		NetHours = netHours;
		DayType = dayType;
	}



	public void setDATE(String dATE) {
		DATE = dATE;
	}

	public String getIn() {
		return In;
	}

	public void setIn(String in) {
		In = in;
	}

	 

	public long getEmployeeid() {
		return employeeid;
	}

	public void setEmployeeid(long employeeid) {
		this.employeeid = employeeid;
	}

	public String getOut() {
		return Out;
	}

	public void setOut(String out) {
		Out = out;
	}

	public String getNetHours() {
		return NetHours;
	}

	public void setNetHours(String netHours) {
		NetHours = netHours;
	}

	 
	public String getDayType() {
		return DayType;
	}

	public void setDayType(String dayType) {
		DayType = dayType;
	}
     
	 

}            
