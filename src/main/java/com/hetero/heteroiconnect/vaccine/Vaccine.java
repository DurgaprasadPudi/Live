package com.hetero.heteroiconnect.vaccine;

public class Vaccine {
	 
	private String empcode; 
	private String vaccineid;
	private String first_dose;
	private String first_dose_date;
	private String second_dose;
	private String second_dose_date;
	private String createdby;
	public String getEmpcode() {
		return empcode;
	}
	public void setEmpcode(String empcode) {
		this.empcode = empcode;
	}
	public String getVaccineid() {
		return vaccineid;
	}
	public void setVaccineid(String vaccineid) {
		this.vaccineid = vaccineid;
	}
	public String getFirst_dose() {
		return first_dose;
	}
	public void setFirst_dose(String first_dose) {
		this.first_dose = first_dose;
	}
	public String getFirst_dose_date() {
		return first_dose_date;
	}
	public void setFirst_dose_date(String first_dose_date) {
		this.first_dose_date = first_dose_date;
	}
	public String getSecond_dose() {
		return second_dose;
	}
	public void setSecond_dose(String second_dose) {
		this.second_dose = second_dose;
	}
	public String getSecond_dose_date() {
		return second_dose_date;
	}
	public void setSecond_dose_date(String second_dose_date) {
		this.second_dose_date = second_dose_date;
	}
	public String getCreatedby() {
		return createdby;
	}
	public void setCreatedby(String createdby) {
		this.createdby = createdby;
	}
	@Override
	public String toString() {
		return "Vaccine [empcode=" + empcode + ", vaccineid=" + vaccineid + ", first_dose=" + first_dose
				+ ", first_dose_date=" + first_dose_date + ", second_dose=" + second_dose + ", second_dose_date="
				+ second_dose_date + ", createdby=" + createdby + "]";
	}
	 
}
