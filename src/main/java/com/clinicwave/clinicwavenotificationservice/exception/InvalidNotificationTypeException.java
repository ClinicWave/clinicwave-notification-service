package com.clinicwave.clinicwavenotificationservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * This class represents a custom exception that is thrown when an invalid notification type is provided.
 * It is annotated with @ResponseStatus to automatically return a HttpStatus.BAD_REQUEST when thrown.
 * The exception takes in resourceName, fieldName, and fieldValue as parameters to construct a detailed error message.
 *
 * @author aamir on 7/14/24
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Invalid notification type")
public class InvalidNotificationTypeException extends RuntimeException {
  /**
   * Constructor for dependency injection.
   *
   * @param resourceName The name of the resource.
   * @param fieldName    The name of the field.
   * @param fieldValue   The value of the field.
   */
  public InvalidNotificationTypeException(String resourceName, String fieldName, Object fieldValue) {
    super(String.format("%s not supported with %s : '%s'", resourceName, fieldName, fieldValue));
  }
}
