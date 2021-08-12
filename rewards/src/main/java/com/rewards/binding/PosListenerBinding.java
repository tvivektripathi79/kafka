package com.rewards.binding;

import org.apache.kafka.streams.kstream.KStream;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;

import com.rewards.model.Notification;
import com.rewards.model.PosInvoice;

public interface PosListenerBinding {

	@Input("invoice-input-channel")
	KStream<String, PosInvoice> inpoiceInputStream();

	@Output("notification-output-channel")
	KStream<String, Notification> notificationOutputStream();
}
