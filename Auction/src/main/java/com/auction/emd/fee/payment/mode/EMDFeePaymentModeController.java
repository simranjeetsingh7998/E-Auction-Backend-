package com.auction.emd.fee.payment.mode;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.auction.ApiResponseMessageResolver;
import com.auction.api.response.ApiResponse;

@RestController
public class EMDFeePaymentModeController {
	
	@Autowired
	private IEMDFeePaymentModeService emdFeePaymentModeService;
	
	@Autowired
	private ApiResponseMessageResolver messageResolver;
	
	@PostMapping("/emd/fee/payment/mode")
	public ResponseEntity<ApiResponse> create(@RequestBody @Valid EMDFeePaymentModeVO emdFeePaymentModeVO){
		  this.emdFeePaymentModeService.addOrUpdate(emdFeePaymentModeVO);
		  return new ResponseEntity<>(new ApiResponse(HttpStatus.CREATED.value(), 
				  this.messageResolver.getMessage("emd.fee.payment.mode.create") , null, null), HttpStatus.OK);
	}
	
	@GetMapping("/emd/fee/payment/modes")
	public ResponseEntity<ApiResponse> activeEMDFeePaymentModes(){
		  return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(), 
				  this.messageResolver.getMessage("emd.fee.payment.mode.fetchs") , this.emdFeePaymentModeService.findAllByIsActiveTrue(), null), HttpStatus.OK);
	}
	
	@GetMapping("/emd/fee/payment/mode/{id}")
	public ResponseEntity<ApiResponse> emdFeePaymentModeById(@PathVariable Integer id){
		  return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(), 
				  this.messageResolver.getMessage("emd.fee.payment.mode.fetch") , this.emdFeePaymentModeService.findById(id), null), HttpStatus.OK);
	}
	
	@DeleteMapping("/emd/fee/payment/mode/{id}/deactivate")
	public ResponseEntity<ApiResponse> deactiveEMDFeePaymentMode(@PathVariable Integer id){
		  this.emdFeePaymentModeService.deActivate(id);
		  return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(), 
				  this.messageResolver.getMessage("emd.fee.payment.mode.delete") ,null, null), HttpStatus.OK);
	}
	
	@PutMapping("/emd/fee/payment/mode/modify")
	public ResponseEntity<ApiResponse> modifyEMDFeePaymentMode(@RequestBody @Valid EMDFeePaymentModeVO emdFeePaymentModeVO){
		  this.emdFeePaymentModeService.addOrUpdate(emdFeePaymentModeVO);
		  return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(), 
				  this.messageResolver.getMessage("emd.fee.payment.mode.update") ,null, null), HttpStatus.OK);
	}

}
