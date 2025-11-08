package com.hetero.heteroiconnect.bankinformation;

public class BankDTO {
	private int sno;
	private String employeeCode;
	private String name;
	private String ifscCode;
	private String accountNumber;
	private int type;
	private String employeeId;
	private int insertedStatus;

	public int getInsertedStatus() {
		return insertedStatus;
	}

	public void setInsertedStatus(int insertedStatus) {
		this.insertedStatus = insertedStatus;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public int getSno() {
		return sno;
	}

	public void setSno(int sno) {
		this.sno = sno;
	}

	public String getEmployeeCode() {
		return employeeCode;
	}

	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIfscCode() {
		return ifscCode;
	}

	public void setIfscCode(String ifscCode) {
		this.ifscCode = ifscCode;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public BankDTO() {
		super();
	}

	public BankDTO(int sno, String employeeCode, String name, String ifscCode, String accountNumber, int type,
			String employeeId, int insertedStatus) {
		super();
		this.sno = sno;
		this.employeeCode = employeeCode;
		this.name = name;
		this.ifscCode = ifscCode;
		this.accountNumber = accountNumber;
		this.type = type;
		this.employeeId = employeeId;
		this.insertedStatus = insertedStatus;
	}

}
