package com.office.seoul.config;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Configuration
public class MailConfig {

	@Value("${spring.mail.host}")
	private String mailHost;
	
	@Value("${spring.mail.port}")
	private int mailPort;
	
	@Value("${spring.mail.username}")
	private String mailUserName;
	
	@Value("${spring.mail.password}")
	private String mailPassword;
	
	@Value("${spring.mail.properties.mail.smtp.auth}")
	private String mailSmtpAuth;
	
	@Value("${spring.mail.properties.mail.smtp.starttls.enable}")
	private String mailSmtpStarttlsEnable;
	
	@Bean
	public JavaMailSenderImpl mailSender() {
		log.info("findPasswordForm()");
		
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setHost(mailHost);
		mailSender.setPort(mailPort);
		mailSender.setUsername(mailUserName);
		mailSender.setPassword(mailPassword);
		
		Properties properties = new Properties();
		properties.setProperty("mail.smtp.auth", mailSmtpAuth);
		properties.setProperty("mail.smtp.starttls.enable", mailSmtpStarttlsEnable);
		
		mailSender.setJavaMailProperties(properties);
		
		return mailSender;
	}
	
}
