package com.hetero.heteroiconnect.hrassetrequests;

public class AssetItemDTO {
	private String asset;
	private Integer count;
	private String remarks;

	public AssetItemDTO(String asset, Integer count, String remarks) {
		this.asset = asset;
		this.count = count;
		this.remarks = remarks;
	}

	public String getAsset() {
		return asset;
	}

	public void setAsset(String asset) {
		this.asset = asset;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

}
