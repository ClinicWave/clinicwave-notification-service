package com.clinicwave.clinicwavenotificationservice.controller;

import com.clinicwave.clinicwavenotificationservice.dto.NotificationRequestDto;
import com.clinicwave.clinicwavenotificationservice.enums.NotificationCategoryEnum;
import com.clinicwave.clinicwavenotificationservice.enums.NotificationTypeEnum;
import com.clinicwave.clinicwavenotificationservice.exception.EmailSendingException;
import com.clinicwave.clinicwavenotificationservice.service.NotificationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * This class contains unit tests for the NotificationController class.
 *
 * @author aamir on 7/20/24
 */
@SpringBootTest
@AutoConfigureMockMvc
class NotificationControllerTest {
  private final MockMvc mockMvc;
  private final ObjectMapper objectMapper;

  @MockBean
  private NotificationService notificationService;

  private NotificationRequestDto notificationRequestDto;

  /**
   * Constructor-based dependency injection.
   *
   * @param mockMvc      The MockMvc object
   * @param objectMapper The ObjectMapper object
   */
  @Autowired
  public NotificationControllerTest(MockMvc mockMvc, ObjectMapper objectMapper) {
    this.mockMvc = mockMvc;
    this.objectMapper = objectMapper;
  }

  /**
   * Sets up the test environment before each test.
   */
  @BeforeEach
  void setUp() {
    Map<String, Object> templateVariables = new HashMap<>();
    templateVariables.put("name", "John Doe");
    templateVariables.put("verificationCode", "123456");

    notificationRequestDto = new NotificationRequestDto(
            "test@example.com",
            "Test Notification",
            "test-template",
            templateVariables,
            NotificationTypeEnum.EMAIL,
            NotificationCategoryEnum.VERIFICATION
    );
  }

  @Test
  @DisplayName("POST /api/notifications/send - Success")
  void testSendNotificationSuccess() throws Exception {
    doNothing().when(notificationService).sendNotification(any(NotificationRequestDto.class));

    mockMvc.perform(post("/api/notifications/send")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(notificationRequestDto)))
            .andExpect(status().isAccepted());

    verify(notificationService, times(1)).sendNotification(any(NotificationRequestDto.class));
  }

  @Test
  @DisplayName("POST /api/notifications/send - Bad Request")
  void testSendNotificationBadRequest() throws Exception {
    NotificationRequestDto invalidDto = new NotificationRequestDto(
            null, // invalid: null recipient
            "Test Notification",
            "test-template",
            new HashMap<>(),
            NotificationTypeEnum.EMAIL,
            NotificationCategoryEnum.VERIFICATION
    );

    mockMvc.perform(post("/api/notifications/send")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(invalidDto)))
            .andExpect(status().isBadRequest());

    verify(notificationService, never()).sendNotification(any(NotificationRequestDto.class));
  }

  @Test
  @DisplayName("POST /api/notifications/send - Internal Server Error")
  void testSendNotificationInternalServerError() throws Exception {
    doThrow(new EmailSendingException("test@example.com", "Test Subject", "Failed to send email"))
            .when(notificationService).sendNotification(any(NotificationRequestDto.class));

    mockMvc.perform(post("/api/notifications/send")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(notificationRequestDto)))
            .andExpect(status().isServiceUnavailable());

    verify(notificationService, times(1)).sendNotification(any(NotificationRequestDto.class));
  }
}