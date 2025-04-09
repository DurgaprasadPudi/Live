package com.hetero.heteroiconnect.changepassword;

public class ResetPassword {
    private int employeeId;
    private int employeeCode;
    private String password;
    private int loginId;
    private int createdBy;
 

    public ResetPassword(int employeeId, int employeeCode, String password, 
                             int loginId, int createdBy) {
        this.employeeId = employeeId;
        this.employeeCode = employeeCode;
        this.password = password;
        this.loginId = loginId;
        this.createdBy = createdBy;

    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public int getEmployeeCode() {
        return employeeCode;
    }

    public void setEmployeeCode(int employeeCode) {
        this.employeeCode = employeeCode;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getLoginId() {
        return loginId;
    }

    public void setLoginId(int loginId) {
        this.loginId = loginId;
    }

    public int getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }

	public ResetPassword() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "LoginRegistration [employeeId=" + employeeId + ", employeeCode=" + employeeCode + ", password="
				+ password + ", loginId=" + loginId + ", createdBy=" + createdBy + "]";
	}

   

}
