package com.hetero.heteroiconnect.stationaryandhousekeepingtrack;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class SHKItems {
	@NotNull(message = "Item type must not be null")
	@Min(value = 1, message = "Item type must be a positive number")
	private Integer typeId;
	@NotNull(message = "Count must not be null")
	@Min(value = 1, message = "Count must be a positive number")
	private Integer count;
	private String description;

	public int getTypeId() {
		return typeId;
	}

	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
