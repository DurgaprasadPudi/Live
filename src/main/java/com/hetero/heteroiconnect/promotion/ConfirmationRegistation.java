package com.hetero.heteroiconnect.promotion;

import java.sql.Date;

public class ConfirmationRegistation {
	private int employeeid;
	private int designationid;
	private int departmentid;
	private int sectionid;
	private int employmenttypeid;
	private Date confirmationdate;
	private String comments;

	public int getEmployeeid() {
		return employeeid;
	}

	public void setEmployeeid(int employeeid) {
		this.employeeid = employeeid;
	}

	public int getDesignationid() {
		return designationid;
	}

	public void setDesignationid(int designationid) {
		this.designationid = designationid;
	}

	public int getDepartmentid() {
		return departmentid;
	}

	public void setDepartmentid(int departmentid) {
		this.departmentid = departmentid;
	}

	public int getSectionid() {
		return sectionid;
	}

	public void setSectionid(int sectionid) {
		this.sectionid = sectionid;
	}

	public int getEmploymenttypeid() {
		return employmenttypeid;
	}

	public void setEmploymenttypeid(int employmenttypeid) {
		this.employmenttypeid = employmenttypeid;
	}

	public Date getConfirmationdate() {
		return confirmationdate;
	}

	public void setConfirmationdate(Date confirmationdate) {
		this.confirmationdate = confirmationdate;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

}
