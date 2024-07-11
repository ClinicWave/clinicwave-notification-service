package com.clinicwave.clinicwavenotificationservice.service.impl;

import com.clinicwave.clinicwavenotificationservice.domain.SmtpSetting;
import com.clinicwave.clinicwavenotificationservice.exception.ResourceNotFoundException;
import com.clinicwave.clinicwavenotificationservice.repository.SmtpSettingRepository;
import com.clinicwave.clinicwavenotificationservice.service.SmtpSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import java.util.Properties;

/**
 * This class is a service class for the SmtpSetting entity.
 * It provides methods to create a JavaMailSender object.
 *
 * @author aamir on 7/8/24
 */
@Service
public class SmtpSettingServiceImpl implements SmtpSettingService {
  private final SmtpSettingRepository smtpSettingRepository;

  /**
   * Constructor for dependency injection.
   *
   * @param smtpSettingRepository The repository for SmtpSetting entities.
   */
  @Autowired
  public SmtpSettingServiceImpl(SmtpSettingRepository smtpSettingRepository) {
    this.smtpSettingRepository = smtpSettingRepository;
  }

  /**
   * Creates a JavaMailSender object using the active SmtpSetting entity.
   *
   * @return A JavaMailSender object.
   */
  @Override
  public JavaMailSender createMailSender() {
    SmtpSetting setting = getActiveSmtpSetting();
    JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
    mailSender.setHost(setting.getHost());
    mailSender.setPort(setting.getPort());
    mailSender.setUsername(setting.getUsername());
    mailSender.setPassword(setting.getPassword());

    Properties props = mailSender.getJavaMailProperties();
    props.put("mail.transport.protocol", "smtp");
    props.put("mail.smtp.auth", setting.getAuth().toString());
    props.put("mail.smtp.starttls.enable", setting.getStarttlsEnable().toString());

    return mailSender;
  }

  /**
   * Retrieves the active SmtpSetting entity.
   *
   * @return The active SmtpSetting entity.
   * @throws ResourceNotFoundException If no active SmtpSetting entity is found.
   */
  private SmtpSetting getActiveSmtpSetting() {
    return smtpSettingRepository.findByIsActiveTrue()
            .orElseThrow(() -> new ResourceNotFoundException("SmtpSetting", "isActive", "true"));
  }
}
