package com.clinicwave.clinicwavenotificationservice.strategy;

import com.clinicwave.clinicwavenotificationservice.dto.NotificationRequestDto;
import com.clinicwave.clinicwavenotificationservice.enums.NotificationCategoryEnum;
import com.clinicwave.clinicwavenotificationservice.enums.NotificationTypeEnum;
import com.clinicwave.clinicwavenotificationservice.exception.EmailSendingException;
import com.clinicwave.clinicwavenotificationservice.exception.TemplateProcessingException;
import com.clinicwave.clinicwavenotificationservice.service.SmtpSettingService;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.thymeleaf.exceptions.TemplateInputException;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * This class tests the EmailNotificationStrategy class.
 *
 * @author aamir on 7/14/24
 */
@ExtendWith(MockitoExtension.class)
class EmailNotificationStrategyTest {
  @Mock
  private SpringTemplateEngine springTemplateEngine;

  @Mock
  private SmtpSettingService smtpSettingService;

  @Mock
  private JavaMailSender javaMailSender;

  @Mock
  private MimeMessage mimeMessage;

  @InjectMocks
  private EmailNotificationStrategy emailNotificationStrategy;

  private NotificationRequestDto notificationRequestDto;

  /**
   * Sets up the test environment by initializing the notification request DTO.
   */
  @BeforeEach
  void setUp() {
    notificationRequestDto = new NotificationRequestDto(
            "test@example.com",
            "Test Subject",
            "test-template",
            Map.of("key", "value"),
            NotificationTypeEnum.EMAIL,
            NotificationCategoryEnum.VERIFICATION
    );
  }

  @Test
  @DisplayName("Test getType()")
  void testGetType() {
    assertEquals(NotificationTypeEnum.EMAIL, emailNotificationStrategy.getType());
  }

  @Test
  @DisplayName("Test send() - Successful")
  void testSendSuccessful() {
    when(smtpSettingService.createMailSender()).thenReturn(javaMailSender);
    when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
    when(springTemplateEngine.process(eq("email/test-template"), any())).thenReturn("Processed HTML Content");

    emailNotificationStrategy.send(notificationRequestDto);

    verify(javaMailSender, times(1)).send(mimeMessage);
  }

  @Test
  @DisplayName("Test send() - Throws EmailSendingException")
  void testSendThrowsEmailSendingException() {
    when(smtpSettingService.createMailSender()).thenReturn(javaMailSender);
    when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
    when(springTemplateEngine.process(eq("email/test-template"), any())).thenReturn("Processed HTML Content");
    doThrow(new MailSendException("Sending failed")).when(javaMailSender).send(any(MimeMessage.class));

    EmailSendingException exception = assertThrows(EmailSendingException.class,
            () -> emailNotificationStrategy.send(notificationRequestDto));
    assertTrue(exception.getMessage().contains("test@example.com"));
    assertTrue(exception.getMessage().contains("Test Subject"));
    assertTrue(exception.getMessage().contains("Sending failed"));
  }

  @Test
  @DisplayName("Test send() - Throws TemplateProcessingException")
  void testSendThrowsTemplateProcessingException() {
    when(smtpSettingService.createMailSender()).thenReturn(javaMailSender);
    when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
    when(springTemplateEngine.process(eq("email/test-template"), any())).thenThrow(new TemplateInputException("Template processing failed"));

    TemplateProcessingException exception = assertThrows(TemplateProcessingException.class,
            () -> emailNotificationStrategy.send(notificationRequestDto));
    assertTrue(exception.getMessage().contains("test-template"));
    assertTrue(exception.getMessage().contains("Template processing failed"));
  }
}