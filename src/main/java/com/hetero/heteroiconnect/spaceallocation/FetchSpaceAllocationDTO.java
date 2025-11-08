package com.hetero.heteroiconnect.spaceallocation;

public class FetchSpaceAllocationDTO {
	private int allocationId;
	private int departmentId;
	private String deptName;
	private int designationId;
	private String desgName;
	private String joiningDate;
	private String cabinOrSeat;
	private int noOfSeats;
	private int status;
	private String comments;
	private String createdDateTime;
	private int raisedBy;
	private String callName;

	public FetchSpaceAllocationDTO() {
		super();
	}

	public FetchSpaceAllocationDTO(int allocationId, int departmentId, String deptName, int designationId,
			String desgName, String joiningDate, String cabinOrSeat, int noOfSeats, int status, String comments,
			String createdDateTime, int raisedBy, String callName) {
		super();
		this.allocationId = allocationId;
		this.departmentId = departmentId;
		this.deptName = deptName;
		this.designationId = designationId;
		this.desgName = desgName;
		this.joiningDate = joiningDate;
		this.cabinOrSeat = cabinOrSeat;
		this.noOfSeats = noOfSeats;
		this.status = status;
		this.comments = comments;
		this.createdDateTime = createdDateTime;
		this.raisedBy = raisedBy;
		this.callName = callName;
	}

	public int getAllocationId() {
		return allocationId;
	}

	public void setAllocationId(int allocationId) {
		this.allocationId = allocationId;
	}

	public int getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(int departmentId) {
		this.departmentId = departmentId;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public int getDesignationId() {
		return designationId;
	}

	public void setDesignationId(int designationId) {
		this.designationId = designationId;
	}

	public String getDesgName() {
		return desgName;
	}

	public void setDesgName(String desgName) {
		this.desgName = desgName;
	}

	public String getJoiningDate() {
		return joiningDate;
	}

	public void setJoiningDate(String joiningDate) {
		this.joiningDate = joiningDate;
	}

	public String getCabinOrSeat() {
		return cabinOrSeat;
	}

	public void setCabinOrSeat(String cabinOrSeat) {
		this.cabinOrSeat = cabinOrSeat;
	}

	public int getNoOfSeats() {
		return noOfSeats;
	}

	public void setNoOfSeats(int noOfSeats) {
		this.noOfSeats = noOfSeats;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getCreatedDateTime() {
		return createdDateTime;
	}

	public void setCreatedDateTime(String createdDateTime) {
		this.createdDateTime = createdDateTime;
	}

	public int getRaisedBy() {
		return raisedBy;
	}

	public void setRaisedBy(int raisedBy) {
		this.raisedBy = raisedBy;
	}

	public String getCallName() {
		return callName;
	}

	public void setCallName(String callName) {
		this.callName = callName;
	}

}
