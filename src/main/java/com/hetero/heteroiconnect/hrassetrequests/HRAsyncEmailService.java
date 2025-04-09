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
public class HRAsyncEmailService {

	private static final Logger logger = LoggerFactory.getLogger(HRAsyncEmailService.class);
	private final MailConfig mailConfig;
	private final AssetRepository employeeRepository;
	private final TemplateEngine templateEngine;

	public HRAsyncEmailService(MailConfig mailConfig, AssetRepository employeeRepository,
			TemplateEngine templateEngine) {
		this.mailConfig = mailConfig;
		this.employeeRepository = employeeRepository;
		this.templateEngine = templateEngine;
	}

	@Async
	public void sendEmail(List<Object[]> emailDataList, List<Object[]> assetNames, String requestId) {
		Integer refRequestId = Integer.parseInt(requestId);
		if (emailDataList.isEmpty()) {
			logger.warn("No email data found for requestID: {}", requestId);
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
			Integer ackId = (Integer) emailData[10];
			String ackName = (String) emailData[11];
			String empName = (String) emailData[12];
			String deptName = (String) emailData[13];
			String date = (String) emailData[14];
			Integer repId = (Integer) emailData[15];
			String repName = (String) emailData[16];

			String[] toEmailArray = toEmails != null && !toEmails.trim().isEmpty() ? toEmails.split(",")
					: new String[0];
			String[] ccEmailArray = ccEmails != null && !ccEmails.trim().isEmpty() ? ccEmails.split(",")
					: new String[0];

			if (toEmailArray.length == 0) {
				logger.warn("No recipient email provided, skipping email send for requestId: {}", requestId);
				return;
			}
			String subject = "Asset Acknowledgement";
			Context context = new Context(Locale.ENGLISH);
			context.setVariable("hrName", capitalizeEachWord(hrName));
			context.setVariable("requestId", requestId);
			context.setVariable("empName", capitalizeEachWord(empName));
			context.setVariable("depName", capitalizeEachWord(deptName));
			context.setVariable("repId", repId);
			context.setVariable("repName", capitalizeEachWord(repName));
			context.setVariable("date", date);
			context.setVariable("assetNames", assetNames);
			context.setVariable("ackId", ackId);
			context.setVariable("ackName", capitalizeEachWord(ackName));
			context.setVariable("assetNames", assetNames);

			String emailBody = templateEngine.process("hrscreen", context);

			JavaMailSender mailSender = mailConfig.createDynamicMailSender(smtpHost, smtpPort, fromEmail, smtpPassword,
					tls);
			sendEmailWithJavaMailSender(mailSender, fromEmail, toEmailArray, ccEmailArray, subject, emailBody,
					refRequestId);
		} catch (Exception e) {
			employeeRepository.updateHRMailStatus("F", e.getMessage(), refRequestId);
			logger.error("Error sending email for requestID: {}", requestId, e);
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
			helper.setTo(toEmails);
			//helper.setTo("durgaprasad.p@heterohealthcare.com");
			if (ccEmails.length > 0) {
				helper.setCc(ccEmails);
				//helper.setCc("durgaprasad.p@heterohealthcare.com");
			}
			helper.setSubject(subject);
			helper.setText(body, true);
			mailSender.send(message);
			logger.info("Email successfully sent to: {}", Arrays.toString(toEmails));
			logger.info("CC Email successfully sent to: {}", Arrays.toString(ccEmails));
			employeeRepository.updateHRMailStatus("S", "Success", requestId);
		} catch (Exception e) {
			employeeRepository.updateHRMailStatus("F", e.getMessage(), requestId);
			logger.error("Error sending email: {}", e.getMessage(), e);
		}
	}
}
