package com.auction.preparation;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
	
	@PostMapping(path="/auction/preparation/{id}/add/item", consumes = {
			MediaType.MULTIPART_FORM_DATA_VALUE})
	public ResponseEntity<ApiResponse> addItemInAuction(@PathVariable("id") Long id,
			@RequestPart("auctionItem") String auctionItem, 
			@RequestPart(name =  "file", required = false) MultipartFile multipartFile) throws IOException{
		return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(), "Item added in auction successfully",
				this.auctionPreparationService.addAuctionItem(id, auctionItem, multipartFile), null), HttpStatus.OK);
	}
	
	@GetMapping(path="/auction/preparation/{auctionId}/item/{itemId}/organization/items")
	public ResponseEntity<ApiResponse> getOrganizationItemsByAuctionItemId(
			@PathVariable("auctionId") Long auctionId,
			@PathVariable("itemId") Long itemId){
		return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(), "Item added in auction successfully",
				this.auctionPreparationService.findOrganizationItemsByAuctionIdAndItemId(auctionId, itemId), null), HttpStatus.OK);
	}

	
	@DeleteMapping(path="/auction/preparation/{id}/delete/item/{itemId}")
	public ResponseEntity<ApiResponse> deleteItemFromAuction(@PathVariable("id") Long auctionPreparationId,
			@PathVariable("itemId") Long auctionItemId){
		 this.auctionPreparationService.deleteAuctionItem(auctionPreparationId, auctionItemId);
		return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(), "Item deleted from auction successfully",
				null, null), HttpStatus.OK);
	}
	
	@PostMapping(path = "/upload/document/auction/preparation/{id}/{documentType}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<ApiResponse> addUserDocument(@PathVariable("id") Long auctionPreparationId, @PathVariable String documentType, 
			@RequestPart("document") MultipartFile multipartFile) throws IOException{
		 return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(), documentType+" uploaded successfully",
					this.auctionPreparationService.uploadDocument(auctionPreparationId, documentType, multipartFile), null), HttpStatus.OK);
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
	
	@PutMapping("/auction/preparation/{id}/publish")
	public ResponseEntity<ApiResponse> publishAuctionById(@PathVariable Long id, @RequestBody AuctionPreparationVO auctionPreparationVO){
		this.auctionPreparationService.publish(id, auctionPreparationVO);
		return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(), "Auction published successfully",
				null, null), HttpStatus.OK);
	}
	
	@PutMapping("/auction/preparation/{id}/schedule")
	public ResponseEntity<ApiResponse> scheduleAuction(@PathVariable Long id, @RequestBody AuctionScheduleVO auctionScheduleVO){
		this.auctionPreparationService.schedule(id, auctionScheduleVO);
		return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(), "Auction scheduled successfully",
				null, null), HttpStatus.OK);
	}
	
	@PutMapping("/auction/preparation/{id}/return")
	public ResponseEntity<ApiResponse> returnAuctionById(@PathVariable Long id, @RequestBody ReturnReasonVO returnReasonVO){
		this.auctionPreparationService.returnAuction(id, returnReasonVO);
		return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(), "Auction returned successfully",
				null, null), HttpStatus.OK);
	}
	
	@GetMapping("/auction/preparation/{id}/return/reasons")
	public ResponseEntity<ApiResponse> returnReasonsByAuctionId(@PathVariable Long id){
		return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(), "Auction return reasons fetched successfully",
				this.auctionPreparationService.returnReasonsByAuctionId(id), null), HttpStatus.OK);
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
	
	@GetMapping("/auction/preparation")
	public ResponseEntity<ApiResponse> auctionPreparationByStatus(
			@RequestParam("status") String status){
		return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(), "Auctions fetched successfully",
				this.auctionPreparationService.findAuctionByStatus(status), null), HttpStatus.OK);
	}
	
	@GetMapping("/user/current/auction/preparation")
	public ResponseEntity<ApiResponse> userCurrentAuctionPreparation(@RequestParam(name = "auctionIds", required = false) String auctions){
		Long[] auctionIds = null;
		if(!Objects.isNull(auctions)){
              String [] auctionIdsArray = auctions.split(",");
			  auctionIds = new Long[auctionIdsArray.length];
			  for(int i =0; i< auctionIdsArray.length; i++){
				   auctionIds[i] = Long.parseLong(auctionIdsArray[i]);
			  }
		}
		return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(), "Current auctions fetched successfully",
				this.auctionPreparationService.userCurrentAuctions(auctionIds), null), HttpStatus.OK);
	}

}

@Data
class AuctionPreparationTemplateMapping {
	 @JsonProperty("auction_id")
	 private Long auctionId;
	 @JsonProperty("template_id")
	 private Integer templateId;
}
