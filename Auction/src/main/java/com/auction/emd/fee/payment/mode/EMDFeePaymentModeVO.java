package com.auction.emd.fee.payment.mode;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class EMDFeePaymentModeVO {
	
	private Integer id;
	@Schema(defaultValue = "EMD Fee Payment Mode", description = "select EMD Fee Payment Mode")
	@NotBlank(message = "{emdfpMode.required}")
	@JsonProperty("emdfp_mode")
	private String emdfpMode;
	@JsonProperty("active")
	private boolean isActive;
	
    public EMDFeePaymentMode emdFeePaymentModeVOToEMDFeePaymentMode() {
    	EMDFeePaymentMode emdFeePaymentMode = new EMDFeePaymentMode();
    	emdFeePaymentMode.setActive(isActive);
    	emdFeePaymentMode.setEmdfpMode(emdfpMode);
    	emdFeePaymentMode.setId(id);
      return emdFeePaymentMode;	
    }

}
