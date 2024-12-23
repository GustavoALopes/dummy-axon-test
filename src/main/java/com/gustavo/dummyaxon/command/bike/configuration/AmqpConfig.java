package com.gustavo.dummyaxon.command.bike.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gustavo.dummyaxon.core.message.resolvers.MetadataRoutingKeyResolver;
import com.rabbitmq.client.Channel;
import org.axonframework.extensions.amqp.eventhandling.AMQPMessageConverter;
import org.axonframework.extensions.amqp.eventhandling.DefaultAMQPMessageConverter;
import org.axonframework.extensions.amqp.eventhandling.RoutingKeyResolver;
import org.axonframework.extensions.amqp.eventhandling.spring.SpringAMQPMessageSource;
import org.axonframework.serialization.Serializer;
import org.axonframework.serialization.json.JacksonSerializer;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@Profile("amqp")
@Configuration(value = "AMQP.Config.Bike")
public class AmqpConfig {

    @Bean
    public AMQPMessageConverter eventMessageConverter(
            final Serializer serializer,
            final RoutingKeyResolver resolver
    ) {
        return new DefaultAMQPMessageConverter.Builder().serializer(serializer).routingKeyResolver(resolver).build();
    }

    @Bean
    @Primary
    public Serializer serializer(
            final ObjectMapper mapper
    ) {
        return JacksonSerializer.builder()
                .objectMapper(mapper)
                .build();
    }

    @Bean
    @Primary
    public RoutingKeyResolver routingKeyResolver() {
        return new MetadataRoutingKeyResolver();
    }

    @Bean
    public Exchange bikeExchange() {
        return ExchangeBuilder.topicExchange(Exchanges.DEFAULT_EXCHANGE).build();
    }


    @Bean
    public Queue bikeQueue() {
        return QueueBuilder
                .durable(Queues.BIKE_QUEUE)
                .quorum()
                .deadLetterExchange(Exchanges.DEAD_EXCHANGE)
                .deadLetterRoutingKey(RoutingKeys.BIKE_DEAD_QUEUE)
                .build();
    }

    @Bean
    public Binding binding() {
        return BindingBuilder.bind(bikeQueue()).to(bikeExchange()).with(RoutingKeys.BIKE).noargs();
    }

    @Bean
    public Queue deadLetterQueue() {
        return QueueBuilder.durable(Queues.BIKE_DEAD_QUEUE).quorum().build();
    }

    @Bean
    public Exchange deadLetterExchange() {
        return ExchangeBuilder.directExchange(Exchanges.DEAD_EXCHANGE).build();
    }

    @Bean
    public Binding deadLetterBinding() {
        return BindingBuilder
                .bind(deadLetterQueue())
                .to((DirectExchange) deadLetterExchange())
                .with(RoutingKeys.BIKE_DEAD_QUEUE);
    }


    @Bean
    public SpringAMQPMessageSource bikeQueueMessageSource(
            final AMQPMessageConverter messageConverter
    ) {
        return new SpringAMQPMessageSource(messageConverter) {

            @Override
            @RabbitListener(queues = Queues.BIKE_QUEUE, ackMode = "AUTO")
            public void onMessage(final Message message, final Channel channel) {
                super.onMessage(message, channel);
            }
        };
    }


    public static final class Queues {
        public static final String BIKE_QUEUE = "bike-rent.bike-group-event.queue";
        public static final String BIKE_DEAD_QUEUE = "bike-rent.bike-group-event.dead.queue";
    }


    public static final class Exchanges {
        public static final String DEFAULT_EXCHANGE = "bike-rent.exchange";
        public static final String DEAD_EXCHANGE = "bike-rent.dead.exchange";
    }

    public static final class RoutingKeys {
        public static final String BIKE = "#.Bike.#";
        public static final String BIKE_DEAD_QUEUE = "Bike";
    }
}
