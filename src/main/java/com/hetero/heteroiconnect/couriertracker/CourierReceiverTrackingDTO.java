package com.hetero.heteroiconnect.couriertracker;

import java.sql.Date;

public class CourierReceiverTrackingDTO {
    private Integer receiverTrackingId;
    private Date receivedDate;
    private String receiverId;
    private String receiverName;
    private Integer courierTypeId;
    private String receiverContactNo;
    private String docketNo;
    private String senderDetails;
    private String receiverLocation;
    private String materialReceived;
    private String dept;
    private String courierName;
    private String comment;
    private String updatedBy;
    private String createdBy;
	public Integer getReceiverTrackingId() {
		return receiverTrackingId;
	}
	public void setReceiverTrackingId(Integer receiverTrackingId) {
		this.receiverTrackingId = receiverTrackingId;
	}
	public Date getReceivedDate() {
		return receivedDate;
	}
	public void setReceivedDate(Date receivedDate) {
		this.receivedDate = receivedDate;
	}
	public String getReceiverId() {
		return receiverId;
	}
	public void setReceiverId(String receiverId) {
		this.receiverId = receiverId;
	}
	public String getReceiverName() {
		return receiverName;
	}
	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}
	public Integer getCourierTypeId() {
		return courierTypeId;
	}
	public void setCourierTypeId(Integer courierTypeId) {
		this.courierTypeId = courierTypeId;
	}
	public String getReceiverContactNo() {
		return receiverContactNo;
	}
	public void setReceiverContactNo(String receiverContactNo) {
		this.receiverContactNo = receiverContactNo;
	}
	public String getDocketNo() {
		return docketNo;
	}
	public void setDocketNo(String docketNo) {
		this.docketNo = docketNo;
	}
	public String getSenderDetails() {
		return senderDetails;
	}
	public void setSenderDetails(String senderDetails) {
		this.senderDetails = senderDetails;
	}
	public String getReceiverLocation() {
		return receiverLocation;
	}
	public void setReceiverLocation(String receiverLocation) {
		this.receiverLocation = receiverLocation;
	}
	public String getMaterialReceived() {
		return materialReceived;
	}
	public void setMaterialReceived(String materialReceived) {
		this.materialReceived = materialReceived;
	}
	public String getDept() {
		return dept;
	}
	public void setDept(String dept) {
		this.dept = dept;
	}
	public String getCourierName() {
		return courierName;
	}
	public void setCourierName(String courierName) {
		this.courierName = courierName;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	

}
