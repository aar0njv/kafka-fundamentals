

package com.zimble.kafka;

import com.zimble.OrderEvent;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.jboss.logging.Logger;

@ApplicationScoped
public class OrderConsumer {

    private static final Logger LOG = Logger.getLogger(OrderConsumer.class);

    @Incoming("orders-in")
    public void consume(OrderEvent event) {
        // In a real app, you would check the eventId against a DB to ensure idempotency
        LOG.infof("Consumed Event ID: %s | Order ID: %s | Amount: %s", 
                  event.eventId(), event.orderId(), event.amount());
    }
}