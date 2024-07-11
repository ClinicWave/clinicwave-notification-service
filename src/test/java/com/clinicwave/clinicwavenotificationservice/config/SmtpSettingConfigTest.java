package com.clinicwave.clinicwavenotificationservice.config;

import com.clinicwave.clinicwavenotificationservice.domain.SmtpSetting;
import com.clinicwave.clinicwavenotificationservice.repository.SmtpSettingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

/**
 * This class contains the unit tests for the SmtpSettingConfig class.
 * The tests verify that the initializeSmtpSettings method creates default settings when none exist.
 *
 * @author aamir on 7/8/24
 */
@ExtendWith(MockitoExtension.class)
class SmtpSettingConfigTest {
  @Mock
  private SmtpSettingRepository smtpSettingRepository;

  @InjectMocks
  private SmtpSettingConfig smtpSettingConfig;

  /**
   * Sets up the test environment before each test.
   */
  @BeforeEach
  void setUp() {
    smtpSettingConfig = new SmtpSettingConfig(smtpSettingRepository);
  }

  @Test
  @DisplayName("initializeSmtpSettings creates default settings when none exist")
  void initializeSmtpSettingsCreatesDefaultSettingsWhenNoneExist() {
    when(smtpSettingRepository.count()).thenReturn(0L);

    smtpSettingConfig.initializeSmtpSettings();

    verify(smtpSettingRepository, times(1)).save(any(SmtpSetting.class));
  }

  @Test
  @DisplayName("initializeSmtpSettings does nothing when settings already exist")
  void initializeSmtpSettingsDoesNothingWhenSettingsAlreadyExist() {
    when(smtpSettingRepository.count()).thenReturn(1L);

    smtpSettingConfig.initializeSmtpSettings();

    verify(smtpSettingRepository, never()).save(any(SmtpSetting.class));
  }
}