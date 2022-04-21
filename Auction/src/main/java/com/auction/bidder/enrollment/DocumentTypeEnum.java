package com.auction.bidder.enrollment;

public enum DocumentTypeEnum {
	
	ADDRESSPROOF("addressproof"),TRANSACTIONPROOF("transactionproof");
	private String documentType;
	private DocumentTypeEnum(String documentType) {
		this.documentType = documentType;
	}
	
	public String getDocumentType() {
		return documentType;
	}

}
