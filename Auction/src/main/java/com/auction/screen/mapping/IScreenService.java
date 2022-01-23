package com.auction.screen.mapping;

import java.util.List;

public interface IScreenService {
	
	void save(ScreenVO screenVO);
	
	List<ScreenVO> findByRole();
	
	void mapScreensToRole(ScreenRoleMappingVO screenRoleMappingVO);
	
	List<ScreenVO> findAllWithMappedByRole(Integer roleId);

}
