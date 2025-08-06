package com.hetero.heteroiconnect.insurancefiles;

public class PremiumInfoDTO {
	private Integer premiumInfoId;
	private String sumInsurance;
	private String grossPremium;
	private String interestFlag;

	public PremiumInfoDTO() {
		super();
	}

	public PremiumInfoDTO(Integer premiumInfoId, String sumInsurance, String grossPremium, String interestFlag) {
		super();
		this.premiumInfoId = premiumInfoId;
		this.sumInsurance = sumInsurance;
		this.grossPremium = grossPremium;
		this.interestFlag = interestFlag;
	}

	public Integer getPremiumInfoId() {
		return premiumInfoId;
	}

	public void setPremiumInfoId(Integer premiumInfoId) {
		this.premiumInfoId = premiumInfoId;
	}

	public String getSumInsurance() {
		return sumInsurance;
	}

	public void setSumInsurance(String sumInsurance) {
		this.sumInsurance = sumInsurance;
	}

	public String getGrossPremium() {
		return grossPremium;
	}

	public void setGrossPremium(String grossPremium) {
		this.grossPremium = grossPremium;
	}

	public String getInterestFlag() {
		return interestFlag;
	}

	public void setInterestFlag(String interestFlag) {
		this.interestFlag = interestFlag;
	}

}
