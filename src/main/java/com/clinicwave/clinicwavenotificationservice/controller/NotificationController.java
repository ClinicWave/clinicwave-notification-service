package com.clinicwave.clinicwavenotificationservice.controller;

import com.clinicwave.clinicwavenotificationservice.dto.NotificationRequestDto;
import com.clinicwave.clinicwavenotificationservice.service.NotificationService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notifications")
@Slf4j
public class NotificationController {
  private final NotificationService notificationService;

  @Autowired
  public NotificationController(NotificationService notificationService) {
    this.notificationService = notificationService;
  }

  @PostMapping("/send")
  public ResponseEntity<Void> sendNotification(@Valid @RequestBody NotificationRequestDto notificationRequestDto) {
    log.info("Received notification request: {}", notificationRequestDto);
    notificationService.sendNotification(notificationRequestDto);
    return ResponseEntity.accepted().build();
  }
}
