package com.hetero.heteroiconnect.hrassetrequests;

import java.util.Date;
import java.util.List;

public class AssetRequestDTO {
	private int requestId;
	private int empId;
	private String empName;
	private String deptName;
	private String desName;
	private String contactNum;
	private String reportingManagerId;
	private String reportingManagerName;
	private String tentativeJoiningDate;
	private String buName;
	private Date raisedDate;
	private String status;
	private int raisedById;
	private String raisedByName;
	private String workLocation;
	private String acknowledgeByName;
	private String acknowledgeById;
	private Date acknowledgeDate;
	private long totalCount;
	private List<AssetItemDTO> items;

	public AssetRequestDTO(int requestId, int empId, String empName, String deptName, String desName, String contactNum,
			String reportingManagerId, String reportingManagerName, String tentativeJoiningDate, String buName,
			Date raisedDate, String status, int raisedById, String raisedByName, String workLocation,
			String acknowledgeByName, String acknowledgeById, Date acknowledgeDate, long totalCount,
			List<AssetItemDTO> items) {
		super();
		this.requestId = requestId;
		this.empId = empId;
		this.empName = empName;
		this.deptName = deptName;
		this.desName = desName;
		this.contactNum = contactNum;
		this.reportingManagerId = reportingManagerId;
		this.reportingManagerName = reportingManagerName;
		this.tentativeJoiningDate = tentativeJoiningDate;
		this.buName = buName;
		this.raisedDate = raisedDate;
		this.status = status;
		this.raisedById = raisedById;
		this.raisedByName = raisedByName;
		this.workLocation = workLocation;
		this.acknowledgeByName = acknowledgeByName;
		this.acknowledgeById = acknowledgeById;
		this.acknowledgeDate = acknowledgeDate;
		this.totalCount = totalCount;
		this.items = items;

	}

	public long getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(long totalCount) {
		this.totalCount = totalCount;
	}

	public int getRequestId() {
		return requestId;
	}

	public void setRequestId(int requestId) {
		this.requestId = requestId;
	}

	public int getEmpId() {
		return empId;
	}

	public void setEmpId(int empId) {
		this.empId = empId;
	}

	public String getEmpName() {
		return empName;
	}

	public void setEmpName(String empName) {
		this.empName = empName;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getDesName() {
		return desName;
	}

	public void setDesName(String desName) {
		this.desName = desName;
	}

	public String getContactNum() {
		return contactNum;
	}

	public void setContactNum(String contactNum) {
		this.contactNum = contactNum;
	}

	public String getTentativeJoiningDate() {
		return tentativeJoiningDate;
	}

	public void setTentativeJoiningDate(String tentativeJoiningDate) {
		this.tentativeJoiningDate = tentativeJoiningDate;
	}

	public List<AssetItemDTO> getItems() {
		return items;
	}

	public void setItems(List<AssetItemDTO> items) {
		this.items = items;
	}

	public String getBuName() {
		return buName;
	}

	public void setBuName(String buName) {
		this.buName = buName;
	}

	public String getReportingManagerId() {
		return reportingManagerId;
	}

	public void setReportingManagerId(String reportingManagerId) {
		this.reportingManagerId = reportingManagerId;
	}

	public String getReportingManagerName() {
		return reportingManagerName;
	}

	public void setReportingManagerName(String reportingManagerName) {
		this.reportingManagerName = reportingManagerName;
	}

	public Date getRaisedDate() {
		return raisedDate;
	}

	public void setRaisedDate(Date raisedDate) {
		this.raisedDate = raisedDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getRaisedById() {
		return raisedById;
	}

	public void setRaisedById(int raisedById) {
		this.raisedById = raisedById;
	}

	public String getRaisedByName() {
		return raisedByName;
	}

	public void setRaisedByName(String raisedByName) {
		this.raisedByName = raisedByName;
	}

	public String getWorkLocation() {
		return workLocation;
	}

	public void setWorkLocation(String workLocation) {
		this.workLocation = workLocation;
	}

	public String getAcknowledgeByName() {
		return acknowledgeByName;
	}

	public void setAcknowledgeByName(String acknowledgeByName) {
		this.acknowledgeByName = acknowledgeByName;
	}

	public String getAcknowledgeById() {
		return acknowledgeById;
	}

	public void setAcknowledgeById(String acknowledgeById) {
		this.acknowledgeById = acknowledgeById;
	}

	public Date getAcknowledgeDate() {
		return acknowledgeDate;
	}

	public void setAcknowledgeDate(Date acknowledgeDate) {
		this.acknowledgeDate = acknowledgeDate;
	}

}
