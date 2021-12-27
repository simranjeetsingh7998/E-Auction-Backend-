package com.auction.bidder.category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.auction.api.response.ApiResponse;

@RestController
public class BidderCategoryController {
	
	@Autowired
	private IBidderCategoryService bidderCategoryService;
	
	@PostMapping("/bidder/category")
	public ResponseEntity<ApiResponse> save(@RequestBody BidderCategoryVO bidderCategoryVO){
		this.bidderCategoryService.save(bidderCategoryVO);
		return new ResponseEntity<>(new ApiResponse(HttpStatus.CREATED.value(), "Bidder Category created successfully",
				null, null), HttpStatus.OK);
	}
	
	@GetMapping("/bidder/categories/bidder/type/{bidderTypeId}")
	public ResponseEntity<ApiResponse> bidderCategoriesByBidderTypeId(@PathVariable Integer bidderTypeId){
		return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(), "Bidder Categories fetched successfully",
				this.bidderCategoryService.findByBidderTypeId(bidderTypeId), null), HttpStatus.OK);
	}
	
	@GetMapping("/bidder/categories")
	public ResponseEntity<ApiResponse> bidderCategories(){
		return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(), "Bidder Categories fetched successfully",
				this.bidderCategoryService.findAll(), null), HttpStatus.OK);
	}

}
