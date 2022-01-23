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
		this.screenDao.save(screenVO.screenVOToScreen());
	}
	
	@Override
	public List<ScreenVO> findByRole() {
		Role role = LoggedInUser.getLoggedInUserDetails().getUser().getRole();
		List<Screen> screens = this.screenDao.findAllByScreenRoleMappings_Role(role);
		Map<Integer, List<Screen>> screenMapByMenuId = screens.stream().filter(screen -> screen.getMenuId()!= null && screen.getMenuId()!=0)
		 .collect(Collectors.groupingBy(Screen::getMenuId));
		if(screenMapByMenuId.isEmpty())
			 return screens.stream().map(Screen::screenToScreenVO).toList();
		else {
			List<ScreenVO> screenVOs = new ArrayList<>();
			for (Screen screen : screens) {
				if(screen.getMenuId()==null && screen.getMenuId() == 0) {
					ScreenVO screenVO = screen.screenToScreenVO();
					List<Screen> subMenus = screenMapByMenuId.get(screen.getId());
					if(!Objects.isNull(subMenus)) {
						List<ScreenVO> subMenusVO = new ArrayList<>();
						for (Screen subMenu : subMenus) {
							ScreenVO subMenuVO = subMenu.screenToScreenVO();
							if(subMenu.getMenuId() != null && subMenu.getMenuId() !=0) {
								List<Screen> nextSubMenus = screenMapByMenuId.get(subMenu.getId());
								if(!Objects.isNull(nextSubMenus))
								    subMenuVO.setScreenVOs(nextSubMenus.stream().map(Screen::screenToScreenVO).toList());
							}
						  subMenusVO.add(subMenuVO);	
						}
					}
				  screenVOs.add(screenVO);
				}
			}
		  return screenVOs;		
		}
	}
	
	
	@Override
	public List<ScreenVO> findAllWithMappedByRole(Integer roleId) {
	  Role role =	this.roleDao.findByIdAndOrganization(roleId, LoggedInUser.getLoggedInUserDetails().getOrganization())
				.orElseThrow(() -> new ResourceNotFoundException("Role not found for your organization"));
		List<Screen> screens = this.screenDao.findAll();
	//	Role role = new Role();
	//	role.setId(roleId);
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
						if(screen.getMenuId()==null && screen.getMenuId() == 0) {
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
							}
						  screenVOs.add(screenVO);
						}
					}
				  return screenVOs;		
				}
	}
	
	private void isPreSelected(ScreenVO screenVO, List<ScreenRole> screenRoles) {
		  Optional<ScreenRole> filteredScreenRole = screenRoles.stream().filter(screenRole -> screenRole.getScreenId()== screenVO.getId()).findFirst();
	      if(filteredScreenRole.isPresent()) {
	    	  Integer mappedId = filteredScreenRole.get().getId();
	    	  screenVO.setMappedId(mappedId);
	    	  screenVO.setMappingStatus(true);
	      }
	}
	
	@Transactional
	@Override
	public void mapScreensToRole(ScreenRoleMappingVO screenRoleMappingVO) {
		Role role = //LoggedInUser.getLoggedInUserDetails().getUser().getRole();
				this.roleDao.findByIdAndOrganization(screenRoleMappingVO.getRoleId(), LoggedInUser.getLoggedInUserDetails().getOrganization())
				.orElseThrow(() -> new ResourceNotFoundException("Role not found for your organization"));
		
		List<ScreenVO> screenVOs = screenRoleMappingVO.getScreens();
		
		List<Integer> deleteByIds = screenVOs.stream().filter(screenVO ->
		(!Objects.isNull(screenVO.getMappedId()) && screenVO.getMappedId()!=0)&& !screenVO.isMappingStatus())
				.map(ScreenVO::getMappedId).toList();
		if(!deleteByIds.isEmpty()) {
			    deleteByIds.forEach(id ->{
		        	this.screenRoleMappingDao.deleteAllByIdIn(id.toString());
			    });
		}  	
	    List<ScreenRoleMapping> screensToSave =  screenVOs.stream().filter(screenVO -> (Objects.isNull(screenVO.getMappedId()) || screenVO.getMappedId()==0) && screenVO.isMappingStatus())
	    .map(screenVO -> new ScreenRoleMapping(null, role, screenVO.screenVOToScreen())).toList();
	    if(!screensToSave.isEmpty()) {
	    	 this.screenRoleMappingDao.saveAllAndFlush(screensToSave);
	    }
	}

}
