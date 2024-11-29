package com.gustavo.dummyaxon.command.bike.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.extensions.amqp.eventhandling.AMQPMessageConverter;
import org.axonframework.extensions.amqp.eventhandling.DefaultAMQPMessageConverter;
import org.axonframework.extensions.amqp.eventhandling.spring.SpringAMQPMessageSource;
import org.axonframework.extensions.amqp.eventhandling.spring.SpringAMQPPublisher;
import org.axonframework.serialization.Serializer;
import org.axonframework.serialization.json.JacksonSerializer;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("amqp")
public class AmqpConfig {

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory
    ) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setDefaultRequeueRejected(false);
        factory.setAcknowledgeMode(AcknowledgeMode.AUTO);
        return factory;
    }

    @Bean
    public AMQPMessageConverter eventMessageConverter(
            final ObjectMapper objectMapper
    ) {
        final Serializer serializer = serializer(objectMapper);
        return new DefaultAMQPMessageConverter.Builder().serializer(serializer).build();
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
    public SpringAMQPPublisher amqpPublisher(
            final EventBus eventBus,
            final ConnectionFactory connectionFactory,
            final Serializer serializer
    ) {
        final var publisher = new SpringAMQPPublisher(eventBus);
        publisher.setConnectionFactory(connectionFactory);
        publisher.setMessageConverter(
                DefaultAMQPMessageConverter.builder()
                        .serializer(serializer)
                        .build()
        );
        publisher.setExchange(bikeExchange()); // Specify your RabbitMQ exchange
        publisher.start();
        return publisher;
    }

    @Bean
    public Exchange bikeExchange() {
        return ExchangeBuilder.topicExchange(Exchanges.BIKE_EXCHANGE).build();
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
        return BindingBuilder.bind(bikeQueue()).to(bikeExchange()).with(RoutingKeys.ALL).noargs();
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
        public static final String BIKE_EXCHANGE = "bike-rent.bike-group-event.exchange";
        public static final String DEAD_EXCHANGE = "bike-rent.dead.exchange";
    }

    public static final class RoutingKeys {
        public static final String ALL = "#";
        public static final String BIKE_DEAD_QUEUE = "Bike";
    }
}
