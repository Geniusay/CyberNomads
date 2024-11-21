package io.github.geniusay.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.mail.*;
import javax.mail.internet.MimeMessage;
import java.util.Properties;


@Component
public class EmailUtil {
	@Value("${spring.mail.username}")
	private String from;
	@Resource
	private String htmlContent;  // HTML模板内容
	@Resource
	private JavaMailSenderImpl mailSender;

	public EmailUtil() {
	}

	@PostConstruct
	public void init(){
		Properties properties = new Properties();
		properties.setProperty("mail.smtp.auth", "true");//开启认证
//		properties.setProperty("mail.debug", "true");//启用调试
		properties.setProperty("mail.smtp.timeout", "200000");//设置链接超时
		properties.setProperty("mail.smtp.port", Integer.toString(25));//设置端口
		properties.setProperty("mail.smtp.socketFactory.port", Integer.toString(465));//设置ssl端口
		properties.setProperty("mail.smtp.socketFactory.fallback", "false");
		properties.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		mailSender.setJavaMailProperties(properties);
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
