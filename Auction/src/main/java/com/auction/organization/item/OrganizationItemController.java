package com.auction.organization.item;

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
public class OrganizationItemController {
	
	@Autowired
	private IOrganizationItemService organizationItemService;
	
	@Autowired
	private ApiResponseMessageResolver messageResolver;
	
	@PostMapping("/organization/item")
	public ResponseEntity<ApiResponse> create(@RequestBody @Valid OrganizationItemVO organizationItemVO){
		  this.organizationItemService.addOrUpdate(organizationItemVO);
		  return new ResponseEntity<>(new ApiResponse(HttpStatus.CREATED.value(), 
				  this.messageResolver.getMessage("organization.item.create") , null, null), HttpStatus.OK);
	}
	
//	@GetMapping("/organization/items")
//	public ResponseEntity<ApiResponse> activeOrganizationItems(){
//		  return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(), 
//				  this.messageResolver.getMessage("organization.item.fetchs") , null, null), HttpStatus.OK);
//	}
//	
	@GetMapping("/organization/items/label/{id}")
	public ResponseEntity<ApiResponse> organizationItemsByItemLabel(@PathVariable("id") Integer itemLabelMasterId){
		  return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(), 
				  this.messageResolver.getMessage("organization.item.fetchs") , this.organizationItemService.findAllByItemLabelMasterId(itemLabelMasterId), null), HttpStatus.OK);
	}
	
	@GetMapping("/organization/item/{id}")
	public ResponseEntity<ApiResponse> organizationItemById(@PathVariable Long id){
		  return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(), 
				  this.messageResolver.getMessage("organization.item.fetch") , this.organizationItemService.findById(id), null), HttpStatus.OK);
	}
	
	@DeleteMapping("/organization/item/{id}/deactivate")
	public ResponseEntity<ApiResponse> deactiveOrganizationItem(@PathVariable Long id){
		  this.organizationItemService.deActivate(id);
		  return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(), 
				  this.messageResolver.getMessage("organization.item.delete") ,null, null), HttpStatus.OK);
	}
	
	@PutMapping("/organization/item/modify")
	public ResponseEntity<ApiResponse> modifyOrganizationItem(@RequestBody @Valid OrganizationItemVO organizationItemVO){
		  this.organizationItemService.addOrUpdate(organizationItemVO);
		  return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(), 
				  this.messageResolver.getMessage("organization.item.update") ,null, null), HttpStatus.OK);
	}

}
