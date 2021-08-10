package com.ktable.services;

import org.apache.kafka.streams.kstream.KTable;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Service;

import com.ktable.bindings.StockListenerBinding;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
@EnableBinding(StockListenerBinding.class)
public class StockTickListenerService {
	@StreamListener("stock-input-channel")
    public void process(KTable<String, String> input) {

        input.filter((key, value) -> key.contains("HDFCBANK"))
                .toStream()
                .foreach((k, v) -> System.out.println("Key = " + k + " Value = " + v));
    }
}
