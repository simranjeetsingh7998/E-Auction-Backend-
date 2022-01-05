package com.auction.station;

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
public class StationController {
	
	@Autowired
	private IStationService stationService;
	
	@Autowired
	private ApiResponseMessageResolver messageResolver;
	
	@PostMapping("/station")
	public ResponseEntity<ApiResponse> create(@RequestBody @Valid StationVO stationVO){
		  this.stationService.addOrUpdate(stationVO);
		  return new ResponseEntity<>(new ApiResponse(HttpStatus.CREATED.value(), 
				  this.messageResolver.getMessage("station.create") , null, null), HttpStatus.OK);
	}
	
	@GetMapping("/stations")
	public ResponseEntity<ApiResponse> activeStations(){
		  return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(), 
				  this.messageResolver.getMessage("station.fetchs") , this.stationService.findAllByIsActiveTrue(), null), HttpStatus.OK);
	}
	
	@GetMapping("/station/{id}")
	public ResponseEntity<ApiResponse> stationById(@PathVariable Integer id){
		  return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(), 
				  this.messageResolver.getMessage("station.fetch") , this.stationService.findById(id), null), HttpStatus.OK);
	}
	
	@DeleteMapping("/station/{id}/deactivate")
	public ResponseEntity<ApiResponse> deactiveStation(@PathVariable Integer id){
		  this.stationService.deActivate(id);
		  return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(), 
				  this.messageResolver.getMessage("station.delete") ,null, null), HttpStatus.OK);
	}
	
	@PutMapping("/station/modify")
	public ResponseEntity<ApiResponse> modifyStation(@RequestBody @Valid StationVO stationVO){
		  this.stationService.addOrUpdate(stationVO);
		  return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(), 
				  this.messageResolver.getMessage("station.update") ,null, null), HttpStatus.OK);
	}

}
