package com.clinicwave.clinicwavenotificationservice.config;

import com.clinicwave.clinicwavenotificationservice.domain.SmtpSetting;
import com.clinicwave.clinicwavenotificationservice.repository.SmtpSettingRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

/**
 * This class is a configuration class for the SmtpSetting entity.
 * It initializes the SmtpSetting entity with default values.
 *
 * @author aamir on 7/8/24
 */
@Configuration
public class SmtpSettingConfig {
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
      smtpSetting.setHost("localhost");
      smtpSetting.setPort(1025);
      smtpSetting.setUsername("");
      smtpSetting.setPassword("");
      smtpSetting.setAuth(false);
      smtpSetting.setStarttlsEnable(false);
      smtpSetting.setIsActive(true);
      smtpSettingRepository.save(smtpSetting);
    }
  }
}
