package com.hetero.heteroiconnect.insurancefiles;

public class InsuranceFileDTO {
	private String employeeId;
	private String fileName;
	private byte[] fileContent;
	private String empName;
	private String buName;
	private String costCenterName;
	private String doj;
	private String familyFileName;
	private byte[] familyFileContent;

	public InsuranceFileDTO(String employeeId, String fileName, byte[] fileContent, String empName, String buName,
			String costCenterName, String doj, String familyFileName, byte[] familyFileContent) {
		super();
		this.employeeId = employeeId;
		this.fileName = fileName;
		this.fileContent = fileContent;
		this.empName = empName;
		this.buName = buName;
		this.costCenterName = costCenterName;
		this.doj = doj;
		this.familyFileName = familyFileName;
		this.familyFileContent = familyFileContent;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public byte[] getFileContent() {
		return fileContent;
	}

	public void setFileContent(byte[] fileContent) {
		this.fileContent = fileContent;
	}

	public String getEmpName() {
		return empName;
	}

	public void setEmpName(String empName) {
		this.empName = empName;
	}

	public String getBuName() {
		return buName;
	}

	public void setBuName(String buName) {
		this.buName = buName;
	}

	public String getCostCenterName() {
		return costCenterName;
	}

	public void setCostCenterName(String costCenterName) {
		this.costCenterName = costCenterName;
	}

	public String getDoj() {
		return doj;
	}

	public void setDoj(String doj) {
		this.doj = doj;
	}

	public String getFamilyFileName() {
		return familyFileName;
	}

	public void setFamilyFileName(String familyFileName) {
		this.familyFileName = familyFileName;
	}

	public byte[] getFamilyFileContent() {
		return familyFileContent;
	}

	public void setFamilyFileContent(byte[] familyFileContent) {
		this.familyFileContent = familyFileContent;
	}

}