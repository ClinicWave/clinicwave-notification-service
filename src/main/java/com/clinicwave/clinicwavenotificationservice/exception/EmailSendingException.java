package com.clinicwave.clinicwavenotificationservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * This exception is thrown when an email fails to send.
 * It is annotated with @ResponseStatus to automatically return a HttpStatus.SERVICE_UNAVAILABLE when thrown.
 * The exception takes in recipient, subject, and errorDetails as parameters to construct a detailed error message.
 *
 * @author aamir on 7/14/24
 */
@ResponseStatus(value = HttpStatus.SERVICE_UNAVAILABLE, reason = "Email Sending Failed")
public class EmailSendingException extends RuntimeException {
  /**
   * Constructor for dependency injection.
   *
   * @param recipient    The recipient of the email.
   * @param subject      The subject of the email.
   * @param errorDetails The details of the error that occurred.
   */
  public EmailSendingException(String recipient, String subject, String errorDetails) {
    super(String.format("Failed to send email to '%s' with subject '%s'. Error: %s", recipient, subject, errorDetails)
    );
  }
}
