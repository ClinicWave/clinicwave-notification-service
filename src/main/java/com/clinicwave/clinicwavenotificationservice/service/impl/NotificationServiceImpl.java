package com.clinicwave.clinicwavenotificationservice.service.impl;

import com.clinicwave.clinicwavenotificationservice.dto.NotificationRequestDto;
import com.clinicwave.clinicwavenotificationservice.enums.NotificationTypeEnum;
import com.clinicwave.clinicwavenotificationservice.exception.InvalidNotificationTypeException;
import com.clinicwave.clinicwavenotificationservice.service.NotificationService;
import com.clinicwave.clinicwavenotificationservice.strategy.NotificationStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * This class is a service class for the Notification entity.
 * It provides methods to send email notifications.
 *
 * @author aamir on 7/8/24
 */
@Service
@Slf4j
public class NotificationServiceImpl implements NotificationService {
  private final Map<NotificationTypeEnum, NotificationStrategy> notificationStrategyMap;

  private static final String TOPIC_NAME = "notification-topic";
  private static final String GROUP_ID = "notification-group";

  /**
   * Constructor for dependency injection.
   *
   * @param notificationStrategyList The list of notification strategies to be used for sending notifications.
   */
  @Autowired
  public NotificationServiceImpl(List<NotificationStrategy> notificationStrategyList) {
    // Initialize the notification strategy map with an EnumMap
    notificationStrategyMap = new EnumMap<>(NotificationTypeEnum.class);

    // Add each notification strategy to the map using its type as the key
    notificationStrategyList.forEach(strategy -> notificationStrategyMap.put(strategy.getType(), strategy));
    log.info("Notification strategies initialized: {}", notificationStrategyMap.keySet());
  }

  /**
   * Kafka listener method to handle notification requests.
   *
   * @param notificationRequestDto The notification request to be handled.
   */
  @KafkaListener(topics = TOPIC_NAME, groupId = GROUP_ID)
  public void handleNotification(NotificationRequestDto notificationRequestDto) {
    log.info("Received notification request: {}", notificationRequestDto);
    sendNotification(notificationRequestDto);
  }

  /**
   * Sends a notification using the appropriate strategy based on the notification type.
   *
   * @param notificationRequestDto The notification request to be sent.
   * @throws InvalidNotificationTypeException if the notification type is not supported.
   */
  @Override
  public void sendNotification(NotificationRequestDto notificationRequestDto) {
    Optional.ofNullable(notificationStrategyMap.get(notificationRequestDto.type()))
            .orElseThrow(() -> new InvalidNotificationTypeException("NotificationStrategy", "type", notificationRequestDto.type()))
            .send(notificationRequestDto);
  }
}
