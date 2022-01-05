package com.auction.bid.submission.placement;

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
public class BidSubmissionPlacementController {
	
	@Autowired
	private IBidSubmissionPlacementService bidSubmissionPlacementService;
	
	@Autowired
	private ApiResponseMessageResolver messageResolver;
	
	@PostMapping("/bid/submission/placement")
	public ResponseEntity<ApiResponse> create(@RequestBody @Valid BidSubmissionPlacementVO bidSubmissionPlacementVO){
		  this.bidSubmissionPlacementService.addOrUpdate(bidSubmissionPlacementVO);
		  return new ResponseEntity<>(new ApiResponse(HttpStatus.CREATED.value(), 
				  this.messageResolver.getMessage("bid.submission.placement.create") , null, null), HttpStatus.OK);
	}
	
	@GetMapping("/bid/submission/placements")
	public ResponseEntity<ApiResponse> activeBidSubmissionPlacement(){
		  return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(), 
				  this.messageResolver.getMessage("bid.submission.placement.fetchs") , this.bidSubmissionPlacementService.findAllByIsActiveTrue(), null), HttpStatus.OK);
	}
	
	@GetMapping("/bid/submission/placement/{id}")
	public ResponseEntity<ApiResponse> bidSubmissionPlacementById(@PathVariable Integer id){
		  return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(), 
				  this.messageResolver.getMessage("bid.submission.placement.fetch") , this.bidSubmissionPlacementService.findById(id), null), HttpStatus.OK);
	}
	
	@DeleteMapping("/bid/submission/placement/{id}/deactivate")
	public ResponseEntity<ApiResponse> deactiveBidSubmissionPlacement(@PathVariable Integer id){
		  this.bidSubmissionPlacementService.deActivate(id);
		  return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(), 
				  this.messageResolver.getMessage("bid.submission.placement.delete") ,null, null), HttpStatus.OK);
	}
	
	@PutMapping("/bid/submission/placement/modify")
	public ResponseEntity<ApiResponse> modifyBidSubmissionPlacement(@RequestBody @Valid BidSubmissionPlacementVO bidSubmissionPlacementVO){
		  this.bidSubmissionPlacementService.addOrUpdate(bidSubmissionPlacementVO);
		  return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(), 
				  this.messageResolver.getMessage("bid.submission.placement.update") ,null, null), HttpStatus.OK);
	}

}
