package com.auction.bidder.enrollment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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
	private IJointHolderService jointHolderService;
	
	@Autowired
	private ApiResponseMessageResolver messageResolver;
	
	@PostMapping("/bidder/auction/enrollment")
	public ResponseEntity<ApiResponse> bidderAuctionEnrollment(@RequestBody BidderAuctionEnrollmentVO bidderAuctionEnrollmentVO){
		return new ResponseEntity<>(
				new ApiResponse(HttpStatus.CREATED.value(), this.messageResolver.getMessage("bidder.auction.enrollment.create"),
						this.auctionEnrollmentService.save(bidderAuctionEnrollmentVO), null), HttpStatus.OK);
	}
	
	@PostMapping("/bidder/auction/enrollment/{id}/joint/holder")
	public ResponseEntity<ApiResponse> addJointHolder(
			@PathVariable("id") Long bidderAuctionEnrollmentId,
			@RequestBody JointHolderVO jointHolderVO){
		return new ResponseEntity<>(
				new ApiResponse(HttpStatus.OK.value(), this.messageResolver.getMessage(
						jointHolderVO.getId() == 0 ? "joint.holder.create" : "joint.holder.update"),
						this.jointHolderService.save(bidderAuctionEnrollmentId, jointHolderVO), null), HttpStatus.OK);
	}
	
	@GetMapping("/bidder/auction/enrollment/{id}/joint/holders")
	public ResponseEntity<ApiResponse> findJointHolderByBidderAuctionEnrollment(@PathVariable("id") Long bidderAuctionEnrollmentId){
		return new ResponseEntity<>(
				new ApiResponse(HttpStatus.OK.value(), this.messageResolver.getMessage("joint.holder.fetches"),
						this.jointHolderService.findAllByBidderAuctionEnrollmentId(bidderAuctionEnrollmentId), null), HttpStatus.OK);
	}
	
	@DeleteMapping("/joint/holder/{id}/delete")
	public ResponseEntity<ApiResponse> deleteJointHolder(@PathVariable("id") Long jointHolderId){
		    this.jointHolderService.delete(jointHolderId);
		return new ResponseEntity<>(
				new ApiResponse(HttpStatus.OK.value(), this.messageResolver.getMessage("joint.holder.delete"),
						null, null), HttpStatus.OK);
	}
	
	@GetMapping("/auction/{id}/bidder/enrollments")
	public ResponseEntity<ApiResponse> bidderEnrollmentsByAuctionId(@PathVariable("id") Long auctionId){
		return new ResponseEntity<>(
				new ApiResponse(HttpStatus.OK.value(), this.messageResolver.getMessage("bidder.auction.enrollment.fetchs"), 
						this.auctionEnrollmentService.findAllByAuctionPreparation(auctionId), null), HttpStatus.OK);
	}

}
