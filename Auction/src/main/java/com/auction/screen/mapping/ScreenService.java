package com.auction.screen.mapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.auction.global.exception.ResourceNotFoundException;
import com.auction.user.IRoleDao;
import com.auction.user.Role;
import com.auction.util.LoggedInUser;

@Service
public class ScreenService implements IScreenService {
	
	@Autowired
	private IScreenDao screenDao;
	
	@Autowired
	private IRoleDao roleDao;
	
	@Autowired
	private IScreenRoleMappingDao screenRoleMappingDao;
	
	@Override
	public void save(ScreenVO screenVO) {
		Screen screen = screenVO.screenVOToScreen();
		if(Objects.isNull(screen.getMenuId()))
				 screen.setMenuId(0);
		this.screenDao.save(screen);
	}
	
	@Override
	public List<ScreenVO> findByRole() {
		Role role = LoggedInUser.getLoggedInUserDetails().getUser().getRole();
		List<Screen> assignedScreens = this.screenDao.findAllByScreenRoleMappings_Role(role);
		Map<Integer, List<Screen>> screenMapByMenuId = assignedScreens.stream().collect(Collectors.groupingBy(Screen::getMenuId));
		if(screenMapByMenuId.size() <= 1 && (screenMapByMenuId.containsKey(null) || screenMapByMenuId.containsKey(0)))
			 return assignedScreens.stream().map(Screen::screenToScreenVO).toList();
		else {
			return this.userHasSubMenu(screenMapByMenuId);
		}
	}
	
	private List<ScreenVO> userHasSubMenu(Map<Integer, List<Screen>> screenMapByMenuId) {
		List<Screen> screens = this.screenDao.findAll();
		List<ScreenVO> screenVOs = new ArrayList<>();
		screenMapByMenuId.forEach((key,value) -> {
			  List<ScreenVO> screenVOListByKey =  value.stream().map(Screen::screenToScreenVO).toList();
			  if(Objects.isNull(key) || key == 0)
				    screenVOs.addAll(screenVOListByKey);
			  else { 
				  ScreenVO screenVO  =  null;
				  while(key != null && key !=0) {
					  screenVO =  getParentMenu(screens, key);
					  screenVO.getScreenVOs().addAll(screenVOListByKey);
					  screenVOListByKey = List.of(screenVO);
					  key = screenVO.getParentMenuId();		
				  }
				  screenVOs.add(screenVO);	  
			  }
		});
	  return screenVOs;	
	}
	
	private ScreenVO getParentMenu(List<Screen> screens, Integer parentMenuId) {
		  Screen parentScreen =  screens.stream().filter(sc -> sc.getId().equals(parentMenuId)).findAny().get(); 
		  return parentScreen.screenToScreenVO();
//		  if(Objects.isNull(parentScreen.getMenuId())||parentScreen.getMenuId() == 0) {
//		   Optional<ScreenVO> filterFromScreenVO = screenVOs.stream().filter(screen -> screen.getId().equals(parentScreen.getId())).findFirst();
//	       System.out.println(filterFromScreenVO.isPresent());
//		   if(filterFromScreenVO.isPresent())
//	    	   screenVO = filterFromScreenVO.get();
//		  }
	}
	
	
	
	
	@Override
	public List<ScreenVO> findAllWithMappedByRole(Integer roleId) {
	  Role role =	this.roleDao.findByIdAndOrganization(roleId, LoggedInUser.getLoggedInUserDetails().getOrganization())
				.orElseThrow(() -> new ResourceNotFoundException("Role not found for your organization"));
		List<Screen> screens = this.screenDao.findAll();
		List<ScreenRole> screenRoles = this.screenRoleMappingDao.findAllByRole(role);
		Map<Integer, List<Screen>> screenMapByMenuId = screens.stream().filter(screen -> screen.getMenuId()!= null && screen.getMenuId()!=0)
				 .collect(Collectors.groupingBy(Screen::getMenuId));
				if(screenMapByMenuId.isEmpty()) {
					return screens.stream().map(screen -> {
						ScreenVO screenVO = screen.screenToScreenVO();
						this.isPreSelected(screenVO, screenRoles);
						return screenVO;
					}).toList();
				}
				else {
					List<ScreenVO> screenVOs = new ArrayList<>();
					for (Screen screen : screens) {
						if(screen.getMenuId()==null || screen.getMenuId() == 0) {
							ScreenVO screenVO = screen.screenToScreenVO();
							this.isPreSelected(screenVO, screenRoles);
							List<Screen> subMenus = screenMapByMenuId.get(screen.getId());
							if(!Objects.isNull(subMenus)) {
								List<ScreenVO> subMenusVO = new ArrayList<>();
								for (Screen subMenu : subMenus) {
									ScreenVO subMenuVO = subMenu.screenToScreenVO();
									this.isPreSelected(subMenuVO, screenRoles);
									if(subMenu.getMenuId() != null && subMenu.getMenuId() !=0) {
										List<Screen> nextSubMenus = screenMapByMenuId.get(subMenu.getId());
										if(!Objects.isNull(nextSubMenus))
										    subMenuVO.setScreenVOs(nextSubMenus.stream().map(nextScreen ->{
										    	ScreenVO nextSubMenu = nextScreen.screenToScreenVO();
										    	this.isPreSelected(nextSubMenu, screenRoles);
										    	return nextSubMenu;
										    }).toList());
									}
								  subMenusVO.add(subMenuVO);	
								}
								screenVO.setScreenVOs(subMenusVO);
							}
						  screenVOs.add(screenVO);
						}
					}
				  return screenVOs;		
				}
	}
	
	private void isPreSelected(ScreenVO screenVO, List<ScreenRole> screenRoles) {
		  Optional<ScreenRole> filteredScreenRole = screenRoles.stream().filter(screenRole -> screenRole.getScreenId() == screenVO.getId()).findFirst();
	      if(filteredScreenRole.isPresent()) {
	    	  Integer mappedId = filteredScreenRole.get().getId();
	    	  screenVO.setMappedId(mappedId);
	    	  screenVO.setMappingStatus(true);
	      }
	}
	
	@Transactional
	@Override
	public void mapScreensToRole(ScreenRoleMappingVO screenRoleMappingVO) {
		Role role =//oggedInUser.getLoggedInUserDetails().getUser().getRole();
				this.roleDao.findByIdAndOrganization(screenRoleMappingVO.getRoleId(), LoggedInUser.getLoggedInUserDetails().getOrganization())
				.orElseThrow(() -> new ResourceNotFoundException("Role not found for your organization"));
		
		List<ScreenVO> screenVOs = screenRoleMappingVO.getScreens();
		
		List<Integer> deleteByIds = screenVOs.stream().filter(screenVO ->
		(!Objects.isNull(screenVO.getMappedId()) && screenVO.getMappedId()!=0) && !screenVO.isMappingStatus())
				.map(ScreenVO::getMappedId).toList();
		if(!deleteByIds.isEmpty()) {
			    deleteByIds.forEach(id -> {
		        	this.screenRoleMappingDao.deleteAllByIdIn(id.toString());
			    });
		} 
		
		
	    List<ScreenRoleMapping> screensToSave =  screenVOs.stream().filter(screenVO ->
	    (Objects.isNull(screenVO.getMappedId()) || screenVO.getMappedId()==0) && screenVO.isMappingStatus())
	    .map(screenVO -> new ScreenRoleMapping(null, role, screenVO.screenVOToScreen())).toList();
	    if(!screensToSave.isEmpty()) {
	    	 this.screenRoleMappingDao.saveAllAndFlush(screensToSave);
	    }
	}

}
