package com.clinicwave.clinicwavenotificationservice.config;

import com.clinicwave.clinicwavenotificationservice.domain.SmtpSetting;
import com.clinicwave.clinicwavenotificationservice.repository.SmtpSettingRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * This class is a configuration class for the SmtpSetting entity.
 * It initializes the SmtpSetting entity with default values.
 *
 * @author aamir on 7/8/24
 */
@Configuration
public class SmtpSettingConfig {
  @Value("${mailtrap.username}")
  private String mailtrapUsername;

  @Value("${mailtrap.password}")
  private String mailtrapPassword;

  private final SmtpSettingRepository smtpSettingRepository;

  /**
   * Constructor for dependency injection.
   *
   * @param smtpSettingRepository The repository for SmtpSetting entities.
   */
  @Autowired
  public SmtpSettingConfig(SmtpSettingRepository smtpSettingRepository) {
    this.smtpSettingRepository = smtpSettingRepository;
  }

  /**
   * Initializes the SmtpSetting entity with default values.
   */
  @PostConstruct
  public void initializeSmtpSettings() {
    if (smtpSettingRepository.count() == 0) {
      SmtpSetting smtpSetting = new SmtpSetting();
      smtpSetting.setHost("sandbox.smtp.mailtrap.io");
      smtpSetting.setPort(2525);
      smtpSetting.setFromAddress("no-reply@clinicwave.com");
      smtpSetting.setUsername(mailtrapUsername);
      smtpSetting.setPassword(mailtrapPassword);
      smtpSetting.setAuth(true);
      smtpSetting.setStarttlsEnable(true);
      smtpSetting.setIsActive(true);
      smtpSettingRepository.save(smtpSetting);
    }
  }
}
