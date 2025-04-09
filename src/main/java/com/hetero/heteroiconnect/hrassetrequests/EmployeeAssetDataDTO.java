package com.hetero.heteroiconnect.hrassetrequests;

import java.util.Date;

public class EmployeeAssetDataDTO {

	private Integer assetTypeId;
	private Integer count;
	private Date createdDateTime;
	private Integer requestId;
	private String remarks;

	public Integer getAssetTypeId() {
		return assetTypeId;
	}

	public void setAssetTypeId(Integer assetTypeId) {
		this.assetTypeId = assetTypeId;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public Date getCreatedDateTime() {
		return createdDateTime;
	}

	public void setCreatedDateTime(Date createdDateTime) {
		this.createdDateTime = createdDateTime;
	}

	public Integer getRequestId() {
		return requestId;
	}

	public void setRequestId(Integer requestId) {
		this.requestId = requestId;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	@Override
	public String toString() {
		return "EmployeeAssetDataDTO [assetTypeId=" + assetTypeId + ", count=" + count + ", createdDateTime="
				+ createdDateTime + ", requestId=" + requestId + ", remarks=" + remarks + "]";
	}

}
