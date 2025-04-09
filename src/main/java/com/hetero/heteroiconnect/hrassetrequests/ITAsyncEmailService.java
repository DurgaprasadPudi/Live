package com.hetero.heteroiconnect.hrassetrequests;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class ITAsyncEmailService {

	private static final Logger logger = LoggerFactory.getLogger(ITAsyncEmailService.class);
	private final MailConfig mailConfig;
	private final AssetRepository employeeRepository;
	private final TemplateEngine templateEngine;

	public ITAsyncEmailService(MailConfig mailConfig, AssetRepository employeeRepository,
			TemplateEngine templateEngine) {
		this.mailConfig = mailConfig;
		this.employeeRepository = employeeRepository;
		this.templateEngine = templateEngine;
	}

	@Async
	public void sendEmail(Integer loginId, List<Object[]> emailDataList, List<Object[]> assetNames, Integer requestId) {
		if (emailDataList.isEmpty()) {
			logger.warn("No email data found for loginId: {}", loginId);
			return;
		}
		try {
			Object[] emailData = emailDataList.get(0);
			String toEmails = (String) emailData[1];
			String hrName = (String) emailData[2];
			String ccEmails = (String) emailData[3];
			String fromEmail = (String) emailData[4];
			String smtpPassword = (String) emailData[5];
			String smtpHost = (String) emailData[6];
			int smtpPort = Integer.parseInt(emailData[9].toString());
			boolean tls = Boolean.parseBoolean(emailData[7].toString());
			String empName = (String) emailData[10];
			String depName = (String) emailData[11];
			String reportingManagerId = (String) emailData[12];
			String reportingManagerName = (String) emailData[13];
			String date = (String) emailData[14];

			String[] toEmailArray = toEmails != null && !toEmails.trim().isEmpty() ? toEmails.split(",")
					: new String[0];
			String[] ccEmailArray = ccEmails != null && !ccEmails.trim().isEmpty() ? ccEmails.split(",")
					: new String[0];

			if (toEmailArray.length == 0) {
				logger.warn("No recipient email provided, skipping email send for requestId: {}", requestId);
				return;
			}

			String subject = "Asset Request";

			Context context = new Context(Locale.ENGLISH);
			context.setVariable("loginId", loginId);
			context.setVariable("hrName", capitalizeEachWord(hrName));
			context.setVariable("requestId", requestId);
			context.setVariable("empName", capitalizeEachWord(empName));
			context.setVariable("depName", capitalizeEachWord(depName));
			context.setVariable("reportingManagerId", reportingManagerId);
			context.setVariable("reportingManagerName", capitalizeEachWord(reportingManagerName));
			context.setVariable("date", date);
			context.setVariable("assetNames", assetNames);

			String emailBody = templateEngine.process("itscreen", context);

			JavaMailSender mailSender = mailConfig.createDynamicMailSender(smtpHost, smtpPort, fromEmail, smtpPassword,
					tls);
			sendEmailWithJavaMailSender(mailSender, fromEmail, toEmailArray, ccEmailArray, subject, emailBody,
					requestId);
		} catch (Exception e) {
			employeeRepository.updateMailStatus("F", e.getMessage(), requestId);
			logger.error("Error sending email for loginId: {}", loginId, e);
		}
	}

	private String capitalizeEachWord(String str) {
		if (str == null || str.trim().isEmpty()) {
			return "";
		}
		return Arrays.stream(str.trim().split("\\s+"))
				.map(word -> word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase())
				.collect(Collectors.joining(" "));
	}

	private void sendEmailWithJavaMailSender(JavaMailSender mailSender, String fromEmail, String[] toEmails,
			String[] ccEmails, String subject, String body, Integer requestId) {
		try {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			helper.setFrom(fromEmail);
			//helper.setTo("durgaprasad.p@heterohealthcare.com");
			helper.setTo(toEmails);
			if (ccEmails.length > 0) {
				helper.setCc(ccEmails);
				//helper.setCc("upendra.bellamkonda@heterohealthcare.com");
			}
			helper.setSubject(subject);
			helper.setText(body, true);
			mailSender.send(message);
			logger.info("Email successfully sent to: {}", Arrays.toString(toEmails));
			logger.info("CC Email successfully sent to: {}", Arrays.toString(ccEmails));
			employeeRepository.updateMailStatus("S", "Success", requestId);
		} catch (Exception e) {
			employeeRepository.updateMailStatus("F", e.getMessage(), requestId);
			logger.error("Error sending email: {}", e.getMessage(), e);
		}
	}
}
