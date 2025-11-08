package com.hetero.heteroiconnect.spaceallocation;

public class SpaceAllocationDTO {
	private int departmentId;
	private int designationId;
	private String joiningDate;
	private String cabinOrSeat;
	private int noOfSeats;
	private int raisedBy;

	public int getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(int departmentId) {
		this.departmentId = departmentId;
	}

	public int getDesignationId() {
		return designationId;
	}

	public void setDesignationId(int designationId) {
		this.designationId = designationId;
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

	public int getRaisedBy() {
		return raisedBy;
	}

	public void setRaisedBy(int raisedBy) {
		this.raisedBy = raisedBy;
	}

	public SpaceAllocationDTO(int departmentId, int designationId, String joiningDate, String cabinOrSeat,
			int noOfSeats, int raisedBy) {
		super();
		this.departmentId = departmentId;
		this.designationId = designationId;
		this.joiningDate = joiningDate;
		this.cabinOrSeat = cabinOrSeat;
		this.noOfSeats = noOfSeats;
		this.raisedBy = raisedBy;
	}

	public SpaceAllocationDTO() {
		super();
	}

}
