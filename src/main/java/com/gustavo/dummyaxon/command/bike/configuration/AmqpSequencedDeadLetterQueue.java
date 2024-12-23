package com.gustavo.dummyaxon.command.bike.configuration;

import org.axonframework.eventhandling.EventMessage;
import org.axonframework.eventhandling.GenericDomainEventMessage;
import org.axonframework.messaging.deadletter.*;
import org.axonframework.serialization.Serializer;
import org.jetbrains.annotations.NotNull;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import javax.annotation.Nonnull;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

public class AmqpSequencedDeadLetterQueue implements SequencedDeadLetterQueue<EventMessage<?>> {

    private final RabbitTemplate rabbitTemplate;

    private final String exchange;

    private final Serializer serializer;


    public AmqpSequencedDeadLetterQueue(
            final RabbitTemplate rabbitTemplate,
            final String exchange,
            final Serializer serializer
    ) {
        this.rabbitTemplate = rabbitTemplate;
        this.exchange = exchange;
        this.serializer = serializer;
    }

    @Override
    public void enqueue(
            @NotNull final Object sequenceIdentifier,
            @NotNull final DeadLetter<? extends EventMessage<?>> letter
    ) throws DeadLetterQueueOverflowException {
        if (isFull(sequenceIdentifier)) {
            throw new DeadLetterQueueOverflowException("The sequence is full: " + sequenceIdentifier);
        }

        // Serialize DeadLetter and send to RabbitMQ
        rabbitTemplate.convertAndSend(exchange, ((GenericDomainEventMessage<?>)letter.message()).getType(), serializer.serialize(letter.message(), byte[].class).getData());
    }



    @Override
    public void evict(final DeadLetter<? extends EventMessage<?>> letter) {
    }

    @Override
    public void requeue(
            @NotNull final DeadLetter<? extends EventMessage<?>> letter,
            @NotNull final UnaryOperator<DeadLetter<? extends EventMessage<?>>> letterUpdater
    ) throws NoSuchDeadLetterException {
        System.out.println("Call requeue");
    }

    @Override
    public boolean contains(@Nonnull Object sequenceIdentifier) {
        return false;
    }

    public Iterable<DeadLetter<? extends EventMessage<?>>> deadLetterSequence(@Nonnull Object sequenceIdentifier) {
        System.out.println("call Dead Letter Sequence");
        return null;
//        return inMemorySequences.getOrDefault(sequenceIdentifier, Collections.emptyList());
    }


    @Override
    public Iterable<Iterable<DeadLetter<? extends EventMessage<?>>>> deadLetters() {
        System.out.println("call Dead Letters");
        return null;
//        return (Iterable)inMemorySequences.values();
    }

    @Override
    public boolean isFull(@Nonnull Object sequenceIdentifier) {
        return false;
    }

    @Override
    public long size() {
        System.out.println("call size");
        return 0;
    }

    @Override
    public long sequenceSize(@Nonnull Object sequenceIdentifier) {
        System.out.println("call sequence size");
        return 0;
    }

    @Override
    public long amountOfSequences() {
        System.out.println("Call amout of sequences");
        return 0;
    }



    @Override
    public boolean process(
            @NotNull final Predicate<DeadLetter<? extends EventMessage<?>>> sequenceFilter,
            @NotNull final Function<DeadLetter<? extends EventMessage<?>>,
            EnqueueDecision<EventMessage<?>>> processingTask
    ) {
        System.out.println("Call process");
        return false;
    }

    @Override
    public void clear() {
        System.out.println("call clear");
    }
}
