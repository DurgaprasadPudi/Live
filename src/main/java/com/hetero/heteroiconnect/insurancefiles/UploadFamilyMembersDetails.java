package com.hetero.heteroiconnect.insurancefiles;

import java.util.List;

public class UploadFamilyMembersDetails {
	private Integer employeeId;
	private String maritalStatus;
	private List<FamilyDetailsUpload> familyDetailsUpload;

	public UploadFamilyMembersDetails() {
		super();
	}

	public UploadFamilyMembersDetails(Integer employeeId, String maritalStatus,
			List<FamilyDetailsUpload> familyDetailsUpload) {
		super();
		this.employeeId = employeeId;
		this.maritalStatus = maritalStatus;
		this.familyDetailsUpload = familyDetailsUpload;
	}

	public Integer getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Integer employeeId) {
		this.employeeId = employeeId;
	}

	public String getMaritalStatus() {
		return maritalStatus;
	}

	public void setMaritalStatus(String maritalStatus) {
		this.maritalStatus = maritalStatus;
	}

	public List<FamilyDetailsUpload> getFamilyDetailsUpload() {
		return familyDetailsUpload;
	}

	public void setFamilyDetailsUpload(List<FamilyDetailsUpload> familyDetailsUpload) {
		this.familyDetailsUpload = familyDetailsUpload;
	}

}
