package com.hetero.heteroiconnect.idcard;
public class EmployeeImageDTO {
    private String employeeId;
    private String addressId;
    private String bloodGroupId;
    private String employeeImagePath;

    // Getters and Setters
    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public String getBloodGroupId() {
        return bloodGroupId;
    }

    public void setBloodGroupId(String bloodGroupId) {
        this.bloodGroupId = bloodGroupId;
    }

    public String getEmployeeImagePath() {
        return employeeImagePath;
    }

    public void setEmployeeImagePath(String employeeImagePath) {
        this.employeeImagePath = employeeImagePath;
    }
}
