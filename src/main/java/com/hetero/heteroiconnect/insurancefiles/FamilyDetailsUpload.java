package com.hetero.heteroiconnect.insurancefiles;

public class FamilyDetailsUpload {
	private Integer relation;
	private String fullname;
	private String gender;
	private String dob;
	private String age;
	private Integer flag;

	public FamilyDetailsUpload() {
		super();
	}

	public FamilyDetailsUpload(Integer relation, String fullname, String gender, String dob, String age, Integer flag) {
		super();
		this.relation = relation;
		this.fullname = fullname;
		this.gender = gender;
		this.dob = dob;
		this.age = age;
		this.flag = flag;
	}

	public Integer getRelation() {
		return relation;
	}

	public void setRelation(Integer relation) {
		this.relation = relation;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

}
