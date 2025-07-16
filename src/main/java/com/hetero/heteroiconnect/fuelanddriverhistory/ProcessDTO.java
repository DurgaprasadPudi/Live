package com.hetero.heteroiconnect.fuelanddriverhistory;

public class ProcessDTO {
	private boolean receivedFlag;
	private String processedBy;
	private Integer fuelDriverId;

	public ProcessDTO() {
		super();
	}

	public ProcessDTO(boolean receivedFlag, String processedBy, Integer fuelDriverId) {
		super();
		this.receivedFlag = receivedFlag;
		this.processedBy = processedBy;
		this.fuelDriverId = fuelDriverId;
	}

	public boolean isReceivedFlag() {
		return receivedFlag;
	}

	public void setReceivedFlag(boolean receivedFlag) {
		this.receivedFlag = receivedFlag;
	}

	public String getProcessedBy() {
		return processedBy;
	}

	public void setProcessedBy(String processedBy) {
		this.processedBy = processedBy;
	}

	public Integer getFuelDriverId() {
		return fuelDriverId;
	}

	public void setFuelDriverId(Integer fuelDriverId) {
		this.fuelDriverId = fuelDriverId;
	}

}