package com.rewards.services;

import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Service;

import com.rewards.binding.PosListenerBinding;
import com.rewards.model.Notification;
import com.rewards.model.PosInvoice;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
@EnableBinding(PosListenerBinding.class)
public class LoyaltyService {

	@Autowired
	RecordBuilder recordBuilder;
	
	@StreamListener("invoice-input-channel")
	@SendTo("notification-output-channel")
	public KStream<String, Notification> process(KStream<String, PosInvoice> input){
		KStream<String, Notification> notificationKStream = input
                .filter((k, v) -> v.getCustomerType().equalsIgnoreCase("PRIME"))
                .map((k, v) -> new KeyValue<>(v.getCustomerCardNo(), recordBuilder.getNotification(v)))
                .groupByKey()
                .reduce((aggValue, newValue) -> {
                    newValue.setTotalLoyaltyPoints(newValue.getEarnedLoyaltyPoints() + aggValue.getTotalLoyaltyPoints());
                    return newValue;
                }).toStream();

        notificationKStream.foreach((k, v) -> log.info(String.format("Notification:- Key: %s, Value: %s", k, v)));
        return notificationKStream;
	}
}
