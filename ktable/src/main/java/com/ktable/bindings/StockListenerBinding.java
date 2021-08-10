/**
 * 
 */
package com.ktable.bindings;

import org.apache.kafka.streams.kstream.KTable;
import org.springframework.cloud.stream.annotation.Input;

/**
 * @author vivek
 *
 */
public interface StockListenerBinding {

	@Input("stock-input-channel")
	KTable<String, String> stockInputStream();
}
