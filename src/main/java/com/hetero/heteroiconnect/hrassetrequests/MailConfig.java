package com.hetero.heteroiconnect.hrassetrequests;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

@Service
public class MailConfig {

	public JavaMailSender createDynamicMailSender(String smtpHost, int smtpPort, String smtpUsername,
			String smtpPassword, boolean smtpTLS) {
		try {
			JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
			mailSender.setHost(smtpHost);
			mailSender.setPort(smtpPort);
			mailSender.setUsername(smtpUsername);
			mailSender.setPassword(smtpPassword);
			mailSender.getJavaMailProperties().put("mail.smtp.auth", "true");
			mailSender.getJavaMailProperties().put("mail.smtp.starttls.enable", "true");
			mailSender.getJavaMailProperties().put("mail.smtp.ssl.protocols", smtpTLS);
			return mailSender;
		} catch (Exception e) {
			throw new RuntimeException("Error configuring mail sender", e);
		}
	}
}
