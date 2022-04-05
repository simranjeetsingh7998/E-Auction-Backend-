package com.auction.bidding;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
		String biddingCreateMessage = this.messageResolver.getMessage("bidding.create");
		this.simpMessagingTemplate.convertAndSend("/queue/bidding/"+biddingVO.getAuctionPreparation().getId(), 
				new ApiResponse(HttpStatus.OK.value(),
						biddingCreateMessage,
				biddingVO, null));
		return new ResponseEntity<>(
				new ApiResponse(HttpStatus.OK.value(),
				biddingCreateMessage,
				biddingVO, null), HttpStatus.OK);
	}
	
	@GetMapping("/auction/{id}/last/bid")
	public ResponseEntity<ApiResponse> lastBidOfAuction(@PathVariable("id") Long auctionId){
		return new ResponseEntity<>(
				new ApiResponse(HttpStatus.OK.value(),
				this.messageResolver.getMessage("bidding.last.bid"),
				this.biddingService.lastBidOfAuction(auctionId), null), HttpStatus.OK);
	}
	
	@GetMapping("/auction/{id}/bid/history")
	public ResponseEntity<ApiResponse> bidHistory(@PathVariable("id") Long auctionId){
		return new ResponseEntity<>(
				new ApiResponse(HttpStatus.OK.value(),
				this.messageResolver.getMessage("bidding.history"),
				this.biddingService.findBidHistoryByActionPreparation(auctionId), null), HttpStatus.OK);
	}
	
	@PostMapping("/auction/{id}/close/round")
	public ResponseEntity<ApiResponse> closeAuctionRound(@PathVariable Long auctionId){
		 long closedRoundNumber = this.biddingService.closeRoundByAuctionPreparation(auctionId);
		 String responseMsg = "Round "+closedRoundNumber+" is closed";
			this.simpMessagingTemplate.convertAndSend("/queue/bidding/round/close/"+auctionId, 
					new ApiResponse(HttpStatus.OK.value(),
							responseMsg,
					null, null));
		 return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(), 
				 responseMsg,
				 null, null), HttpStatus.OK);
	}
	
	@GetMapping("/auction/{id}/properties/unsold")
	public ResponseEntity<ApiResponse> unsoldProperties(@PathVariable Long auctionId){
		return new ResponseEntity<>(
				new ApiResponse(HttpStatus.OK.value(),
				this.messageResolver.getMessage("auction.item.properties.unsold.fetch"),
				this.biddingService.findUnsoldPropertiesForH1Bidder(auctionId), null), HttpStatus.OK);
	}
	
	@PatchMapping("/auction/{auctionId}/{propertyId}/reserve/property")
	public ResponseEntity<ApiResponse> markPropertyReserve(@PathVariable Long auctionId,@PathVariable Long propertyId){
		this.biddingService.markPropertyAsReserved(auctionId,propertyId);
		 return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(),
				 messageResolver.getMessage("auction.item.properties.status.update"), null, null), HttpStatus.OK);
	}

}
