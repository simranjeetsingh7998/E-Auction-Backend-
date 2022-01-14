package com.auction.organization;

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
public class OrganizationController {
	
	@Autowired
	private IOrganizationService organizationService;
	
	@Autowired
	private ApiResponseMessageResolver messageResolver;
	
	@PostMapping("/organization")
	public ResponseEntity<ApiResponse> create(@RequestBody @Valid OrganizationVO organizationVO){
		  this.organizationService.addOrUpdate(organizationVO);
		  return new ResponseEntity<>(new ApiResponse(HttpStatus.CREATED.value(), 
				  this.messageResolver.getMessage("organization.create") , null, null), HttpStatus.OK);
	}
	
	@GetMapping("/organizations")
	public ResponseEntity<ApiResponse> activeOrganizations(){
		  return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(), 
				  this.messageResolver.getMessage("organization.fetchs") , this.organizationService.findAllByIsActiveTrue(), null), HttpStatus.OK);
	}
	
	@GetMapping("/organization/{id}")
	public ResponseEntity<ApiResponse> organizationById(@PathVariable Integer id){
		  return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(), 
				  this.messageResolver.getMessage("organization.fetch") , this.organizationService.findById(id), null), HttpStatus.OK);
	}
	
	@DeleteMapping("/organization/{id}/deactivate")
	public ResponseEntity<ApiResponse> deactiveOrganization(@PathVariable Integer id){
		  this.organizationService.deActivate(id);
		  return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(), 
				  this.messageResolver.getMessage("organization.delete") ,null, null), HttpStatus.OK);
	}
	
	@PutMapping("/organization/modify")
	public ResponseEntity<ApiResponse> modifyOrganization(@RequestBody @Valid OrganizationVO organizationVO){
		  this.organizationService.addOrUpdate(organizationVO);
		  return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(), 
				  this.messageResolver.getMessage("organization.update") ,null, null), HttpStatus.OK);
	}

}
