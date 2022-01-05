package com.auction.type;

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
public class AuctionTypeController {
	
	@Autowired
	private IAuctionTypeService auctionTypeService;
	
	@Autowired
	private ApiResponseMessageResolver messageResolver;
	
	@PostMapping("/auction/type")
	public ResponseEntity<ApiResponse> create(@RequestBody @Valid AuctionTypeVO auctionTypeVO){
		  this.auctionTypeService.addOrUpdate(auctionTypeVO);
		  return new ResponseEntity<>(new ApiResponse(HttpStatus.CREATED.value(), 
				  this.messageResolver.getMessage("auction.type.create") , null, null), HttpStatus.OK);
	}
	
	@GetMapping("/auction/types")
	public ResponseEntity<ApiResponse> activeAuctionTypes(){
		  return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(), 
				  this.messageResolver.getMessage("auction.type.fetchs") , this.auctionTypeService.findAllByIsActiveTrue(), null), HttpStatus.OK);
	}
	
	@GetMapping("/auction/type/{id}")
	public ResponseEntity<ApiResponse> auctionTypeById(@PathVariable Integer id){
		  return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(), 
				  this.messageResolver.getMessage("auction.type.fetch") , this.auctionTypeService.findById(id), null), HttpStatus.OK);
	}
	
	@DeleteMapping("/auction/type/{id}/deactivate")
	public ResponseEntity<ApiResponse> deactiveAuctionType(@PathVariable Integer id){
		  this.auctionTypeService.deActivate(id);
		  return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(), 
				  this.messageResolver.getMessage("auction.type.delete") ,null, null), HttpStatus.OK);
	}
	
	@PutMapping("/auction/type/modify")
	public ResponseEntity<ApiResponse> modifyAuctionType(@RequestBody @Valid AuctionTypeVO auctionTypeVO){
		  this.auctionTypeService.addOrUpdate(auctionTypeVO);
		  return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(), 
				  this.messageResolver.getMessage("auction.type.update") ,null, null), HttpStatus.OK);
	}

}
