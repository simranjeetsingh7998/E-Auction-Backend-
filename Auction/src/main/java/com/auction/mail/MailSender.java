package com.auction.mail;

import java.util.Date;

import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.auction.util.CommonUtils;

@Service
public class MailSender implements IMailSender {

	@Autowired
	private JavaMailSender jmmailSender;
	
	@Autowired
	private IMailStatusDao mailStatusDao;

	
	public void sendMail(final EmailObject emailBO) {
		EmailServiceThread sendMail = new EmailServiceThread(emailBO,setMailStatus(emailBO));
		sendMail.start();
	}
	
	private MailStatus setMailStatus(final EmailObject emailBO) {
		MailStatus mailStatus = new MailStatus();
		mailStatus.setUserId(emailBO.getUserId());
		mailStatus.setEmailType(emailBO.getEmailType());
		mailStatus.setImagePathList(StringUtils.join(emailBO.getImagePathList(), ','));
		if(CommonUtils.isNotEmpty(emailBO.getMailBody())) {
			mailStatus.setMailBody(emailBO.getMailBody().getBytes());
		}
		mailStatus.setRecipientList(StringUtils.join(emailBO.getRecipientList(), ','));
		mailStatus.setSentDate(new Date());
		mailStatus.setSubject(emailBO.getSubject());
		mailStatus.setCcList(StringUtils.join(emailBO.getCcList(), ','));
		mailStatus.setBccList(StringUtils.join(emailBO.getBccList(), ','));
		mailStatus.setIsSuccess(true);
		return mailStatus;
	}

	private class EmailServiceThread extends Thread {

		private EmailObject emailBO = null;
		private MailStatus mailStatus = null;

		public EmailServiceThread(EmailObject emailBO,MailStatus mailStatus) {
			this.emailBO = emailBO;
			this.mailStatus = mailStatus;
			
		}
		
		public void run() {

			try {
				final MimeMessage message = jmmailSender.createMimeMessage();
				final MimeMessageHelper messageHelper = new MimeMessageHelper(
						message, true, "UTF-8");
				if (emailBO == null
						|| emailBO.getRecipientList() == null
						|| emailBO.getRecipientList().length == Integer
								.valueOf(0)) {
					throw new RuntimeException();
				}
				messageHelper.setTo(emailBO.getRecipientList());

				if (!CommonUtils.isEmpty(emailBO.getCcList())) {
					messageHelper.setCc(emailBO.getCcList());
				}

				if (!CommonUtils.isEmpty(emailBO.getBccList())) {
					messageHelper.setBcc(emailBO.getBccList());
				}

				messageHelper
						.setFrom(!CommonUtils.isEmpty(emailBO.getSender()) ? emailBO
								.getSender() : "");
				messageHelper
						.setSentDate(emailBO.getSentDate() != null ? emailBO
								.getSentDate() : new Date());

				messageHelper.setSubject(!CommonUtils.isEmpty(emailBO
							.getSubject()) ? emailBO.getSubject() : "");
				
				messageHelper.setText(emailBO.getMailBody(), true);
				
				if(CommonUtils.isNotEmpty(emailBO.getAttachments())) {
					for(String attachment : emailBO.getAttachments()) {
						FileSystemResource file = new FileSystemResource(attachment);
						messageHelper.addAttachment(file.getFilename(), file);
					}
				}
				
				if (CommonUtils.isNotEmpty(emailBO.getImagePathList())) {
					int i=0;
					for (String imagePath : emailBO.getImagePathList()) {
						i++;
						 DataSource fds = new FileDataSource(imagePath);
			                messageHelper.addInline("image"+i, fds);
					}
				}
				
				jmmailSender.send(messageHelper.getMimeMessage());
				System.out.println("Mail Sent!!!!!!!");
			} catch (final Exception e) {
				System.out.println("Exception");
				e.printStackTrace();
				mailStatus.setIsSuccess(false);
				mailStatus.setException(e.getMessage());
				throw new RuntimeException("sendMailFailed");
			}
			finally {
				try {
					mailStatusDao.save(mailStatus);
				} catch (Exception e) {
					e.printStackTrace();
				}
	        }
			

		}

		/**
		 * @param message
		 * @param emailBO
		 * @throws MessagingException
		 */
		@SuppressWarnings("unused")
		private void setHTMLContent(final MimeMessage message,
				final EmailObject emailBO) throws MessagingException {

			final String html = emailBO.getMailBody() != null ? emailBO
					.getMailBody() : "";

			message.setContent(html, "text/html; charset=\"UTF-8\"");
		}

		/**
		 * @param message
		 * @param emailBO
		 * @throws MessagingException
		 */
		@SuppressWarnings("unused")
		private void setTextContent(final MimeMessage message,
				final EmailObject emailBO) throws MessagingException {
			message.setText(emailBO.getMailBody(), "UTF-8");
			message.setContent(emailBO.getMailBody(), "text/plain");
		}

	}

}
