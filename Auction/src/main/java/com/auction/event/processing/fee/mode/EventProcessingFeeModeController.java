package com.auction.event.processing.fee.mode;

import javax.validation.Valid;

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
public class EventProcessingFeeModeController {
	
	@Autowired
	private IEventProcessingFeeModeService eventProcessingFeeModeService;
	
	@Autowired
	private ApiResponseMessageResolver messageResolver;
	
	@PostMapping("/event/processing/fee/mode")
	public ResponseEntity<ApiResponse> create(@RequestBody @Valid EventProcessingFeeModeVO enEventProcessingFeeModeVO){
		  this.eventProcessingFeeModeService.addOrUpdate(enEventProcessingFeeModeVO);
		  return new ResponseEntity<>(new ApiResponse(HttpStatus.CREATED.value(), 
				  this.messageResolver.getMessage("event.processing.fee.mode.create") , null, null), HttpStatus.OK);
	}
	
	@GetMapping("/event/processing/fee/modes")
	public ResponseEntity<ApiResponse> activeEventProcessingFeeModes(){
		  return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(), 
				  this.messageResolver.getMessage("event.processing.fee.mode.fetchs") , this.eventProcessingFeeModeService.findAllByIsActiveTrue(), null), HttpStatus.OK);
	}
	
	@GetMapping("/event/processing/fee/mode/{id}")
	public ResponseEntity<ApiResponse> eventProcessingFeeModeById(@PathVariable Integer id){
		  return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(), 
				  this.messageResolver.getMessage("event.processing.fee.mode.fetch") , this.eventProcessingFeeModeService.findById(id), null), HttpStatus.OK);
	}
	
	@DeleteMapping("/event/processing/fee/mode/{id}/deactivate")
	public ResponseEntity<ApiResponse> deactiveEventProcessingFeeMode(@PathVariable Integer id){
		  this.eventProcessingFeeModeService.deActivate(id);
		  return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(), 
				  this.messageResolver.getMessage("event.processing.fee.mode.delete") ,null, null), HttpStatus.OK);
	}
	
	@PutMapping("/event/processing/fee/mode/modify")
	public ResponseEntity<ApiResponse> modifyEventProcessingFeeMode(@RequestBody @Valid EventProcessingFeeModeVO eventProcessingFeeModeVO){
		  this.eventProcessingFeeModeService.addOrUpdate(eventProcessingFeeModeVO);
		  return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(), 
				  this.messageResolver.getMessage("event.processing.fee.mode.update") ,null, null), HttpStatus.OK);
	}

}
