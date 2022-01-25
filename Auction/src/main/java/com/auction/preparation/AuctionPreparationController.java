package com.auction.preparation;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.auction.api.response.ApiResponse;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@RestController
public class AuctionPreparationController {
	
	@Autowired
	private IAuctionPreparationService auctionPreparationService;
	
	@PostMapping("/auction/preparation")
	public ResponseEntity<ApiResponse> create(@RequestBody AuctionPreparationVO auctionPreparationVO){
		return new ResponseEntity<>(new ApiResponse(HttpStatus.CREATED.value(), "Auction prepared successfully",
				this.auctionPreparationService.save(auctionPreparationVO), null), HttpStatus.OK);
	}
	
	@PostMapping("/auction/preparation/template/mapping")
	public ResponseEntity<ApiResponse> auctionPreparationTemplateMapping(@RequestBody AuctionPreparationTemplateMapping auctionPreparationTemplateMapping){
		 this.auctionPreparationService.mapToTemplate(auctionPreparationTemplateMapping.getAuctionId(), auctionPreparationTemplateMapping.getTemplateId());
		return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(), "Auction mapped with template successfully",
				null, null), HttpStatus.OK);
	}
	
	@GetMapping("/auction/preparation/{id}")
	public ResponseEntity<ApiResponse> auctionPreparationById(@PathVariable Long id){
		return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(), "Auction fetched successfully",
				this.auctionPreparationService.findAllDetailsById(id), null), HttpStatus.OK);
	}
	
	@GetMapping("/auction/preparation/filter")
	public ResponseEntity<ApiResponse> auctionPreparationFilter(
			@RequestParam(name = "rsdt", required = false) LocalDateTime registrationStartDateTime,
			@RequestParam(name = "redt", required = false) LocalDateTime registrationEndDateTime,
			@RequestParam(name = "description", required = false) String description){
		AuctionPreparationSearchParam auctionPreparationSearchParam = new AuctionPreparationSearchParam();
		auctionPreparationSearchParam.setDescription(description);
		auctionPreparationSearchParam.setRegistrationEndDateTime(registrationEndDateTime);
		auctionPreparationSearchParam.setRegistrationStartDateTime(registrationStartDateTime);
		return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(), "Auctions fetched successfully",
				this.auctionPreparationService.searchAuctionPreparation(auctionPreparationSearchParam), null), HttpStatus.OK);
	}

}

@Data
class AuctionPreparationTemplateMapping {
	 @JsonProperty("auction_id")
	 private Long auctionId;
	 @JsonProperty("template_id")
	 private Integer templateId;
}
