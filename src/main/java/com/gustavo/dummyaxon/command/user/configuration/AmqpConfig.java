package com.gustavo.dummyaxon.command.user.configuration;

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
@Configuration(value = "AMQP.Config.User")
public class AmqpConfig {

    @Bean
    public ConfigurerModule configureDeadQueue(
            final RabbitTemplate rabbitTemplate,
            final Serializer serializer
    ) {
        return configurer -> {
            configurer.eventProcessing().registerDeadLetterQueue(
                    "user-group",
                    config -> new AmqpSequencedDeadLetterQueue(rabbitTemplate, Exchanges.DEAD_EXCHANGE, serializer)
            );
        };
    }

    @Bean
    public SpringAMQPMessageSource userQueueMessageSource(
            final AMQPMessageConverter messageConverter
    ) {
        return new SpringAMQPMessageSource(messageConverter) {

            @Override
            @RabbitListener(queues = Queues.USER_QUEUE, ackMode = "AUTO")
            public void onMessage(final Message message, final Channel channel) {
                super.onMessage(message, channel);
            }
        };
    }

    @Configuration
    class QueuesCreations {

        @Bean
        public Exchange userExchange() {
            return ExchangeBuilder.topicExchange(Exchanges.DEFAULT_EXCHANGE).build();
        }

        @Bean
        public Exchange userDeadExchange() {
            return ExchangeBuilder.directExchange(Exchanges.DEAD_EXCHANGE).build();
        }

        @Bean
        public Queue userQueue() {
            return QueueBuilder
                    .durable(Queues.USER_QUEUE)
                    .quorum()
                    .deadLetterExchange(Exchanges.DEAD_EXCHANGE)
                    .deadLetterRoutingKey(RoutingKeys.USER_DEAD_QUEUE)
                    .build();
        }

        @Bean
        public Queue userDeadQueue() {
            return QueueBuilder
                    .durable(Queues.USER_DEAD_QUEUE)
                    .quorum()
                    .build();
        }

        @Bean
        public Binding binding$user() {
            return BindingBuilder
                    .bind(userQueue())
                    .to(userExchange())
                    .with(RoutingKeys.USER_ROUTING_KEY)
                    .noargs();
        }

        @Bean
        public Binding bindingDeadExchange$user() {
            return BindingBuilder
                    .bind(userDeadQueue())
                    .to(userDeadExchange())
                    .with(RoutingKeys.USER_DEAD_QUEUE)
                    .noargs();
        }
    }

    public static final class Queues {
        public static final String USER_QUEUE = "bike-rent.user-group-event.queue";
        public static final String USER_DEAD_QUEUE = "bike-rent.user-group-event.dead.queue";
    }

    public static final class Exchanges {
        public static final String DEFAULT_EXCHANGE = "bike-rent.exchange";
        public static final String DEAD_EXCHANGE = "bike-rent.dead.exchange";
    }

    public static final class RoutingKeys {
        public static final String USER_ROUTING_KEY = "#.User.#";
        public static final String USER_DEAD_QUEUE = "User";
    }
}
