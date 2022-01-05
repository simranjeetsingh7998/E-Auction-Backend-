package com.auction.sector;

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
public class SectorController {
	
	@Autowired
	private ISectorService sectorService;
	
	@Autowired
	private ApiResponseMessageResolver messageResolver;
	
	@PostMapping("/sector")
	public ResponseEntity<ApiResponse> create(@RequestBody @Valid SectorVO sectorVO){
		  this.sectorService.addOrUpdate(sectorVO);
		  return new ResponseEntity<>(new ApiResponse(HttpStatus.CREATED.value(), 
				  this.messageResolver.getMessage("sector.create") , null, null), HttpStatus.OK);
	}
	
	@GetMapping("/sectors")
	public ResponseEntity<ApiResponse> activeSectors(){
		  return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(), 
				  this.messageResolver.getMessage("sector.fetchs") , this.sectorService.findAllByIsActiveTrue(), null), HttpStatus.OK);
	}
	
	@GetMapping("/sector/{id}")
	public ResponseEntity<ApiResponse> sectorById(@PathVariable Integer id){
		  return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(), 
				  this.messageResolver.getMessage("sector.fetch") , this.sectorService.findById(id), null), HttpStatus.OK);
	}
	
	@DeleteMapping("/sector/{id}/deactivate")
	public ResponseEntity<ApiResponse> deactiveSector(@PathVariable Integer id){
		  this.sectorService.deActivate(id);
		  return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(), 
				  this.messageResolver.getMessage("sector.delete") ,null, null), HttpStatus.OK);
	}
	
	@PutMapping("/sector/modify")
	public ResponseEntity<ApiResponse> modifySector(@RequestBody @Valid SectorVO sectorVO){
		  this.sectorService.addOrUpdate(sectorVO);
		  return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(), 
				  this.messageResolver.getMessage("sector.update") ,null, null), HttpStatus.OK);
	}

}
