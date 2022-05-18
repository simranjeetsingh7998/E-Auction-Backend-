package com.auction.report;

import java.io.IOException;
import java.util.Objects;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.auction.api.response.ApiResponse;
import com.auction.global.exception.ResourceNotFoundException;
import com.auction.preparation.AuctionMethodEnum;
import com.auction.preparation.IAuctionPreparationService;

@RestController
public class ReportController {
	
	@Autowired
	private AuctionBiddingReport auctionBiddingReport;
	
	@Autowired
	private IAuctionPreparationService auctionPreparationService;
	
	@GetMapping("/auction/{id}/bidding/report")
	public ResponseEntity<ApiResponse> getBiddingHistoryAuction(@PathVariable("id") Long auctionId, HttpServletResponse response) throws IOException{
		       String auctionMethod = this.auctionPreparationService.findAuctionMethodByAuctionId(auctionId); 
		       if(Objects.isNull(auctionMethod)) {
				      throw new ResourceNotFoundException("Auction method not found");
		       }
		       response.setContentType("application/octet-stream");
		        String headerKey = "Content-Disposition";
		        String headerValue = "attachment; filename=auctionnumber_"+auctionId+"_bidding.xlsx";
		        response.setHeader(headerKey, headerValue);
		        if(auctionMethod.equalsIgnoreCase(AuctionMethodEnum.NORMAL.getMethod()))
		              this.auctionBiddingReport.export(response, auctionId);
		        else if(auctionMethod.equalsIgnoreCase(AuctionMethodEnum.ROUNDWISE.getMethod()))
		        	 this.auctionBiddingReport.export(response, auctionId);
		      return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(),
		    		  "Report Generated successfully", null, null), HttpStatus.OK);  
	}
	
	@GetMapping("/auction/{id}/bidder/bidding/report")
	public ResponseEntity<ApiResponse> getBiddingHistoryAuctionForBidder(@PathVariable("id") Long auctionId, HttpServletResponse response) throws IOException{
		       String auctionMethod = this.auctionPreparationService.findAuctionMethodByAuctionId(auctionId); 
		       if(Objects.isNull(auctionMethod)) {
				      throw new ResourceNotFoundException("Auction method not found");
		       }
		       response.setContentType("application/octet-stream");
		        String headerKey = "Content-Disposition";
		        String headerValue = "attachment; filename=auctionnumber_"+auctionId+"_bidding.xlsx";
		        response.setHeader(headerKey, headerValue);
		        if(auctionMethod.equalsIgnoreCase(AuctionMethodEnum.NORMAL.getMethod()))
		              this.auctionBiddingReport.exportForBidder(response, auctionId);
		        else if(auctionMethod.equalsIgnoreCase(AuctionMethodEnum.ROUNDWISE.getMethod()))
		        	 this.auctionBiddingReport.exportForBidder(response, auctionId);
		      return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(),
		    		  "Report Generated successfully", null, null), HttpStatus.OK);  
	}

}
