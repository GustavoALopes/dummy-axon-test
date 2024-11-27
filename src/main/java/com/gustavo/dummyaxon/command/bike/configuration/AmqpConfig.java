package com.gustavo.dummyaxon.command.bike.configuration;

import com.rabbitmq.client.Channel;
import org.axonframework.extensions.amqp.eventhandling.AMQPMessageConverter;
import org.axonframework.extensions.amqp.eventhandling.DefaultAMQPMessageConverter;
import org.axonframework.extensions.amqp.eventhandling.spring.SpringAMQPMessageSource;
import org.axonframework.serialization.Serializer;
import org.axonframework.serialization.xml.XStreamSerializer;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("amqp")
public class AmqpConfig {

    @Bean
    public AMQPMessageConverter eventMessageConverter() {
        final Serializer serializer = serializer();
        return new DefaultAMQPMessageConverter.Builder().serializer(serializer).build();
    }

    @Bean
    @Primary
    public Serializer serializer() {
        return XStreamSerializer.builder()
                .build();
    }

    @Bean
    public Queue bikeQueue() {
        return new Queue("bike-queue", true, false, false);
    }

    @Bean
    public SpringAMQPMessageSource bikeQueueMessageSource(
            final AMQPMessageConverter messageConverter
    ) {
        return new SpringAMQPMessageSource(messageConverter) {

            @Override
            @RabbitListener(queues = "bike-queue")
            public void onMessage(final Message message, final Channel channel) {
                super.onMessage(message, channel);
            }
        };
    }
}
