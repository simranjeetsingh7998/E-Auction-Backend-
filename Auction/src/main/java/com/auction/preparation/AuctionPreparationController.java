package com.auction.preparation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.auction.api.response.ApiResponse;

@RestController
public class AuctionPreparationController {
	
	@Autowired
	private IAuctionPreparationService auctionPreparationService;
	
	@PostMapping("/auction/preparation")
	public ResponseEntity<ApiResponse> create(@RequestBody AuctionPreparationVO auctionPreparationVO){
		 this.auctionPreparationService.save(auctionPreparationVO);
		return new ResponseEntity<>(new ApiResponse(HttpStatus.CREATED.value(), "Auction prepared successfully",
				null, null), HttpStatus.OK);
	}

}
