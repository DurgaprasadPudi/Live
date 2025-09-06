package com.hetero.heteroiconnect.touradvance;

public class ConveyanceBillDTO {
	private byte[] billFile;
	private byte[] chequeFile;

	public byte[] getBillFile() {
		return billFile;
	}

	public void setBillFile(byte[] billFile) {
		this.billFile = billFile;
	}

	public byte[] getChequeFile() {
		return chequeFile;
	}

	public void setChequeFile(byte[] chequeFile) {
		this.chequeFile = chequeFile;
	}
}
