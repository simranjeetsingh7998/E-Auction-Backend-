package com.auction.properties;

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
public class AuctionItemProprtiesController {
	
	@Autowired
	private IAuctionItemProprtiesService auctionItemProprtiesService;
	
	@Autowired
	private ApiResponseMessageResolver messageResolver;
	
	@PostMapping("/auction/item/properties")
	public ResponseEntity<ApiResponse> addAuctionItemProperties(@RequestBody AuctionItemProprtiesVO auctionItemProprtiesVO){
		 this.auctionItemProprtiesService.save(auctionItemProprtiesVO);
		 return new ResponseEntity<>(new ApiResponse(HttpStatus.CREATED.value(),
				 messageResolver.getMessage("auction.item.properties.create"), null, null), HttpStatus.OK);
	}

	@GetMapping("/organization/item/{id}/sold/auction/item/properties")
	public ResponseEntity<ApiResponse> findAllSoldAuctionItemProperties(@PathVariable("organizationItemId") Long organizationItemId){
		 return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(),
				 messageResolver.getMessage("auction.item.properties.sold.fetch"), 
				 this.auctionItemProprtiesService.findAllSoldProperties(organizationItemId), null), HttpStatus.OK);
	}
	
	@GetMapping("/organization/item/{id}/unsold/auction/item/properties")
	public ResponseEntity<ApiResponse> findAllUnsoldAuctionItemProperties(@PathVariable("organizationItemId") Long organizationItemId){
		 return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(),
				 messageResolver.getMessage("auction.item.properties.unsold.fetch"),
				 this.auctionItemProprtiesService.findAllUnSoldProperties(organizationItemId), null), HttpStatus.OK);
	}
	
	
}
