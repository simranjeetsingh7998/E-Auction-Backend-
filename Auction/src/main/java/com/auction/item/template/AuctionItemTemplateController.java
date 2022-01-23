package com.auction.item.template;

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
public class AuctionItemTemplateController {
	
	@Autowired
	private IAuctionItemTemplateService auctionItemTemplateService;
	
	@Autowired
	private ApiResponseMessageResolver messageResolver;
	
	@PostMapping("/auction/item/template")
	public ResponseEntity<ApiResponse> create(@RequestBody AuctionItemTemplateVO auctionItemTemplateVO){
		this.auctionItemTemplateService.save(auctionItemTemplateVO);
		return new ResponseEntity<>(new ApiResponse(HttpStatus.CREATED.value(),
				this.messageResolver.getMessage("auction.item.template.create"), null, null), HttpStatus.OK);
	}
	
	@PutMapping("/auction/item/template/modify")
	public ResponseEntity<ApiResponse> update(@RequestBody AuctionItemTemplateVO auctionItemTemplateVO){
		this.auctionItemTemplateService.save(auctionItemTemplateVO);
		return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(),
				this.messageResolver.getMessage("auction.item.template.update"), null, null), HttpStatus.OK);
	}
	
	@GetMapping("/organization/auction/item/templates")
	public ResponseEntity<ApiResponse> auctionItemTemplateByOrganization(){
		return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(),
				this.messageResolver.getMessage("auction.item.template.fetchs"), this.auctionItemTemplateService.findAllByOrganization(), null), HttpStatus.OK);
	}

	@DeleteMapping("/auction/item/template/{id}/deactivate")
	public ResponseEntity<ApiResponse> deactivate(@PathVariable Integer id){
		this.auctionItemTemplateService.deActivateById(id);
		return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(),
				this.messageResolver.getMessage("auction.item.template.delete"), null, null), HttpStatus.OK);
	}
	
}
