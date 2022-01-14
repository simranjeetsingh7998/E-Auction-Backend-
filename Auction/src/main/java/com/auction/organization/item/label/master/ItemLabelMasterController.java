package com.auction.organization.item.label.master;

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
public class ItemLabelMasterController {
	
	@Autowired
	private IItemLabelMasterService itemLabelMasterService;
	
	@Autowired
	private ApiResponseMessageResolver messageResolver;
	
	@PostMapping("/item/label/master")
	public ResponseEntity<ApiResponse> create(@RequestBody @Valid ItemLabelMasterVO itemMItemLabelMasterVO){
		  this.itemLabelMasterService.addOrUpdate(itemMItemLabelMasterVO);
		  return new ResponseEntity<>(new ApiResponse(HttpStatus.CREATED.value(), 
				  this.messageResolver.getMessage("item.label.master.create") , null, null), HttpStatus.OK);
	}
	
	@GetMapping("/item/label/masters")
	public ResponseEntity<ApiResponse> activeItemLabelMasters(){
		  return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(), 
				  this.messageResolver.getMessage("item.label.master.fetchs") , this.itemLabelMasterService.findAllByIsActiveTrue(), null), HttpStatus.OK);
	}
	
	@GetMapping("/item/label/master/{id}")
	public ResponseEntity<ApiResponse> itemLabelMasterById(@PathVariable Integer id){
		  return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(), 
				  this.messageResolver.getMessage("item.label.master.fetch") , this.itemLabelMasterService.findById(id), null), HttpStatus.OK);
	}
	
	@DeleteMapping("/item/label/master/{id}/deactivate")
	public ResponseEntity<ApiResponse> deactiveItemLabelMaster(@PathVariable Integer id){
		  this.itemLabelMasterService.deActivate(id);
		  return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(), 
				  this.messageResolver.getMessage("item.label.master.delete") ,null, null), HttpStatus.OK);
	}
	
	@PutMapping("/item/label/master/modify")
	public ResponseEntity<ApiResponse> modifyItemLabelMaster(@RequestBody @Valid ItemLabelMasterVO itemLabelMasterVO){
		  this.itemLabelMasterService.addOrUpdate(itemLabelMasterVO);
		  return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(), 
				  this.messageResolver.getMessage("item.label.master.update") ,null, null), HttpStatus.OK);
	}

}
