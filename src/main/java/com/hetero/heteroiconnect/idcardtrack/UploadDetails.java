package com.hetero.heteroiconnect.idcardtrack;

public class UploadDetails {
 
	private Integer employeeid;
	private String idcardissueddate;
	private String comment;
	private Integer createdby;
	private String cardType;
 
	public UploadDetails() {
		super();
		// TODO Auto-generated constructor stub
	}

	public UploadDetails(Integer employeeid, String idcardissueddate, String comment, Integer createdby,
			String cardType) {
		super();
		this.employeeid = employeeid;
		this.idcardissueddate = idcardissueddate;
		this.comment = comment;
		this.createdby = createdby;
		this.cardType = cardType;
	}
 
	public String getCardType() {
		return cardType;
	}
 
	public void setCardType(String cardType) {
		this.cardType = cardType;
	}
 
	public Integer getEmployeeid() {
		return employeeid;
	}
 
	public void setEmployeeid(Integer employeeid) {
		this.employeeid = employeeid;
	}
 
	public String getIdcardissueddate() {
		return idcardissueddate;
	}
 
	public void setIdcardissueddate(String idcardissueddate) {
		this.idcardissueddate = idcardissueddate;
	}
 
	public String getComment() {
		return comment;
	}
 
	public void setComment(String comment) {
		this.comment = comment;
	}
 
	public Integer getCreatedby() {
		return createdby;
	}
 
	public void setCreatedby(Integer createdby) {
		this.createdby = createdby;
	}
 
}