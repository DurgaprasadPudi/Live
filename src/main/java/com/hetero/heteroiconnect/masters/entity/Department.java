package com.hetero.heteroiconnect.masters.entity;
public class Department {
    private int businessunitid;
    private String departmentid;
    private int status;
    private String sectionid;
    private int createdBy;
   

    // Getters and setters
    public int getBusinessunitid() {
        return businessunitid;
    }

    public void setBusinessunitid(int businessunitid) {
        this.businessunitid = businessunitid;
    }

    public String getDepartmentid() {
        return departmentid;
    }

    public void setDepartmentid(String departmentid) {
        this.departmentid = departmentid;
    }

    public int getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
	}

	public String getSectionid() {
		return sectionid;
	}

	public void setSectionid(String sectionid) {
		this.sectionid = sectionid;
	}

	public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
