package com.clinicwave.clinicwavenotificationservice.exception;

import com.clinicwave.clinicwavenotificationservice.dto.ErrorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

/**
 * This class is a global exception handler for the application.
 * It is annotated with @ControllerAdvice, which makes it applicable to all controllers in the application.
 * <p>
 * The class defines several methods, each annotated with @ExceptionHandler and handling a specific type of exception.
 * When an exception to that type is thrown anywhere in the application, the corresponding method in this class will be invoked to handle it.
 * Each method constructs an ErrorResponseDto object containing details about the exception and returns it in the response entity.
 *
 * @author aamir on 7/8/24
 */
@ControllerAdvice
public class GlobalExceptionHandler {
  /**
   * Handles all types of exceptions.
   */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponseDto> handleGlobalException(
          Exception exception,
          WebRequest webRequest
  ) {
    return createErrorResponse(exception, webRequest, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  /**
   * Handles ResourceNotFoundException.
   */
  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<ErrorResponseDto> handleResourceNotFoundException(
          Exception exception,
          WebRequest webRequest
  ) {
    return createErrorResponse(exception, webRequest, HttpStatus.NOT_FOUND);
  }

  /**
   * Creates an ErrorResponseDto object with the specified exception, web request, and status.
   *
   * @param exception  The exception that was thrown.
   * @param webRequest The web request that caused the exception.
   * @param status     The HTTP status code to return.
   * @return An ErrorResponseDto object containing details about the exception.
   */
  private ResponseEntity<ErrorResponseDto> createErrorResponse(
          Exception exception,
          WebRequest webRequest,
          HttpStatus status
  ) {
    ErrorResponseDto errorResponseDto = new ErrorResponseDto(
            webRequest.getDescription(false),
            status.value(),
            exception.getMessage(),
            LocalDateTime.now()
    );

    return new ResponseEntity<>(errorResponseDto, status);
  }
}
