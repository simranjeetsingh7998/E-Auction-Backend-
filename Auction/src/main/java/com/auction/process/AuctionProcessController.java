package com.auction.process;

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
public class AuctionProcessController {
	
	@Autowired
	private IAuctionProcessService auctionProcessService;
	
	@Autowired
	private ApiResponseMessageResolver messageResolver;
	
	@PostMapping("/auction/process")
	public ResponseEntity<ApiResponse> create(@RequestBody @Valid AuctionProcessVO auctionProcessVO){
		  this.auctionProcessService.addOrUpdate(auctionProcessVO);
		  return new ResponseEntity<>(new ApiResponse(HttpStatus.CREATED.value(), 
				  this.messageResolver.getMessage("auction.process.create") , null, null), HttpStatus.OK);
	}
	
	@GetMapping("/auction/processes")
	public ResponseEntity<ApiResponse> activeAuctionProcesss(){
		  return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(), 
				  this.messageResolver.getMessage("auction.process.fetchs") , this.auctionProcessService.findAllByIsActiveTrue(), null), HttpStatus.OK);
	}
	
	@GetMapping("/auction/process/{id}")
	public ResponseEntity<ApiResponse> auctionProcessById(@PathVariable Integer id){
		  return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(), 
				  this.messageResolver.getMessage("auction.process.fetch") , this.auctionProcessService.findById(id), null), HttpStatus.OK);
	}
	
	@DeleteMapping("/auction/process/{id}/deactivate")
	public ResponseEntity<ApiResponse> deactiveAuctionProcess(@PathVariable Integer id){
		  this.auctionProcessService.deActivate(id);
		  return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(), 
				  this.messageResolver.getMessage("auction.process.delete") ,null, null), HttpStatus.OK);
	}
	
	@PutMapping("/auction/process/modify")
	public ResponseEntity<ApiResponse> modifyAuctionProcess(@RequestBody @Valid AuctionProcessVO auctionProcessVO){
		  this.auctionProcessService.addOrUpdate(auctionProcessVO);
		  return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(), 
				  this.messageResolver.getMessage("auction.process.update") ,null, null), HttpStatus.OK);
	}

}
