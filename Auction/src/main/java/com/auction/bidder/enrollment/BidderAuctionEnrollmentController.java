package com.auction.bidder.enrollment;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
	
	@PostMapping(value = "/auction/bidder/enrollment", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<ApiResponse> bidderAuctionEnrollment(@RequestPart("bidderAuctionEnrollment") String bidderAuctionEnrollmentVO,
			@RequestPart("file") MultipartFile document
			) throws IOException{
		BidderAuctionEnrollmentVO enrollmentVO = this.auctionEnrollmentService.save(bidderAuctionEnrollmentVO, document);
		return new ResponseEntity<>(
				new ApiResponse(HttpStatus.CREATED.value(), this.messageResolver.getMessage("bidder.auction.enrollment.create"),
						enrollmentVO, null), HttpStatus.OK);
	}
	
	@PostMapping(value = "/auction/bidder/enrollment/{id}/{documentType}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<ApiResponse> uploadBidderAuctionEnrollment(
            @PathVariable("id") Long auctionBidderEnrollmentId, 
            @PathVariable("documentType") String documentType,
			@RequestPart("file") MultipartFile document
			) throws IOException{
		this.auctionEnrollmentService.uploadBidderAuctionEnrollmentDocument(auctionBidderEnrollmentId, documentType, document);
		return new ResponseEntity<>(
				new ApiResponse(HttpStatus.CREATED.value(), this.messageResolver.getMessage("bidder.auction.enrollment.create"),
						null, null), HttpStatus.OK);
	}
	
	@GetMapping(value = "/auction/{auctionId}/bidder/{id}/enrollment/document/{documentType}")
	public ResponseEntity<ApiResponse> getBidderAuctionEnrollmentDocument( 
			@PathVariable("auctionId") Long auctionId,
			@PathVariable("id") Long bidderId, 
            @PathVariable("documentType") String documentType){
		return new ResponseEntity<>(
				new ApiResponse(HttpStatus.CREATED.value(), this.messageResolver.getMessage("bidder.auction.enrollment.document.fetch"),
						this.auctionEnrollmentService.getDocumentForBidder(auctionId, bidderId, documentType), null), HttpStatus.OK);
	}
	
	@PutMapping(value = "/auction/bidder/enrollment/{id}/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<ApiResponse> modifyBidderAuctionEnrollment(
			@PathVariable("id") Long bidderAuctionEnrollmentId,
			@RequestPart("bidderAuctionEnrollment") String bidderAuctionEnrollmentVO,
			@RequestPart(name =  "file", required = false) MultipartFile document
			) throws IOException{
		BidderAuctionEnrollmentVO enrollmentVO = this.auctionEnrollmentService.save(bidderAuctionEnrollmentVO, document);
		return new ResponseEntity<>(
				new ApiResponse(HttpStatus.CREATED.value(), this.messageResolver.getMessage("bidder.auction.enrollment.create"),
						enrollmentVO, null), HttpStatus.OK);
	}
	
	@PostMapping("/auction/bidder/enrollment/{id}/joint/holder")
	public ResponseEntity<ApiResponse> addJointHolder(
			@PathVariable("id") Long bidderAuctionEnrollmentId,
			@RequestBody JointHolderVO jointHolderVO){
		return new ResponseEntity<>(
				new ApiResponse(HttpStatus.OK.value(), this.messageResolver.getMessage(
						jointHolderVO.getId() == 0 ? "joint.holder.create" : "joint.holder.update"),
						this.jointHolderService.save(bidderAuctionEnrollmentId, jointHolderVO), null), HttpStatus.OK);
	}
	
	@GetMapping("/auction/bidder/enrollment/{id}/joint/holders")
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
	
	@GetMapping("/auction/{id}/bidder/enrollment/details")
	public ResponseEntity<ApiResponse> bidderEnrollmentByAuctionId(@PathVariable("id") Long auctionId){
		return new ResponseEntity<>(
				new ApiResponse(HttpStatus.OK.value(), this.messageResolver.getMessage("bidder.auction.enrollment.fetch"), 
						this.auctionEnrollmentService.findByAuctionIdAndBidder(auctionId), null), HttpStatus.OK);
	}
	
   @PutMapping("/auction/bidder/enrollment/verification")
   public ResponseEntity<ApiResponse> bidderEnrollmentVerification(
			@RequestBody BidderEnrollmentVerificationVO bidderEnrollmentVerificationVO)
   {
	   this.auctionEnrollmentService.bidderAuctionEnrollmentVerification(bidderEnrollmentVerificationVO);
		return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(), "Bidder Verified Successfully",
				null, null), HttpStatus.OK);
   
   }
   
}
