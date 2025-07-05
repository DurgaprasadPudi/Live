package com.hetero.heteroiconnect.notifications;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/HRMS")
public class NotificationController {
	private NotificationService notificationService;

	public NotificationController(NotificationService notificationService) {
		this.notificationService = notificationService;
	}

	@PostMapping(value = "/notifications", consumes = "multipart/form-data", produces = "application/json")
	public ResponseEntity<List<Map<String, Object>>> appNotifications(@RequestParam int empId) {
		return ResponseEntity.ok(notificationService.appNotifications(empId));
	}
}
