package com.auction.bidder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.auction.api.response.ApiResponse;

@RestController
public class BidderTypeController {
	
	@Autowired
	private IBidderTypeService bidderTypeService;
	
	@PostMapping("/bidder/type")
	public ResponseEntity<ApiResponse> save(@RequestBody BidderTypeVO bidderTypeVO){
		this.bidderTypeService.saveBidderType(bidderTypeVO);
		return new ResponseEntity<>(new ApiResponse(HttpStatus.CREATED.value(), "Bidder Type created successfully",
				null, null), HttpStatus.OK);
	}
	
	@GetMapping("/active/bidder/types")
	public ResponseEntity<ApiResponse> activeBidderTypes(){
		return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(), "Bidder Types fetched successfully",
				this.bidderTypeService.findAllActiveBidderTypes(), null), HttpStatus.OK);
	}

	
	@GetMapping("/bidder/type/{id}")
	public ResponseEntity<ApiResponse> bidderTypeById(@PathVariable Integer id){
		return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(), "Bidder Type fetched successfully",
				this.bidderTypeService.findBidderTypeById(id), null), HttpStatus.OK);
	}
	
	@PutMapping("/bidder/type/update")
	public ResponseEntity<ApiResponse> update(@RequestBody BidderTypeVO bidderTypeVO){
		this.bidderTypeService.saveBidderType(bidderTypeVO);
		return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(), "Bidder Type updated successfully",
				null, null), HttpStatus.OK);
	}
}
