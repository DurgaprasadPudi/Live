package com.hetero.heteroiconnect.couriertracker;

import java.sql.Date;
public class CourierSenderTrackingDTO {
    private Integer senderTrackingId;
    private Date registerDate;
    private Integer courierTypeId;
    private String docketNo;
    private Integer trackingStatusId;
    private String senderName;
    private String senderId;
    private String senderContactNo;
    private String receiverName;
    private String receiverContactNo;
    private String fromLocation;
    private String toLocation;
    private String material;
    private String comment;
    private String dept;
    private String createdBy;
    private String updatedBy;
    private String courierName;
   
	public Integer getSenderTrackingId() {
		return senderTrackingId;
	}
	public void setSenderTrackingId(Integer senderTrackingId) {
		this.senderTrackingId = senderTrackingId;
	}
	public Date getRegisterDate() {
		return registerDate;
	}
	public void setRegisterDate(Date registerDate) {
		this.registerDate = registerDate;
	}
	public Integer getCourierTypeId() {
		return courierTypeId;
	}
	public void setCourierTypeId(Integer courierTypeId) {
		this.courierTypeId = courierTypeId;
	}
	public String getDocketNo() {
		return docketNo;
	}
	public void setDocketNo(String docketNo) {
		this.docketNo = docketNo;
	}
	public Integer getTrackingStatusId() {
		return trackingStatusId;
	}
	public void setTrackingStatusId(Integer trackingStatusId) {
		this.trackingStatusId = trackingStatusId;
	}
	public String getSenderName() {
		return senderName;
	}
	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}
	
	public String getSenderId() {
		return senderId;
	}
	public void setSenderId(String senderId) {
		this.senderId = senderId;
	}
	public String getSenderContactNo() {
		return senderContactNo;
	}
	public void setSenderContactNo(String senderContactNo) {
		this.senderContactNo = senderContactNo;
	}
	public String getReceiverName() {
		return receiverName;
	}
	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}
	public String getReceiverContactNo() {
		return receiverContactNo;
	}
	public void setReceiverContactNo(String receiverContactNo) {
		this.receiverContactNo = receiverContactNo;
	}
	public String getFromLocation() {
		return fromLocation;
	}
	public void setFromLocation(String fromLocation) {
		this.fromLocation = fromLocation;
	}

	public String getToLocation() {
		return toLocation;
	}
	public void setToLocation(String toLocation) {
		this.toLocation = toLocation;
	}
	public String getMaterial() {
		return material;
	}
	public void setMaterial(String material) {
		this.material = material;
	}
	
	public String getDept() {
		return dept;
	}
	public void setDept(String dept) {
		this.dept = dept;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public String getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}
	public String getCourierName() {
		return courierName;
	}
	public void setCourierName(String courierName) {
		this.courierName = courierName;
	}

    
}
