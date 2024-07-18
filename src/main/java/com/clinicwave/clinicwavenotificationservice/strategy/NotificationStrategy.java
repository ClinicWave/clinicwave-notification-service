package com.clinicwave.clinicwavenotificationservice.strategy;

import com.clinicwave.clinicwavenotificationservice.dto.NotificationRequestDto;
import com.clinicwave.clinicwavenotificationservice.enums.NotificationTypeEnum;

/**
 * This interface defines the strategy for sending notifications.
 * Different implementations of this interface can be used to send notifications using different methods.
 * For example, an implementation can send notifications via email, SMS, or push notifications.
 *
 * @author aamir on 7/10/24
 */
public interface NotificationStrategy {
  NotificationTypeEnum getType();

  void send(NotificationRequestDto notificationRequestDto);
}
