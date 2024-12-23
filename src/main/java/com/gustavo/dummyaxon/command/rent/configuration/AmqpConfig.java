package com.gustavo.dummyaxon.command.rent.configuration;

import com.gustavo.dummyaxon.command.bike.configuration.AmqpSequencedDeadLetterQueue;
import com.rabbitmq.client.Channel;
import org.axonframework.config.ConfigurerModule;
import org.axonframework.extensions.amqp.eventhandling.AMQPMessageConverter;
import org.axonframework.extensions.amqp.eventhandling.spring.SpringAMQPMessageSource;
import org.axonframework.serialization.Serializer;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("amqp")
@Configuration(value = "AMQP.Config.Rent")
public class AmqpConfig {

    @Bean
    public ConfigurerModule configureDeadQueue$rent(
            final RabbitTemplate rabbitTemplate,
            final Serializer serializer
    ) {
        return configurer -> {
            configurer.eventProcessing().registerDeadLetterQueue(
                    "rent-group",
                    config -> new AmqpSequencedDeadLetterQueue(rabbitTemplate, Exchanges.DEAD_EXCHANGE, serializer)
            );
        };
    }

    @Bean
    public SpringAMQPMessageSource rentQueueMessageSource(
            final AMQPMessageConverter messageConverter
    ) {
        return new SpringAMQPMessageSource(messageConverter) {

            @Override
            @RabbitListener(queues = Queues.RENT_QUEUE, ackMode = "AUTO")
            public void onMessage(final Message message, final Channel channel) {
                super.onMessage(message, channel);
            }
        };
    }

    @Configuration
    class QueuesCreations {

        @Bean
        public Exchange rentExchange() {
            return ExchangeBuilder.topicExchange(Exchanges.DEFAULT_EXCHANGE).build();
        }

        @Bean
        public Exchange rentDeadExchange() {
            return ExchangeBuilder.directExchange(Exchanges.DEAD_EXCHANGE).build();
        }

        @Bean
        public Queue rentQueue() {
            return QueueBuilder
                    .durable(Queues.RENT_QUEUE)
                    .quorum()
                    .deadLetterExchange(Exchanges.DEAD_EXCHANGE)
                    .deadLetterRoutingKey(RoutingKeys.RENT_DEAD_QUEUE)
                    .build();
        }

        @Bean
        public Queue rentDeadQueue() {
            return QueueBuilder
                    .durable(Queues.RENT_DEAD_QUEUE)
                    .quorum()
                    .build();
        }

        @Bean
        public Binding binding$rent() {
            return BindingBuilder
                    .bind(rentQueue())
                    .to(rentExchange())
                    .with(RoutingKeys.RENT_ROUTING_KEY)
                    .noargs();
        }

        @Bean
        public Binding bindingDeadExchange$rent() {
            return BindingBuilder
                    .bind(rentDeadQueue())
                    .to(rentDeadExchange())
                    .with(RoutingKeys.RENT_DEAD_QUEUE)
                    .noargs();
        }
    }

    public static final class Queues {
        public static final String RENT_QUEUE = "bike-rent.rent-group-event.queue";
        public static final String RENT_DEAD_QUEUE = "bike-rent.rent-group-event.dead.queue";
    }

    public static final class Exchanges {
        public static final String DEFAULT_EXCHANGE = "bike-rent.exchange";
        public static final String DEAD_EXCHANGE = "bike-rent.dead.exchange";
    }

    public static final class RoutingKeys {
        public static final String RENT_ROUTING_KEY = "#.Rent.#";
        public static final String RENT_DEAD_QUEUE = "Rent";
    }
}
