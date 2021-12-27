package com.auction.address;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema
@Data
public class AddressVO {
	
    private Long id;
	
    @Schema(defaultValue = "abc", description = "enter address line 1 of user")
    @JsonProperty("address_line1")
	private String addressLine1;
	
    @Schema(defaultValue = "def", description = "enter address line 2 of user")
    @JsonProperty("address_line2")
	private String addressLine2;
	
    @Schema(defaultValue = "xyz", description = "enter address line 3 of user")
    @JsonProperty("address_line3")
	private String addressLine3;
	
    @Schema(defaultValue = "Haryana", description = "enter state of user")
	private String state;
	
    @Schema(defaultValue = "Sonipat", description = "enter district of user")
	private String district;
	
    @Schema(defaultValue = "Sonipat", description = "enter tehsil of user")
	private String tehsil;
	
    @Schema(defaultValue = "456454", description = "enter pin code of user")
	private Integer pinCode;
	
	public Address addressVOToAddress() {
		Address address = new Address();
	  if(this.id!=0) {
		address.setId(id);
	  }
		address.setAddressLine1(addressLine1);
		address.setAddressLine2(addressLine2);
		address.setAddressLine3(addressLine3);
		address.setDistrict(district);
		address.setPinCode(pinCode);
		address.setState(state);
		address.setTehsil(tehsil);
	return address;
   }

}
