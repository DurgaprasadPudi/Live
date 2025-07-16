package com.hetero.heteroiconnect.stationaryandhousekeepingtrack;

import java.util.List;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class AssignPojo {
	@NotNull(message = "Employee ID must not be null")
	@Min(value = 1, message = "Employee ID must be a positive number")
	private Integer empId;
	@NotNull(message = "AssignBy must not be null")
	@Min(value = 1, message = "AssignBy must be a positive number")
	private Integer assignBy;
	@NotNull(message = "Item type must not be null")
	@Min(value = 1, message = "Item type must be a positive number")
	private Integer type;
	private boolean isRegular;
	@NotNull(message = "Assignment data must not be null")
	private List<AssignList> data;

	public Integer getEmpId() {
		return empId;
	}

	public void setEmpId(int empId) {
		this.empId = empId;
	}

	public Integer getAssignBy() {
		return assignBy;
	}

	public void setAssignBy(int assignBy) {
		this.assignBy = assignBy;
	}

	public boolean getIsRegular() {
		return isRegular;
	}

	public void setIsRegular(boolean isRegular) {
		this.isRegular = isRegular;
	}

	public List<AssignList> getData() {
		return data;
	}

	public void setData(List<AssignList> data) {
		this.data = data;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public void setEmpId(Integer empId) {
		this.empId = empId;
	}
}
