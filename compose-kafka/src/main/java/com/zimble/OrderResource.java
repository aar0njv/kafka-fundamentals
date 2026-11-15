

// REST endpoint for creating orders and sending order events to Kafka

package com.zimble;

import com.zimble.kafka.OrderProducer;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.jboss.resteasy.reactive.ResponseStatus;

@Path("/orders")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class OrderResource {

    private final OrderProducer producer;

    public OrderResource(OrderProducer producer) {
        this.producer = producer;
    }

    @POST
    @ResponseStatus(202)
    public Uni<Void> createOrder(OrderEvent event) {
        return producer.publishOrder(event);
    }
}