package com.streamingaggregates.services;

import java.util.Arrays;

import org.apache.kafka.streams.kstream.KStream;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Service;

import com.streamingaggregates.bindings.WordListenerBinding;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
@EnableBinding(WordListenerBinding.class)
public class WordListenerService {

	@StreamListener("words-input-channel")
	public void process(KStream<String, String> input) {

		KStream<String, String> wordStream = input
				.flatMapValues(value -> Arrays.asList(value.toLowerCase().split(" ")));

		wordStream.groupBy((key, value) -> value).count().toStream()
				.peek((k, v) -> System.out.println("Word: " + k + " Count: " + v));
	}

}