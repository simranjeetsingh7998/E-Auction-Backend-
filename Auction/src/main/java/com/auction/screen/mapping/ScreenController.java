package com.auction.screen.mapping;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.auction.ApiResponseMessageResolver;
import com.auction.api.response.ApiResponse;

@RestController
public class ScreenController {
	
	@Autowired
	private IScreenService screenService;
	
	@Autowired
	private ApiResponseMessageResolver messageResolver;
	
	@PostMapping("/screen")
	public ResponseEntity<ApiResponse> create(@RequestBody ScreenVO screenVO){
		 this.screenService.save(screenVO);
		return new ResponseEntity<>(new ApiResponse(HttpStatus.CREATED.value(), this.messageResolver.getMessage("") ,
				null, null), HttpStatus.OK); 
	}
	
	@GetMapping("/screen/role/{id}/mapping")
	public ResponseEntity<ApiResponse> getScreensForMappingByRole(@PathVariable("id") int id){
		return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(), this.messageResolver.getMessage("") ,
				this.screenService.findAllWithMappedByRole(id), null), HttpStatus.OK); 
	}
	
	@PostMapping("/screen/role/mapping")
	public ResponseEntity<ApiResponse> screenRoleMapping(@RequestBody ScreenRoleMappingVO screenRoleMapping){
		 this.screenService.mapScreensToRole(screenRoleMapping);
		return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(), this.messageResolver.getMessage("") ,
				null, null), HttpStatus.OK); 
	}
	
	@GetMapping("/user/screens")
	public ResponseEntity<ApiResponse> getScreensForUser(){
		return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(), this.messageResolver.getMessage("") ,
				this.screenService.findByRole(), null), HttpStatus.OK); 
	}

}
