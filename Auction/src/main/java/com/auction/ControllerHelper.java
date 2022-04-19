package com.auction;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.auction.mail.EmailObject;
import com.auction.mail.EmailSetting;
import com.auction.mail.IEmailSettingDao;
import com.auction.mail.IMailSender;
import com.auction.mail.MessageType;
import com.auction.user.User;
import com.auction.util.CommonUtils;
import com.auction.util.Constants;
import com.auction.util.IAuctionConfigDao;

public class ControllerHelper {
	
	@Autowired
	private IEmailSettingDao emailSettingDao;
	
	@Autowired
	private IAuctionConfigDao auctionConfigDao;
	
	@Autowired
	private IMailSender mailSenderDao;
	
	public Map<String, EmailSetting> getCompanyEmailSettings(Integer organizationId) {
		List<EmailSetting> emails = emailSettingDao.findByIdOrganization(organizationId);
		Map<String, EmailSetting> emailMap = new HashMap<>();
		for(EmailSetting email : emails) {
			emailMap.put(email.getEmailType(), email);
		}
		return emailMap;
	}
	
	 public List<String> addEAuctionLogoToImagePath()
	    {
	        List<String> imagePathList = new ArrayList<String>();
	        StringBuilder sbBuffer = new StringBuilder();
	        sbBuffer.append(auctionConfigDao.findByConfigKey(Constants.LOGO_PATH).get().getConfigValue());
	        sbBuffer.append(File.separator);
	        sbBuffer.append("logo-icon.png");
	        imagePathList.add(sbBuffer.toString());
	        return imagePathList;
	    }
	 
	 public void sendFormEmail(User user, String content,EmailSetting setting, List<String> imagePathList){

			if(!CommonUtils.isEmpty(setting)) {
				final EmailObject emailBO = new EmailObject();
				emailBO.setMailBody(CommonUtils.formatMessage(content));
			    emailBO.setMessageType(MessageType.HTML);
			    emailBO.setSubject(setting.getEmailSubject());
			    emailBO.setSender(setting.getFromAddress());
			    emailBO.setSentDate(null);
			    emailBO.setImagePathList(imagePathList);
				emailBO.setUserId(String.valueOf(user.getId()));
				emailBO.setEmailType(setting.getEmailType());
			    
				String[] recipients = null;
			    if(CommonUtils.isNotEmpty(setting.getCcMailAddress())){
			    	emailBO.setBccList(new String[]{setting.getCcMailAddress()});
			    }
			    if(user.getEmail()!=null)
			    {
			    	recipients = new String[]{ user.getEmail()};
			    }
			    emailBO.setRecipientList(recipients);
			    mailSenderDao.sendMail(emailBO);
			}
		}


}
