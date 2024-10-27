package io.github.geniusay.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.mail.*;
import javax.mail.internet.MimeMessage;


@Component
public class EmailUtil {
	@Value("${spring.mail.username}")
	private String from;
	@Resource
	private String htmlContent;  // HTML模板内容
	@Resource
	private JavaMailSender mailSender;

	public EmailUtil() {
	}

	public void sendCode(String to, String Code) {
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");

		try {
			helper.setTo(to);
			helper.setFrom(from);
			// 邮件主题
			String subject = "CyberNomads邮箱验证";
			helper.setSubject(subject);
			helper.setText(htmlContent.replace("CODE", Code), true);
			mailSender.send(message);
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}
}
