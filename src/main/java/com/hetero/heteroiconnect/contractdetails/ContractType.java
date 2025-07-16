package com.hetero.heteroiconnect.contractdetails;

public class ContractType {
	private int contractTypeId;
	private String contractTypeName;
	private String wage;

	public ContractType() {
		super();
	}

	public ContractType(int contractTypeId, String contractTypeName, String wage) {
		super();
		this.contractTypeId = contractTypeId;
		this.contractTypeName = contractTypeName;
		this.wage = wage;
	}

	public int getContractTypeId() {
		return contractTypeId;
	}

	public void setContractTypeId(int contractTypeId) {
		this.contractTypeId = contractTypeId;
	}

	public String getContractTypeName() {
		return contractTypeName;
	}

	public void setContractTypeName(String contractTypeName) {
		this.contractTypeName = contractTypeName;
	}

	public String getWage() {
		return wage;
	}

	public void setWage(String wage) {
		this.wage = wage;
	}
}
