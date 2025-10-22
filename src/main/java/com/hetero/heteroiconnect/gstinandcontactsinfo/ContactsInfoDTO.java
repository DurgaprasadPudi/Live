package com.hetero.heteroiconnect.gstinandcontactsinfo;

public class ContactsInfoDTO {

    private int sno;
    private String department;
    private String name;
    private String emailId;
    private String division;

    public ContactsInfoDTO() {}

    public ContactsInfoDTO(int sno, String department, String name, String emailId, String division) {
        this.sno = sno;
        this.department = department;
        this.name = name;
        this.emailId = emailId;
        this.division = division;
    }

    public int getSno() {
        return sno;
    }

    public void setSno(int sno) {
        this.sno = sno;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }
}
