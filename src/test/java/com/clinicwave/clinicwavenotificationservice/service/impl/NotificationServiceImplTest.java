package com.clinicwave.clinicwavenotificationservice.service.impl;

import com.clinicwave.clinicwavenotificationservice.dto.NotificationRequestDto;
import com.clinicwave.clinicwavenotificationservice.enums.NotificationCategoryEnum;
import com.clinicwave.clinicwavenotificationservice.enums.NotificationTypeEnum;
import com.clinicwave.clinicwavenotificationservice.exception.InvalidNotificationTypeException;
import com.clinicwave.clinicwavenotificationservice.strategy.EmailNotificationStrategy;
import com.clinicwave.clinicwavenotificationservice.strategy.NotificationStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * This class contains unit tests for the NotificationServiceImpl class.
 *
 * @author aamir on 7/16/24
 */
@ExtendWith(MockitoExtension.class)
class NotificationServiceImplTest {
  private NotificationServiceImpl notificationService;

  @Mock
  private NotificationStrategy smsNotificationStrategy;

  @Mock
  private EmailNotificationStrategy emailNotificationStrategy;

  /**
   * Sets up the test environment before each test.
   */
  @BeforeEach
  void setUp() {
    when(emailNotificationStrategy.getType()).thenReturn(NotificationTypeEnum.EMAIL);
    when(smsNotificationStrategy.getType()).thenReturn(NotificationTypeEnum.SMS);

    List<NotificationStrategy> strategies = Arrays.asList(emailNotificationStrategy, smsNotificationStrategy);
    notificationService = new NotificationServiceImpl(strategies);
  }

  @ParameterizedTest
  @EnumSource(value = NotificationTypeEnum.class, names = {"EMAIL", "SMS"})
  @DisplayName("sendNotification should use correct strategy")
  void sendNotification_ShouldUseCorrectStrategy(NotificationTypeEnum type) {
    NotificationRequestDto requestDto = createNotificationRequestDto(type);

    notificationService.sendNotification(requestDto);

    if (type == NotificationTypeEnum.EMAIL) {
      verify(emailNotificationStrategy, times(1)).send(requestDto);
      verify(smsNotificationStrategy, never()).send(any());
    } else {
      verify(smsNotificationStrategy, times(1)).send(requestDto);
      verify(emailNotificationStrategy, never()).send(any());
    }
  }

  @Test
  @DisplayName("sendNotification should throw exception for unsupported type")
  void sendNotification_WithUnsupportedType_ShouldThrowException() {
    NotificationRequestDto requestDto = createNotificationRequestDto(NotificationTypeEnum.WEB);

    InvalidNotificationTypeException exception = assertThrows(InvalidNotificationTypeException.class,
            () -> notificationService.sendNotification(requestDto));

    String expectedMessage = "NotificationStrategy not supported with type : 'WEB'";
    assertEquals(expectedMessage, exception.getMessage());
  }

  @Test
  @DisplayName("sendNotification should throw exception for no matching type")
  void sendNotification_WithNoMatchingType_ShouldThrowException() {
    notificationService = new NotificationServiceImpl(Collections.singletonList(emailNotificationStrategy));
    NotificationRequestDto requestDto = createNotificationRequestDto(NotificationTypeEnum.SMS);

    InvalidNotificationTypeException exception = assertThrows(InvalidNotificationTypeException.class,
            () -> notificationService.sendNotification(requestDto));

    String expectedMessage = "NotificationStrategy not supported with type : 'SMS'";
    assertEquals(expectedMessage, exception.getMessage());
  }

  @Test
  @DisplayName("constructNotificationService should allow construction with empty strategy list")
  void constructNotificationService_WithEmptyStrategyList_ShouldAllowConstruction() {
    NotificationServiceImpl service = new NotificationServiceImpl(List.of());
    assertNotNull(service);
  }

  @Test
  @DisplayName("sendNotification should throw exception for empty strategy list")
  void sendNotification_WithEmptyStrategyList_ShouldThrowException() {
    NotificationServiceImpl service = new NotificationServiceImpl(List.of());
    NotificationRequestDto requestDto = createNotificationRequestDto(NotificationTypeEnum.EMAIL);

    assertThrows(InvalidNotificationTypeException.class, () -> service.sendNotification(requestDto));
  }

  @Test
  @DisplayName("constructNotificationService should use last strategy for duplicate type")
  void constructNotificationService_WithDuplicateStrategyTypes_ShouldUseLastStrategyForType() {
    EmailNotificationStrategy duplicateEmailStrategy = mock(EmailNotificationStrategy.class);
    when(duplicateEmailStrategy.getType()).thenReturn(NotificationTypeEnum.EMAIL);

    NotificationServiceImpl service = new NotificationServiceImpl(Arrays.asList(emailNotificationStrategy, duplicateEmailStrategy));
    NotificationRequestDto requestDto = createNotificationRequestDto(NotificationTypeEnum.EMAIL);

    service.sendNotification(requestDto);

    verify(duplicateEmailStrategy, times(1)).send(requestDto);
    verify(emailNotificationStrategy, never()).send(any());
  }

  /**
   * Creates a NotificationRequestDto object with the given type.
   *
   * @param type the type of the notification
   * @return the created NotificationRequestDto object
   */
  private NotificationRequestDto createNotificationRequestDto(NotificationTypeEnum type) {
    return new NotificationRequestDto(
            "recipient@example.com",
            "Test Subject",
            "test-template",
            new HashMap<>(),
            type,
            NotificationCategoryEnum.VERIFICATION
    );
  }
}