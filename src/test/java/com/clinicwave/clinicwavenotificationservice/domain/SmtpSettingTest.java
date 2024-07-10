package com.clinicwave.clinicwavenotificationservice.domain;

import com.clinicwave.clinicwavenotificationservice.repository.SmtpSettingRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This class is a test class for the SmtpSetting entity.
 *
 * @author aamir on 7/10/24
 */
@SpringBootTest
class SmtpSettingTest {
  private final SmtpSettingRepository smtpSettingRepository;
  private SmtpSetting smtpSetting;

  /**
   * Constructor for dependency injection.
   *
   * @param smtpSettingRepository The repository for SmtpSetting entities.
   */
  @Autowired
  public SmtpSettingTest(SmtpSettingRepository smtpSettingRepository) {
    this.smtpSettingRepository = smtpSettingRepository;
  }

  /**
   * Set up method that runs before each test.
   */
  @BeforeEach
  void setUp() {
    smtpSetting = new SmtpSetting();
    smtpSetting.setHost("smtp.gmail.com");
    smtpSetting.setPort(587);
    smtpSetting.setUsername("aamirshaikh3232@gmail.com");
    smtpSetting.setPassword("password");
    smtpSetting.setAuth(true);
    smtpSetting.setStarttlsEnable(true);
    smtpSetting.setIsActive(true);
    smtpSettingRepository.save(smtpSetting);
  }

  /**
   * Tear down method that runs after each test.
   */
  @AfterEach
  void tearDown() {
    smtpSettingRepository.deleteAll();
  }

  @Test
  @DisplayName("Test save SmtpSetting")
  void testSaveSmtpSetting() {
    smtpSettingRepository.save(smtpSetting);
    assertNotNull(smtpSetting.getId());
  }

  @Test
  @DisplayName("Test get SmtpSetting")
  void testGetSmtpSetting() {
    smtpSettingRepository.save(smtpSetting);
    SmtpSetting savedSmtpSetting = smtpSettingRepository.findById(smtpSetting.getId()).orElse(null);
    assertNotNull(savedSmtpSetting);
    assertEquals(smtpSetting.getHost(), savedSmtpSetting.getHost());
    assertEquals(smtpSetting.getPort(), savedSmtpSetting.getPort());
    assertEquals(smtpSetting.getUsername(), savedSmtpSetting.getUsername());
    assertEquals(smtpSetting.getPassword(), savedSmtpSetting.getPassword());
    assertEquals(smtpSetting.getAuth(), savedSmtpSetting.getAuth());
    assertEquals(smtpSetting.getStarttlsEnable(), savedSmtpSetting.getStarttlsEnable());
    assertEquals(smtpSetting.getIsActive(), savedSmtpSetting.getIsActive());
  }

  @Test
  @DisplayName("Test update SmtpSetting")
  void testUpdateSmtpSetting() {
    smtpSettingRepository.save(smtpSetting);
    smtpSetting.setHost("smtp.mailtrap.io");
    smtpSetting.setPort(2525);
    smtpSetting.setUsername("johndoe@email.com");
    smtpSetting.setPassword("password123");
    smtpSetting.setAuth(false);
    smtpSetting.setStarttlsEnable(false);
    smtpSetting.setIsActive(false);
    smtpSettingRepository.save(smtpSetting);
    SmtpSetting updatedSmtpSetting = smtpSettingRepository.findById(smtpSetting.getId()).orElse(null);
    assertNotNull(updatedSmtpSetting);
    assertEquals(smtpSetting.getHost(), updatedSmtpSetting.getHost());
    assertEquals(smtpSetting.getPort(), updatedSmtpSetting.getPort());
    assertEquals(smtpSetting.getUsername(), updatedSmtpSetting.getUsername());
    assertEquals(smtpSetting.getPassword(), updatedSmtpSetting.getPassword());
    assertEquals(smtpSetting.getAuth(), updatedSmtpSetting.getAuth());
    assertEquals(smtpSetting.getStarttlsEnable(), updatedSmtpSetting.getStarttlsEnable());
    assertEquals(smtpSetting.getIsActive(), updatedSmtpSetting.getIsActive());
  }

  @Test
  @DisplayName("Test delete SmtpSetting")
  void testDeleteSmtpSetting() {
    smtpSettingRepository.save(smtpSetting);
    smtpSettingRepository.deleteById(smtpSetting.getId());
    SmtpSetting deletedSmtpSetting = smtpSettingRepository.findById(smtpSetting.getId()).orElse(null);
    assertNull(deletedSmtpSetting);
  }
}