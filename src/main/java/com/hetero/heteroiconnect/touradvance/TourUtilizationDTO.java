package com.hetero.heteroiconnect.touradvance;
public class TourUtilizationDTO {

    private Integer tourId;
    private Double utilizedAmount;
    private Double adjustment;
    private int modeOfSettlement; 
    private String comment;
    private String uploadedBy;


    public Integer getTourId() {
        return tourId;
    }

    public void setTourId(Integer tourId) {
        this.tourId = tourId;
    }

    public Double getUtilizedAmount() {
        return utilizedAmount;
    }

    public void setUtilizedAmount(Double utilizedAmount) {
        this.utilizedAmount = utilizedAmount;
    }

    public Double getAdjustment() {
        return adjustment;
    }

    public void setAdjustment(Double adjustment) {
        this.adjustment = adjustment;
    }


    public int getModeOfSettlement() {
		return modeOfSettlement;
	}

	public void setModeOfSettlement(int modeOfSettlement) {
		this.modeOfSettlement = modeOfSettlement;
	}

	public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getUploadedBy() {
        return uploadedBy;
    }

    public void setUploadedBy(String uploadedBy) {
        this.uploadedBy = uploadedBy;
    }
}
