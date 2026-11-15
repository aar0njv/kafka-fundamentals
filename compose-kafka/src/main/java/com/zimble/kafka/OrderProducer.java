

package com.zimble.kafka;

import com.zimble.OrderEvent;
import io.smallrye.reactive.messaging.kafka.KafkaRecord;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Channel;
import io.smallrye.reactive.messaging.MutinyEmitter;
import io.smallrye.mutiny.Uni;

@ApplicationScoped
public class OrderProducer {

    @Inject
    @Channel("orders-out")
    MutinyEmitter<OrderEvent> emitter;

    public Uni<Void> publishOrder(OrderEvent event) {
        // The key (orderId) guarantees sequence, Returning Uni keeps it reactive and non-blocking.
        return emitter.sendMessage(KafkaRecord.of(event.orderId(), event));
    }
}