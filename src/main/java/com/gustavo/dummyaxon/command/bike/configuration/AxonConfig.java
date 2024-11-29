package com.gustavo.dummyaxon.command.bike.configuration;

import org.axonframework.config.ConfigurerModule;
import org.axonframework.eventhandling.EventMessage;
import org.axonframework.messaging.deadletter.SequencedDeadLetterQueue;
import org.axonframework.serialization.Serializer;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AxonConfig {

    @Bean
    public SequencedDeadLetterQueue<EventMessage<?>> amqpDeadLetterQueue(RabbitTemplate rabbitTemplate, Serializer serializer) {
        return new AmqpSequencedDeadLetterQueue(rabbitTemplate, AmqpConfig.Exchanges.DEAD_EXCHANGE, serializer, 100);
    }

    @Bean
    public ConfigurerModule deadLetterQueueConfigurerModule(
            final SequencedDeadLetterQueue<EventMessage<?>> amqpSequencedDeadLetterQueue
    ) {
        // Replace "my-processing-group" for the processing group you want to configure the DLQ on.
        return configurer -> configurer.eventProcessing().registerDeadLetterQueue(
                "bike-group",
                config -> amqpSequencedDeadLetterQueue
        );
    }
}
