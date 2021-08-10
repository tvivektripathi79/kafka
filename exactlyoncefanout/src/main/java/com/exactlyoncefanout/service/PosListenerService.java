package com.exactlyoncefanout.service;

import org.apache.kafka.streams.kstream.KStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Service;

import com.exactlyoncefanout.bindings.PosListenerBinding;
import com.exactlyoncefanout.model.HadoopRecord;
import com.exactlyoncefanout.model.Notification;
import com.exactlyoncefanout.model.PosInvoice;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
@EnableBinding(PosListenerBinding.class)
public class PosListenerService {

    @Autowired
    RecordBuilder recordBuilder;

    @StreamListener("pos-input-channel")
    public void process(KStream<String, PosInvoice> input) {

        KStream<String, HadoopRecord> hadoopRecordKStream = input
                .mapValues( v -> recordBuilder.getMaskedInvoice(v))
                .flatMapValues( v -> recordBuilder.getHadoopRecords(v));

        KStream<String, Notification> notificationKStream = input
                .filter((k, v) -> v.getCustomerType().equalsIgnoreCase("PRIME"))
                .mapValues(v -> recordBuilder.getNotification(v));

        hadoopRecordKStream.foreach((k, v) -> log.info(String.format("Hadoop Record:- Key: %s, Value: %s", k, v)));
        notificationKStream.foreach((k, v) -> log.info(String.format("Notification:- Key: %s, Value: %s", k, v)));

        hadoopRecordKStream.to("hadoop-sink-topic");
        notificationKStream.to("loyalty-topic");

    }

}
