package com.clinicwave.clinicwavenotificationservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * This exception is thrown when an email template fails to process.
 * It is annotated with @ResponseStatus to automatically return a HttpStatus.INTERNAL_SERVER_ERROR when thrown.
 * The exception takes in templateName and errorDetails as parameters to construct a detailed error message.
 *
 * @author aamir on 7/14/24
 */
@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Email Template Processing Failed")
public class TemplateProcessingException extends RuntimeException {
  /**
   * Constructor for dependency injection.
   *
   * @param templateName The name of the email template.
   * @param errorDetails The details of the error that occurred.
   */
  public TemplateProcessingException(String templateName, String errorDetails) {
    super(String.format("Failed to process email template '%s'. Error: %s", templateName, errorDetails)
    );
  }
}
