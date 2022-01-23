package com.auction.user;

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
public class RoleController {
	
	@Autowired
	private IRoleService roleService;
	
	@Autowired 
	private ApiResponseMessageResolver messageResolver;
	
	@PostMapping("/role")
	public ResponseEntity<ApiResponse> create( @RequestBody RoleVO roleVO){
		 this.roleService.save(roleVO);
		 return new ResponseEntity<>(new ApiResponse(HttpStatus.CREATED.value(), this.messageResolver.getMessage("role.create"), null, null), HttpStatus.OK);
	}
	
	@PutMapping("/role/modify")
	public ResponseEntity<ApiResponse> modifyRole(@RequestBody RoleVO roleVO){
		 this.roleService.save(roleVO);
		 return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(), this.messageResolver.getMessage("role.update"), null, null), HttpStatus.OK);
	}
	
	@GetMapping("/roles")
	public ResponseEntity<ApiResponse> rolesOfOrganization(){
		 return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(), this.messageResolver.getMessage("role.fetchs"), 
				 this.roleService.findAllByOrganization(), null), HttpStatus.OK);
	}
	
	@DeleteMapping("/role/{id}/delete")
	public ResponseEntity<ApiResponse> deleteRoleById(@PathVariable Integer id){
		this.roleService.deleteRole(id);
		 return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(), this.messageResolver.getMessage("role.delete"), 
				 null, null), HttpStatus.OK);
	}

}
