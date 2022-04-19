package com.auction.emd.fee.payment.mode;

public enum EmdFeePaymentModeEnum {
	
	ONLINE("online"), OFFLINE("offline"), NOTREQUIRED("Not Required");
	
	private String paymentMode;
	private EmdFeePaymentModeEnum(String paymentMode) {
		this.paymentMode = paymentMode;
	}
	
	public String getPaymentMode() {
		return paymentMode;
	}

}
