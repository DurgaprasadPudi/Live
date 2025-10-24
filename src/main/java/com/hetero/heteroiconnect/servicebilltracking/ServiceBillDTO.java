package com.hetero.heteroiconnect.servicebilltracking;

import java.util.Date;

public class ServiceBillDTO {

	private Integer serviceId;
	private String vendorName;
	private String location;
	private Integer countryId;
	private Integer stateId;
	private Integer cityId;
	private String sapCode;
	private Date billSubmittedDate;
	private Date billReceivedDate;
	private String invoice;
	private String cheque;
	private int invoiceAmount;
	private Date invoiceDate;
	private int businessUnitId;
	private String remark;
	private Integer status;
	private String createdBy;
	private String updatedBy;

	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}

	public String getVendorName() {
		return vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Integer getCountryId() {
		return countryId;
	}

	public void setCountryId(Integer countryId) {
		this.countryId = countryId;
	}

	public Integer getStateId() {
		return stateId;
	}

	public void setStateId(Integer stateId) {
		this.stateId = stateId;
	}

	public Integer getCityId() {
		return cityId;
	}

	public void setCityId(Integer cityId) {
		this.cityId = cityId;
	}

	public String getSapCode() {
		return sapCode;
	}

	public void setSapCode(String sapCode) {
		this.sapCode = sapCode;
	}

	public Date getBillSubmittedDate() {
		return billSubmittedDate;
	}

	public void setBillSubmittedDate(Date billSubmittedDate) {
		this.billSubmittedDate = billSubmittedDate;
	}

	public Date getBillReceivedDate() {
		return billReceivedDate;
	}

	public void setBillReceivedDate(Date billReceivedDate) {
		this.billReceivedDate = billReceivedDate;
	}

	public String getInvoice() {
		return invoice;
	}

	public void setInvoice(String invoice) {
		this.invoice = invoice;
	}

	public String getCheque() {
		return cheque;
	}

	public void setCheque(String cheque) {
		this.cheque = cheque;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
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

	public int getInvoiceAmount() {
		return invoiceAmount;
	}

	public void setInvoiceAmount(int invoiceAmount) {
		this.invoiceAmount = invoiceAmount;
	}

	public Date getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(Date invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public int getBusinessUnitId() {
		return businessUnitId;
	}

	public void setBusinessUnitId(int businessUnitId) {
		this.businessUnitId = businessUnitId;
	}

}
