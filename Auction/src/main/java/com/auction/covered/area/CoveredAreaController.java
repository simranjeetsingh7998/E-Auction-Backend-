package com.auction.covered.area;

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
public class CoveredAreaController {
	
	@Autowired
	private ICoveredAreaService coveredAreaService;
	
	@Autowired
	private ApiResponseMessageResolver messageResolver;
	
	@PostMapping("/covered/area")
	public ResponseEntity<ApiResponse> create(@RequestBody @Valid CoveredAreaVO coveredAreaVO){
		  this.coveredAreaService.addOrUpdate(coveredAreaVO);
		  return new ResponseEntity<>(new ApiResponse(HttpStatus.CREATED.value(), 
				  this.messageResolver.getMessage("covered.area.create") , null, null), HttpStatus.OK);
	}
	
	@GetMapping("/covered/areas")
	public ResponseEntity<ApiResponse> activeCoveredAreas(){
		  return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(), 
				  this.messageResolver.getMessage("covered.area.fetchs") , this.coveredAreaService.findAllByIsActiveTrue(), null), HttpStatus.OK);
	}
	
	@GetMapping("/covered/area/{id}")
	public ResponseEntity<ApiResponse> coveredAreaById(@PathVariable Integer id){
		  return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(), 
				  this.messageResolver.getMessage("covered.area.fetch") , this.coveredAreaService.findById(id), null), HttpStatus.OK);
	}
	
	@DeleteMapping("/covered/area/{id}/deactivate")
	public ResponseEntity<ApiResponse> deactiveCoveredArea(@PathVariable Integer id){
		  this.coveredAreaService.deActivate(id);
		  return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(), 
				  this.messageResolver.getMessage("covered.area.delete") ,null, null), HttpStatus.OK);
	}
	
	@PutMapping("/covered/area/modify")
	public ResponseEntity<ApiResponse> modifyCoveredArea(@RequestBody @Valid CoveredAreaVO coveredAreaVO){
		  this.coveredAreaService.addOrUpdate(coveredAreaVO);
		  return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(), 
				  this.messageResolver.getMessage("covered.area.update") ,null, null), HttpStatus.OK);
	}

}
