package com.gustavo.dummyaxon.core.message.resolvers;

import org.axonframework.eventhandling.EventMessage;
import org.axonframework.eventhandling.GenericDomainEventMessage;
import org.axonframework.extensions.amqp.eventhandling.RoutingKeyResolver;

public class MetadataRoutingKeyResolver implements RoutingKeyResolver {

    @Override
    public String resolveRoutingKey(final EventMessage<?> eventMessage) {
        final var type = ((GenericDomainEventMessage) eventMessage).getType();
        final var eventName = eventMessage.getPayloadType().getSimpleName();
        return String.join(".", "bike-rent", type, eventName);
    }
}
