package com.hetero.heteroiconnect.bankinformation;

public class EmployeeBankDTO {
	private int sno;
	private int empId;
	private String bankIfsc;
	private String bankAccount;
	private int bankId;

	public EmployeeBankDTO() {
		super();
	}

	public EmployeeBankDTO(int sno, int empId, String bankIfsc, String bankAccount, int bankId) {
		super();
		this.sno = sno;
		this.empId = empId;
		this.bankIfsc = bankIfsc;
		this.bankAccount = bankAccount;
		this.bankId = bankId;
	}

	public int getSno() {
		return sno;
	}

	public void setSno(int sno) {
		this.sno = sno;
	}

	public int getEmpId() {
		return empId;
	}

	public void setEmpId(int empId) {
		this.empId = empId;
	}

	public String getBankIfsc() {
		return bankIfsc;
	}

	public void setBankIfsc(String bankIfsc) {
		this.bankIfsc = bankIfsc;
	}

	public String getBankAccount() {
		return bankAccount;
	}

	public void setBankAccount(String bankAccount) {
		this.bankAccount = bankAccount;
	}

	public int getBankId() {
		return bankId;
	}

	public void setBankId(int bankId) {
		this.bankId = bankId;
	}

}
