package com.auction.method;

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
public class AuctionMethodController {
	
	@Autowired
	private IAuctionMethodService auctionMethodService;
	
	@Autowired
	private ApiResponseMessageResolver messageResolver;
	
	@PostMapping("/auction/method")
	public ResponseEntity<ApiResponse> create(@RequestBody @Valid AuctionMethodVO auctionMethodVO){
		  this.auctionMethodService.addOrUpdate(auctionMethodVO);
		  return new ResponseEntity<>(new ApiResponse(HttpStatus.CREATED.value(), 
				  this.messageResolver.getMessage("auction.method.create") , null, null), HttpStatus.OK);
	}
	
	@GetMapping("/auction/methods")
	public ResponseEntity<ApiResponse> activeAuctionMethods(){
		  return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(), 
				  this.messageResolver.getMessage("auction.method.fetchs") , this.auctionMethodService.findAllByIsActiveTrue(), null), HttpStatus.OK);
	}
	
	@GetMapping("/auction/method/{id}")
	public ResponseEntity<ApiResponse> auctionMethodById(@PathVariable Integer id){
		  return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(), 
				  this.messageResolver.getMessage("auction.method.fetch") , this.auctionMethodService.findById(id), null), HttpStatus.OK);
	}
	
	@DeleteMapping("/auction/method/{id}/deactivate")
	public ResponseEntity<ApiResponse> deactiveAuctionMethod(@PathVariable Integer id){
		  this.auctionMethodService.deActivate(id);
		  return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(), 
				  this.messageResolver.getMessage("auction.method.delete") ,null, null), HttpStatus.OK);
	}
	
	@PutMapping("/auction/method/modify")
	public ResponseEntity<ApiResponse> modifyAuctionMethod(@RequestBody @Valid AuctionMethodVO auctionMethodVO){
		  this.auctionMethodService.addOrUpdate(auctionMethodVO);
		  return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(), 
				  this.messageResolver.getMessage("auction.method.update") ,null, null), HttpStatus.OK);
	}


}
