package com.hetero.heteroiconnect.insurancefiles;

public class FamilyInsuranceDetailsDTO {
	private Integer id;
	private Integer employeeId;
	private String relation;
	private String fullname;
	private String gender;
	private String dob;
	private String age;
	private Integer flag;

	public FamilyInsuranceDetailsDTO() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Integer employeeId) {
		this.employeeId = employeeId;
	}

	public String getRelation() {
		return relation;
	}

	public void setRelation(String relation) {
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

	public FamilyInsuranceDetailsDTO(Integer id, Integer employeeId, String relation, String fullname, String gender,
			String dob, String age, Integer flag) {
		super();
		this.id = id;
		this.employeeId = employeeId;
		this.relation = relation;
		this.fullname = fullname;
		this.gender = gender;
		this.dob = dob;
		this.age = age;
		this.flag = flag;
	}

}
