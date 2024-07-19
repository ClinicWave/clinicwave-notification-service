package com.clinicwave.clinicwavenotificationservice.service;

import com.clinicwave.clinicwavenotificationservice.dto.NotificationRequestDto;

/**
 * This interface is a service for sending notifications.
 *
 * @author aamir on 7/8/24
 */
public interface NotificationService {
  void sendNotification(NotificationRequestDto notificationRequestDto);
}
