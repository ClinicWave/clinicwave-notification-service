package com.clinicwave.clinicwavenotificationservice.service;

import org.springframework.mail.javamail.JavaMailSender;

/**
 * This interface is a service for SmtpSetting entities.
 *
 * @author aamir on 7/8/24
 */
public interface SmtpSettingService {
  JavaMailSender createMailSender();
}
