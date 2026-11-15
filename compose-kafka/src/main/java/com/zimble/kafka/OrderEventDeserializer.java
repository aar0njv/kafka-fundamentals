

package com.zimble.kafka;
import com.zimble.OrderEvent;
import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;

public class OrderEventDeserializer extends ObjectMapperDeserializer<OrderEvent> {
    public OrderEventDeserializer() {
        super(OrderEvent.class);
    }
}