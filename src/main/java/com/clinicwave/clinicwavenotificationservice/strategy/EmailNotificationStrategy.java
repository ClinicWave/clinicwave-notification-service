package com.clinicwave.clinicwavenotificationservice.strategy;

import com.clinicwave.clinicwavenotificationservice.dto.NotificationRequestDto;
import com.clinicwave.clinicwavenotificationservice.enums.NotificationTypeEnum;
import com.clinicwave.clinicwavenotificationservice.exception.EmailSendingException;
import com.clinicwave.clinicwavenotificationservice.exception.TemplateProcessingException;
import com.clinicwave.clinicwavenotificationservice.service.SmtpSettingService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.exceptions.TemplateInputException;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.Map;

/**
 * This class defines the strategy for sending email notifications.
 * It implements the NotificationStrategy interface.
 *
 * @author aamir on 7/10/24
 */
@Component
@Slf4j
public class EmailNotificationStrategy implements NotificationStrategy {
  private final SpringTemplateEngine springTemplateEngine;
  private final SmtpSettingService smtpSettingService;

  /**
   * Constructor for dependency injection.
   *
   * @param smtpSettingService   The service for SmtpSetting entities.
   * @param springTemplateEngine The SpringTemplateEngine to be used for rendering email templates.
   */
  @Autowired
  public EmailNotificationStrategy(SpringTemplateEngine springTemplateEngine, SmtpSettingService smtpSettingService) {
    this.springTemplateEngine = springTemplateEngine;
    this.smtpSettingService = smtpSettingService;
  }

  /**
   * Returns the type of notification strategy.
   *
   * @return The type of notification strategy.
   */
  @Override
  public NotificationTypeEnum getType() {
    return NotificationTypeEnum.EMAIL;
  }

  /**
   * Sends an email notification.
   *
   * @param notificationRequestDto The notification request to be sent.
   */
  @Override
  public void send(NotificationRequestDto notificationRequestDto) {
    log.debug("Sending email notification to: {}", notificationRequestDto.recipient());
    try {
      // Create a JavaMailSender object using the active SmtpSetting entity.
      JavaMailSender mailSender = smtpSettingService.createMailSender();
      MimeMessage mimeMessage = createMimeMessage(mailSender, notificationRequestDto);

      // Send the email.
      mailSender.send(mimeMessage);
      log.info("Email sent successfully to: {}", notificationRequestDto.recipient());
    } catch (MailException | MessagingException e) {
      log.error("Failed to send email to: {}", notificationRequestDto.recipient(), e);
      throw new EmailSendingException(notificationRequestDto.recipient(), notificationRequestDto.subject(), e.getMessage());
    }
  }

  /**
   * Creates a MimeMessage object for sending an email.
   *
   * @param mailSender             The JavaMailSender object to be used for sending the email.
   * @param notificationRequestDto The notification request to be sent.
   * @return The created MimeMessage object.
   * @throws MessagingException If an error occurs while creating the MimeMessage object.
   */
  private MimeMessage createMimeMessage(JavaMailSender mailSender, NotificationRequestDto notificationRequestDto) throws MessagingException {
    // Create a new MimeMessage and MimeMessageHelper object.
    MimeMessage mimeMessage = mailSender.createMimeMessage();
    MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

    // Set the email properties.
    mimeMessageHelper.setTo(notificationRequestDto.recipient());
    mimeMessageHelper.setSubject(notificationRequestDto.subject());

    // Process the email template and set the email content.
    String htmlContent = processTemplate(notificationRequestDto.templateName(), notificationRequestDto.templateVariables());
    mimeMessageHelper.setText(htmlContent, true);

    return mimeMessage;
  }

  /**
   * Processes an email template using Thymeleaf.
   *
   * @param templateName      The name of the template to be processed.
   * @param templateVariables The variables to be used in the template.
   * @return The processed template.
   */
  private String processTemplate(String templateName, Map<String, Object> templateVariables) {
    try {
      // Process the template using Thymeleaf.
      Context context = new Context();
      context.setVariables(templateVariables);
      return springTemplateEngine.process("email/" + templateName, context);
    } catch (TemplateInputException e) {
      throw new TemplateProcessingException(templateName, e.getMessage());
    }
  }
}
