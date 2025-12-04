package com.backend.paymentsprocessing.service.interfaces;

import com.backend.paymentsprocessing.pojo.NotificationRequest;

public interface NotificationService {
	
	public void processNotification(NotificationRequest notificationRequest);

}
