package com.auction.category;

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
public class AuctionCategoryController {
	
	@Autowired
	private IAuctionCategoryService auctionCategoryService;
	
	@Autowired
	private ApiResponseMessageResolver messageResolver;
	
	@PostMapping("/auction/category")
	public ResponseEntity<ApiResponse> create(@RequestBody @Valid AuctionCategoryVO auctionCategoryVO){
		  this.auctionCategoryService.addOrUpdate(auctionCategoryVO);
		  return new ResponseEntity<>(new ApiResponse(HttpStatus.CREATED.value(), 
				  this.messageResolver.getMessage("auction.category.create") , null, null), HttpStatus.OK);
	}
	
	@GetMapping("/active/auction/categories")
	public ResponseEntity<ApiResponse> activeAuctionCategories(){
		  return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(), 
				  this.messageResolver.getMessage("auction.category.fetchs") , this.auctionCategoryService.findAllByIsActiveTrue(), null), HttpStatus.OK);
	}
	
	@GetMapping("/auction/category/{id}")
	public ResponseEntity<ApiResponse> auctionCategoryById(@PathVariable Integer id){
		  return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(), 
				  this.messageResolver.getMessage("auction.category.fetch") , this.auctionCategoryService.findById(id), null), HttpStatus.OK);
	}
	
	@DeleteMapping("/auction/category/{id}/deactivate")
	public ResponseEntity<ApiResponse> deactiveAuctionMethod(@PathVariable Integer id){
		  this.auctionCategoryService.deActivate(id);
		  return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(), 
				  this.messageResolver.getMessage("auction.category.delete") ,null, null), HttpStatus.OK);
	}
	
	@PutMapping("/auction/category/modify")
	public ResponseEntity<ApiResponse> modifyAuctionMethod(@RequestBody @Valid AuctionCategoryVO auctionCategoryVO){
		  this.auctionCategoryService.addOrUpdate(auctionCategoryVO);
		  return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(), 
				  this.messageResolver.getMessage("auction.category.update") ,null, null), HttpStatus.OK);
	}


}
