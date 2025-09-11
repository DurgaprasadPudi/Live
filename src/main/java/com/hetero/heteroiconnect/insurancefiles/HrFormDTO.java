package com.hetero.heteroiconnect.insurancefiles;

public class HrFormDTO {
	private Integer buId;
	private String fileName;
	private byte[] data;

	public HrFormDTO(Integer buId, String fileName, byte[] data) {
		super();
		this.buId = buId;
		this.fileName = fileName;
		this.data = data;
	}

	public Integer getBuId() {
		return buId;
	}

	public void setBuId(Integer buId) {
		this.buId = buId;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}
}