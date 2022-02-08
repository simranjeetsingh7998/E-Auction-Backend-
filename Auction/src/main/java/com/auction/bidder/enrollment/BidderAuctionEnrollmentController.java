package com.auction.bidder.enrollment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.auction.ApiResponseMessageResolver;
import com.auction.api.response.ApiResponse;

@RestController
public class BidderAuctionEnrollmentController {
	
	@Autowired
	private IBidderAuctionEnrollmentService auctionEnrollmentService;
	
	@Autowired
	private ApiResponseMessageResolver messageResolver;
	
	@PostMapping("/bidder/auction/enrollment")
	public ResponseEntity<ApiResponse> bidderAuctionEnrollment(@RequestBody BidderAuctionEnrollmentVO bidderAuctionEnrollmentVO){
		this.auctionEnrollmentService.save(bidderAuctionEnrollmentVO);
		return new ResponseEntity<>(
				new ApiResponse(HttpStatus.CREATED.value(), this.messageResolver.getMessage("bidder.auction.enrollment.create"),
						null, null), HttpStatus.OK);
	}
	
	@GetMapping("/auction/{id}/bidder/enrollments")
	public ResponseEntity<ApiResponse> bidderEnrollmentsByAuctionId(@PathVariable("id") Long auctionId){
		return new ResponseEntity<>(
				new ApiResponse(HttpStatus.OK.value(), this.messageResolver.getMessage("bidder.auction.enrollment.fetchs"), 
						this.auctionEnrollmentService.findAllByAuctionPreparation(auctionId), null), HttpStatus.OK);
	}

}
