package com.hetero.heteroiconnect.zonedetails;

public class ZoneEmployeeDTO {
	private String id;
	private String employeeId;
	private int cityId;
	private int locationId;
	private int companyId;
	private int buildingId;
	private int floorId;
	private int zoneId;
	private String section;
	private String subSection;

	private String createdby;
	private String updateby;
	public ZoneEmployeeDTO() {
	}

	public ZoneEmployeeDTO(String id,String employeeId, int cityId, int locationId,int companyId, int buildingId, int floorId, int zoneId,
			String section, String subSection, String createdby,String updateby) {
		super();
		this.id=id;
		this.employeeId = employeeId;
		this.cityId = cityId;
		this.locationId = locationId;
		this.companyId = companyId;
		this.buildingId = buildingId;
		this.floorId = floorId;
		this.zoneId = zoneId;
		this.section = section;
		this.subSection = subSection;
		this.createdby = createdby;
		this.updateby=updateby;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public int getCityId() {
		return cityId;
	}

	public void setCityId(int cityId) {
		this.cityId = cityId;
	}

	public int getLocationId() {
		return locationId;
	}

	public void setLocationId(int locationId) {
		this.locationId = locationId;
	}
	

	public int getCompanyId() {
		return companyId;
	}

	public void setCompanyId(int companyId) {
		this.companyId = companyId;
	}

	public int getBuildingId() {
		return buildingId;
	}

	public void setBuildingId(int buildingId) {
		this.buildingId = buildingId;
	}

	public int getFloorId() {
		return floorId;
	}

	public void setFloorId(int floorId) {
		this.floorId = floorId;
	}

	public int getZoneId() {
		return zoneId;
	}

	public void setZoneId(int zoneId) {
		this.zoneId = zoneId;
	}

	public String getSection() {
		return section;
	}

	public void setSection(String section) {
		this.section = section;
	}

	public String getSubSection() {
		return subSection;
	}

	public void setSubSection(String subSection) {
		this.subSection = subSection;
	}

	public String getCreatedby() {
		return createdby;
	}

	public void setCreatedby(String createdby) {
		this.createdby = createdby;
	}

	public String getUpdateby() {
		return updateby;
	}

	public void setUpdateby(String updateby) {
		this.updateby = updateby;
	}
	

}
