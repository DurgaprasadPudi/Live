package com.hetero.heteroiconnect.stationaryandhousekeepingtrack;

import java.util.List;

public class SHKRaiseRequest {
	private int raisedBy;
	private List<SHKItems> items;

	public int getRaisedBy() {
		return raisedBy;
	}

	public void setRaisedBy(int raisedBy) {
		this.raisedBy = raisedBy;
	}

	public List<SHKItems> getItems() {
		return items;
	}

	public void setItems(List<SHKItems> items) {
		this.items = items;
	}
}
