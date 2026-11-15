

// REST endpoint for creating orders and sending order events to Kafka

package com.zimble;

import com.zimble.kafka.OrderProducer;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import io.smallrye.mutiny.Uni;
import org.jboss.resteasy.reactive.ResponseStatus;

@Path("/orders")
public class OrderResource {

    private final OrderProducer producer;

    // Constructor injection
    public OrderResource(OrderProducer producer) {
        this.producer = producer;
    }

    @POST
    @ResponseStatus(202)
    public Uni<Void> createOrder(OrderEvent event) {
        return producer.publishOrder(event);
    }
}