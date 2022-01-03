package com.auction.emd.applied;

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
public class EMDAppliedForController {
	
	@Autowired
	private IEMDAppliedForService emdAppliedForService;
	
	@Autowired
	private ApiResponseMessageResolver messageResolver;
	
	@PostMapping("/emd/applied/for")
	public ResponseEntity<ApiResponse> create(@RequestBody @Valid EMDAppliedForVO emdAppliedForVO){
		  this.emdAppliedForService.addOrUpdate(emdAppliedForVO);
		  return new ResponseEntity<>(new ApiResponse(HttpStatus.CREATED.value(), 
				  this.messageResolver.getMessage("emd.applied.for.create") , null, null), HttpStatus.OK);
	}
	
	@GetMapping("/emd/applied/fors")
	public ResponseEntity<ApiResponse> activeAuctionTypes(){
		  return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(), 
				  this.messageResolver.getMessage("emd.applied.for.fetchs") , this.emdAppliedForService.findAllByIsActiveTrue(), null), HttpStatus.OK);
	}
	
	@GetMapping("/emd/applied/for/{id}")
	public ResponseEntity<ApiResponse> auctionTypeById(@PathVariable Integer id){
		  return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(), 
				  this.messageResolver.getMessage("emd.applied.for.fetch") , this.emdAppliedForService.findById(id), null), HttpStatus.OK);
	}
	
	@DeleteMapping("/emd/applied/for/{id}/deactivate")
	public ResponseEntity<ApiResponse> deactiveAuctionType(@PathVariable Integer id){
		  this.emdAppliedForService.deActivate(id);
		  return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(), 
				  this.messageResolver.getMessage("emd.applied.for.delete") ,null, null), HttpStatus.OK);
	}
	
	@PutMapping("/emd/applied/for/modify")
	public ResponseEntity<ApiResponse> modifyAuctionType(@RequestBody @Valid EMDAppliedForVO emdAppliedForVO){
		  this.emdAppliedForService.addOrUpdate(emdAppliedForVO);
		  return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(), 
				  this.messageResolver.getMessage("emd.applied.for.update") ,null, null), HttpStatus.OK);
	}

}
