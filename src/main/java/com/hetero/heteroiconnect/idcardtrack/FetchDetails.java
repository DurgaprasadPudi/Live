package com.hetero.heteroiconnect.idcardtrack;

public class FetchDetails {
	private Integer trackingId;
	private Integer employeeid;
	private String idCardIssuedDate;
	private String issuedComment;
	private Integer createdBy;
	private String createdByName;
	private String createddate;
	private Integer status;
	private String cardType;
	private String employeeName;
	private String idSubmittedDate;
	private String idSubmittedBy;
	private String submittedComment;
	private String submittedByName;
	private String finalStatus;
	
	
 
	public FetchDetails() {
		super();
		// TODO Auto-generated constructor stub
	}
 
	public FetchDetails(Integer trackingId, Integer employeeid, String idCardIssuedDate, String issuedComment,
			Integer createdBy, String createdByName, String createddate, Integer status, String cardType,
			String employeeName, String idSubmittedDate, String idSubmittedBy, String submittedComment,
			String submittedByName, String finalStatus) {
		super();
		this.trackingId = trackingId;
		this.employeeid = employeeid;
		this.idCardIssuedDate = idCardIssuedDate;
		this.issuedComment = issuedComment;
		this.createdBy = createdBy;
		this.createdByName = createdByName;
		this.createddate = createddate;
		this.status = status;
		this.cardType = cardType;
		this.employeeName = employeeName;
		this.idSubmittedDate = idSubmittedDate;
		this.idSubmittedBy = idSubmittedBy;
		this.submittedComment = submittedComment;
		this.submittedByName = submittedByName;
		this.finalStatus = finalStatus;
	}
 
	public Integer getTrackingId() {
		return trackingId;
	}
 
	public void setTrackingId(Integer trackingId) {
		this.trackingId = trackingId;
	}
 
	public Integer getEmployeeid() {
		return employeeid;
	}
 
	public void setEmployeeid(Integer employeeid) {
		this.employeeid = employeeid;
	}
 
	public String getIdCardIssuedDate() {
		return idCardIssuedDate;
	}
 
	public void setIdCardIssuedDate(String idCardIssuedDate) {
		this.idCardIssuedDate = idCardIssuedDate;
	}
 
	public String getIssuedComment() {
		return issuedComment;
	}
 
	public void setIssuedComment(String issuedComment) {
		this.issuedComment = issuedComment;
	}
 
	public Integer getCreatedBy() {
		return createdBy;
	}
 
	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}
 
	public String getCreatedByName() {
		return createdByName;
	}
 
	public void setCreatedByName(String createdByName) {
		this.createdByName = createdByName;
	}
 
	public String getCreateddate() {
		return createddate;
	}
 
	public void setCreateddate(String createddate) {
		this.createddate = createddate;
	}
 
	public Integer getStatus() {
		return status;
	}
 
	public void setStatus(Integer status) {
		this.status = status;
	}
 
	public String getCardType() {
		return cardType;
	}
 
	public void setCardType(String cardType) {
		this.cardType = cardType;
	}
 
	public String getEmployeeName() {
		return employeeName;
	}
 
	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}
 
	public String getIdSubmittedDate() {
		return idSubmittedDate;
	}
 
	public void setIdSubmittedDate(String idSubmittedDate) {
		this.idSubmittedDate = idSubmittedDate;
	}
 
	public String getIdSubmittedBy() {
		return idSubmittedBy;
	}
 
	public void setIdSubmittedBy(String idSubmittedBy) {
		this.idSubmittedBy = idSubmittedBy;
	}
 
	public String getSubmittedComment() {
		return submittedComment;
	}
 
	public void setSubmittedComment(String submittedComment) {
		this.submittedComment = submittedComment;
	}
 
	public String getSubmittedByName() {
		return submittedByName;
	}
 
	public void setSubmittedByName(String submittedByName) {
		this.submittedByName = submittedByName;
	}
 
	public String getFinalStatus() {
		return finalStatus;
	}
 
	public void setFinalStatus(String finalStatus) {
		this.finalStatus = finalStatus;
	}
 
}
 
  