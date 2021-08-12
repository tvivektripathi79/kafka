package com.rewards.services;

import org.springframework.stereotype.Service;

import com.rewards.model.Notification;
import com.rewards.model.PosInvoice;

@Service
public class RecordBuilder {

	public Notification getNotification(PosInvoice posInvoice) {
		Notification notification =new Notification();
		notification.setInvoiceNumber(posInvoice.getInvoiceNumber());
		notification.setCustomerCardNo(posInvoice.getCustomerCardNo());
		notification.setTotalAmount(posInvoice.getTotalAmount());
		notification.setEarnedLoyaltyPoints(posInvoice.getTotalAmount() *0.02);
		notification.setTotalLoyaltyPoints(notification.getEarnedLoyaltyPoints());
		return notification;
	}
}
