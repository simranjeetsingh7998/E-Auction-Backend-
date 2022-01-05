package com.auction.division;

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
public class DivisionController {
	
	@Autowired
	private IDivisionService divisionService;
	
	@Autowired
	private ApiResponseMessageResolver messageResolver;
	
	@PostMapping("/division")
	public ResponseEntity<ApiResponse> create(@RequestBody @Valid DivisionVO divisionVO){
		  this.divisionService.addOrUpdate(divisionVO);
		  return new ResponseEntity<>(new ApiResponse(HttpStatus.CREATED.value(), 
				  this.messageResolver.getMessage("division.create") , null, null), HttpStatus.OK);
	}
	
	@GetMapping("/divisions")
	public ResponseEntity<ApiResponse> activeDivisions(){
		  return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(), 
				  this.messageResolver.getMessage("division.fetchs") , this.divisionService.findAllByIsActiveTrue(), null), HttpStatus.OK);
	}
	
	@GetMapping("/division/{id}")
	public ResponseEntity<ApiResponse> divisionById(@PathVariable Integer id){
		  return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(), 
				  this.messageResolver.getMessage("division.fetch") , this.divisionService.findById(id), null), HttpStatus.OK);
	}
	
	@DeleteMapping("/division/{id}/deactivate")
	public ResponseEntity<ApiResponse> deactiveDivision(@PathVariable Integer id){
		  this.divisionService.deActivate(id);
		  return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(), 
				  this.messageResolver.getMessage("division.delete") ,null, null), HttpStatus.OK);
	}
	
	@PutMapping("/division/modify")
	public ResponseEntity<ApiResponse> modifyDivision(@RequestBody @Valid DivisionVO divisionVO){
		  this.divisionService.addOrUpdate(divisionVO);
		  return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(), 
				  this.messageResolver.getMessage("division.update") ,null, null), HttpStatus.OK);
	}

}
