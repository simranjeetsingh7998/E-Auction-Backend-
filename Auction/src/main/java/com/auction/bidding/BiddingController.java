package com.auction.bidding;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.auction.ApiResponseMessageResolver;
import com.auction.api.response.ApiResponse;

@RestController
public class BiddingController {
	
	@Autowired
	private IBiddingService biddingService;
	
	@Autowired
	private ApiResponseMessageResolver messageResolver;
	
	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;
	
	@PostMapping("/bidding")
	public ResponseEntity<ApiResponse> bid(@RequestBody BiddingVO biddingVO){
		biddingVO = this.biddingService.bidding(biddingVO);
		this.simpMessagingTemplate.convertAndSend("/queue/bidding/"+biddingVO.getAuctionPreparation().getId(), 
				new ApiResponse(HttpStatus.OK.value(),
				this.messageResolver.getMessage("bidding.create"),
				biddingVO, null));
		return new ResponseEntity<>(
				new ApiResponse(HttpStatus.OK.value(),
				this.messageResolver.getMessage("bidding.create") ,
				biddingVO, null), HttpStatus.OK);
	}

}
