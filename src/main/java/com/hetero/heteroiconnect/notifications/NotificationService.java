package com.hetero.heteroiconnect.notifications;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NotificationService {
	private NotificationRepository notificationRepository;

	public NotificationService(NotificationRepository notificationRepository) {
		this.notificationRepository = notificationRepository;
	}

	@Transactional(rollbackFor = Throwable.class)
	public List<Map<String, Object>> appNotifications(int empId) {
		return notificationRepository.appNotifications(empId);
	}
}
