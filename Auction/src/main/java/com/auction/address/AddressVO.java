package com.auction.address;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema
@Data
public class AddressVO {
	
    private Long id;
	
    @Schema(defaultValue = "abc", description = "enter address line 1 of user")
    @JsonProperty("address_line1")
    @NotBlank(message = "{address_line_1}")
	private String addressLine1;
	
    @Schema(defaultValue = "def", description = "enter address line 2 of user")
    @JsonProperty("address_line2")
	private String addressLine2;
	
    @Schema(defaultValue = "xyz", description = "enter address line 3 of user")
    @JsonProperty("address_line3")
	private String addressLine3;
	
    @Schema(defaultValue = "Haryana", description = "enter state of user")
    @NotBlank(message = "{state}")
	private String state;
	
    @Schema(defaultValue = "Sonipat", description = "enter district of user")
    @NotBlank(message = "{district}")
	private String district;
	
    @Schema(defaultValue = "Sonipat", description = "enter tehsil of user")
    @NotBlank(message = "{tehsil}")
	private String tehsil;
	
    @Schema(defaultValue = "456454", description = "enter pin code of user")
    @Size(min = 6, message = "{pin_code}")
	private Integer pinCode;
    
    @Schema(defaultValue = "CURRENT | CORRESPONDENCE", description = "enter address type of user")
    @JsonProperty("address_type")
    private AddressType addressType;
	
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
		address.setAddressType(this.addressType);
	return address;
   }

}
