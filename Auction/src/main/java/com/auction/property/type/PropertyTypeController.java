package com.auction.property.type;

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
public class PropertyTypeController {
	
	@Autowired
	private IPropertyTypeService propertyTypeService;
	
	@Autowired
	private ApiResponseMessageResolver messageResolver;
	
	@PostMapping("/property/type")
	public ResponseEntity<ApiResponse> save(@RequestBody PropertyTypeVO propertyTypeVO){
		this.propertyTypeService.save(propertyTypeVO);
	  return new ResponseEntity<>(new ApiResponse(HttpStatus.CREATED.value(),
			this.messageResolver.getMessage("property.type.created"), null, null), HttpStatus.OK);	
	}
	
	@GetMapping("/auction/category/{id}/property/types")
	public ResponseEntity<ApiResponse> propertyTypesByAuctionCategoryId(@PathVariable("id") Integer auctionCategoryId){
	  return new ResponseEntity<>(new ApiResponse(HttpStatus.CREATED.value(),
			this.messageResolver.getMessage("property.type.fetchs"), 
			this.propertyTypeService.findAllByAuctionCategory(auctionCategoryId), null), HttpStatus.OK);	
	}

	@PutMapping("/property/type/update")
	public ResponseEntity<ApiResponse> modifyPropertyType(@RequestBody PropertyTypeVO propertyTypeVO){
		this.propertyTypeService.save(propertyTypeVO);
		  return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(),
				this.messageResolver.getMessage("property.type.update"), null, null), HttpStatus.OK);	
	}
	
	@DeleteMapping("/property/type/{id}/deactivate")
	public ResponseEntity<ApiResponse> deactivatePropertyType(@PathVariable("id") Integer id){
		  this.propertyTypeService.deactivate(id);
		  return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(),
					this.messageResolver.getMessage("property.type.delete"), null, null), HttpStatus.OK);
	}
}
