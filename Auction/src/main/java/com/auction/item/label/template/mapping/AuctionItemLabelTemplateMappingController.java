package com.auction.item.label.template.mapping;

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
public class AuctionItemLabelTemplateMappingController {
	
	@Autowired
	private IAuctionItemLabelTemplateMappingService  auctionItemLabelTemplateMappingService;
	
	@Autowired
	private ApiResponseMessageResolver messageResolver;
	
	@PostMapping("/auction/item/label/template/mapping")
	public ResponseEntity<ApiResponse> create(@RequestBody AuctionItemLabelTemplateMappingVO auctionItemLabelTemplateMappingVO){
		this.auctionItemLabelTemplateMappingService.save(auctionItemLabelTemplateMappingVO);
		return new ResponseEntity<>(new ApiResponse(HttpStatus.CREATED.value(),
				this.messageResolver.getMessage("auction.item.label.template.mapping.create"), null, null), HttpStatus.OK);
	}
	
	@PutMapping("/auction/item/label/template/mapping/modify")
	public ResponseEntity<ApiResponse> update(@RequestBody AuctionItemLabelTemplateMappingVO auctionItemLabelTemplateMappingVO){
		this.auctionItemLabelTemplateMappingService.save(auctionItemLabelTemplateMappingVO);
		return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(),
				this.messageResolver.getMessage("auction.item.label.template.mapping.update"), null, null), HttpStatus.OK);
	}
	
	@GetMapping("/template/{id}/item/label/master")
	public ResponseEntity<ApiResponse> itemLabelsByTemplate(@PathVariable Integer id){
		return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(),
				this.messageResolver.getMessage("auction.item.label.template.mapping.fetchs"), this.auctionItemLabelTemplateMappingService.findAllByAuctionItemTemplate(id), null), HttpStatus.OK);
	}

	@DeleteMapping("/auction/item/label/template/mapping/{id}/delete")
	public ResponseEntity<ApiResponse> deactivate(@PathVariable Integer id){
		this.auctionItemLabelTemplateMappingService.deleteById(id);
		return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(),
				this.messageResolver.getMessage("auction.item.label.template.mapping.delete"), null , null), HttpStatus.OK);
	}
	
	@DeleteMapping("/template/{templateId}/label/{labelId}/delete")
	public ResponseEntity<ApiResponse> deleteMappingByTemplateIdAndLabelId(
			@PathVariable("templateId") Integer templateId,@PathVariable("labelId") Integer labelId){
		this.auctionItemLabelTemplateMappingService.deleteByTemplateIdAndLabelId(templateId, labelId);
		return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(),
				this.messageResolver.getMessage("auction.item.label.template.mapping.delete"), null , null), HttpStatus.OK);
	}
	

}
