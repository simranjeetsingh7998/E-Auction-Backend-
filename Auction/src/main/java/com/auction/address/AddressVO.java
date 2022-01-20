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
    @JsonProperty("address")
    @NotBlank(message = "{address_line_1}")
	private String addressArea;
	
    @Schema(defaultValue = "Haryana", description = "enter state of user")
    @NotBlank(message = "{state}")
	private String state;
	
    @Schema(defaultValue = "Sonipat", description = "enter district of user")
    @NotBlank(message = "{district}")
	private String district;
	
    @Schema(defaultValue = "Sonipat", description = "enter tehsil of user")
    @NotBlank(message = "{tehsil}")
	private String tehsil;
	
    @Schema(defaultValue = "456454", description = "enter zip code of user")
    @Size(min = 6, message = "{zip_code}")
	private Integer pinCode;
    
    @Schema(defaultValue = "PERMANENT | CORRESPONDENCE", description = "enter address type of user")
    @JsonProperty("address_type")
    private AddressType addressType;
	
	public Address addressVOToAddress() {
		Address address = new Address();
	    if(this.id!=0) {
		  address.setId(id);
	    }
		address.setAddressArea(addressArea);
		address.setDistrict(district);
		address.setPinCode(pinCode);
		address.setState(state);
		address.setTehsil(tehsil);
		address.setAddressType(this.addressType);
	return address;
   }

}
