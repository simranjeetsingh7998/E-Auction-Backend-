package com.auction.address;

public enum AddressType {
   PERMANENT("PERMANENT"),CORRESPONDENCE("CORRESPONDENCE");
   
   private String code;

   private AddressType(String code) {
       this.code = code;
   }

   public String getCode() {
       return code;
   }
}
