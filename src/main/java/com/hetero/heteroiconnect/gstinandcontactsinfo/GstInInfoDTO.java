package com.hetero.heteroiconnect.gstinandcontactsinfo;

public class GstInInfoDTO {

    private int sno;
    private String gstinNumber;
    private String state;

    public GstInInfoDTO() {}

    public GstInInfoDTO(int sno, String gstinNumber, String state) {
        this.sno = sno;
        this.gstinNumber = gstinNumber;
        this.state = state;
    }

    public int getSno() {
        return sno;
    }

    public void setSno(int sno) {
        this.sno = sno;
    }

    public String getGstinNumber() {
        return gstinNumber;
    }

    public void setGstinNumber(String gstinNumber) {
        this.gstinNumber = gstinNumber;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
