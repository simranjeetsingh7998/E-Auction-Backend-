package com.auction.label.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.auction.ApiResponseMessageResolver;
import com.auction.api.response.ApiResponse;

@RestController
public class AuctionLabelItemController {
	
	@Autowired
	private IAuctionLabelItemService auctionLabelItemService;
	
	@Autowired
	private ApiResponseMessageResolver messageResolver;
	
	@PostMapping("/auction/label/item")
	public ResponseEntity<ApiResponse> create(@RequestBody AuctionItemLabelVO auctionItemLabelVO){
		this.auctionLabelItemService.save(auctionItemLabelVO);
	  return new ResponseEntity<>(new ApiResponse(HttpStatus.CREATED.value(), messageResolver.getMessage("auction.label.item.create") , auctionItemLabelVO, null), HttpStatus.OK);	
	}

}
