package com.auction.report;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.auction.api.response.ApiResponse;

@RestController
public class ReportController {
	
	@Autowired
	private AuctionBiddingReport auctionBiddingReport;
	
	@GetMapping("/unsecure/users/export/excel")
	public ResponseEntity<ApiResponse> getBiddingHistoryAuction(HttpServletResponse response) throws IOException{
		        response.setContentType("application/octet-stream");
		         
		        String headerKey = "Content-Disposition";
		        String headerValue = "attachment; filename=users.xlsx";
		        response.setHeader(headerKey, headerValue);
		        this.auctionBiddingReport.export(response);
		      return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(),
		    		  "Report Generated successfully", null, null), HttpStatus.OK);  
	}

}
