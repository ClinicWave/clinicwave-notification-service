package com.clinicwave.clinicwavenotificationservice.service.impl;

import com.clinicwave.clinicwavenotificationservice.domain.SmtpSetting;
import com.clinicwave.clinicwavenotificationservice.exception.ResourceNotFoundException;
import com.clinicwave.clinicwavenotificationservice.repository.SmtpSettingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Optional;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * This class contains the unit tests for the SmtpSettingServiceImpl class.
 * It uses Mockito to mock the SmtpSettingRepository and test the methods in the SmtpSettingServiceImpl class.
 * The tests verify that the createMailSender method creates a JavaMailSender object using the active SmtpSetting entity.
 *
 * @author aamir on 7/10/24
 */
@ExtendWith(MockitoExtension.class)
class SmtpSettingServiceImplTest {
  @Mock
  private SmtpSettingRepository smtpSettingRepository;

  @InjectMocks
  private SmtpSettingServiceImpl smtpSettingService;

  private SmtpSetting smtpSetting;

  /**
   * Sets up the test environment before each test.
   */
  @BeforeEach
  void setUp() {
    smtpSetting = new SmtpSetting();
    smtpSetting.setHost("smtp.example.com");
    smtpSetting.setPort(587);
    smtpSetting.setUsername("testuser");
    smtpSetting.setPassword("password");
    smtpSetting.setAuth(true);
    smtpSetting.setStarttlsEnable(true);
  }

  @Test
  @DisplayName("createMailSender creates a JavaMailSender object")
  void createMailSender() {
    when(smtpSettingRepository.findByIsActiveTrue()).thenReturn(Optional.of(smtpSetting));

    JavaMailSender javaMailSender = smtpSettingService.createMailSender();

    assertNotNull(javaMailSender);
    assertInstanceOf(JavaMailSenderImpl.class, javaMailSender);

    JavaMailSenderImpl mailSender = (JavaMailSenderImpl) javaMailSender;

    assertEquals(smtpSetting.getHost(), mailSender.getHost());
    assertEquals(smtpSetting.getPort(), mailSender.getPort());
    assertEquals(smtpSetting.getUsername(), mailSender.getUsername());
    assertEquals(smtpSetting.getPassword(), mailSender.getPassword());

    Properties props = mailSender.getJavaMailProperties();
    assertEquals("smtp", props.getProperty("mail.transport.protocol"));
    assertEquals(smtpSetting.getAuth().toString(), props.getProperty("mail.smtp.auth"));
    assertEquals(smtpSetting.getStarttlsEnable().toString(), props.getProperty("mail.smtp.starttls.enable"));

    verify(smtpSettingRepository, times(1)).findByIsActiveTrue();
  }

  @Test
  @DisplayName("getActiveSmtpSetting throws ResourceNotFoundException when no active setting")
  void getActiveSmtpSettingThrowsResourceNotFoundExceptionWhenNoActiveSetting() {
    when(smtpSettingRepository.findByIsActiveTrue()).thenReturn(Optional.empty());

    assertThrows(ResourceNotFoundException.class, () -> smtpSettingService.createMailSender());
    verify(smtpSettingRepository, times(1)).findByIsActiveTrue();
  }
}