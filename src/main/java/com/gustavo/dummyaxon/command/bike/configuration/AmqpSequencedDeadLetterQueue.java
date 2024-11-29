package com.gustavo.dummyaxon.command.bike.configuration;

import org.axonframework.eventhandling.EventMessage;
import org.axonframework.eventhandling.GenericDomainEventMessage;
import org.axonframework.messaging.deadletter.*;
import org.axonframework.serialization.Serializer;
import org.jetbrains.annotations.NotNull;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

public class AmqpSequencedDeadLetterQueue implements SequencedDeadLetterQueue<EventMessage<?>> {

    private final RabbitTemplate rabbitTemplate;
    private final String exchange;
    private final Serializer serializer;
    private final Map<Object, List<DeadLetter<? extends EventMessage<?>>>> inMemorySequences = new ConcurrentHashMap<>();
    private final int maxQueueSize;

    public AmqpSequencedDeadLetterQueue(
            final RabbitTemplate rabbitTemplate,
            final String exchange,
            final Serializer serializer,
            final int maxQueueSize
    ) {
        this.rabbitTemplate = rabbitTemplate;
        this.exchange = exchange;
        this.serializer = serializer;
        this.maxQueueSize = maxQueueSize;
    }

    @Override
    public void enqueue(@NotNull final Object sequenceIdentifier, @NotNull final DeadLetter<? extends EventMessage<?>> letter) throws DeadLetterQueueOverflowException {
        if (isFull(sequenceIdentifier)) {
            throw new DeadLetterQueueOverflowException("The sequence is full: " + sequenceIdentifier);
        }

        // Serialize DeadLetter and send to RabbitMQ
        final var serializedLetter = serializer.serialize(letter, byte[].class).getData();
        rabbitTemplate.convertAndSend(exchange, ((GenericDomainEventMessage<?>)letter.message()).getType(), serializedLetter);

        // Store in memory for sequencing
        inMemorySequences.computeIfAbsent(sequenceIdentifier, k -> new ArrayList<>()).add((DeadLetter<EventMessage<?>>) letter);
    }



    @Override
    public void evict(final DeadLetter<? extends EventMessage<?>> letter) {
        inMemorySequences.values().forEach(sequence -> sequence.remove(letter));
    }

    @Override
    public void requeue(@NotNull final DeadLetter<? extends EventMessage<?>> letter, @NotNull final UnaryOperator<DeadLetter<? extends EventMessage<?>>> letterUpdater) throws NoSuchDeadLetterException {
        // Find the sequence containing the letter
        final var sequenceEntry = inMemorySequences.entrySet().stream()
                .filter(entry -> entry.getValue().contains(letter))
                .findFirst();

        if (sequenceEntry.isEmpty()) {
            throw new NoSuchDeadLetterException("DeadLetter does not exist in any sequence");
        }

        Object sequenceIdentifier = sequenceEntry.get().getKey();
        final var sequence = sequenceEntry.get().getValue();

        // Update the letter
        final var updatedLetter = letterUpdater.apply(letter);

        // Remove the original letter and add the updated one
        sequence.remove(letter);
        enqueue(sequenceIdentifier, updatedLetter);
    }

    @Override
    public boolean contains(@Nonnull Object sequenceIdentifier) {
        return inMemorySequences.containsKey(sequenceIdentifier);
    }

    public Iterable<DeadLetter<? extends EventMessage<?>>> deadLetterSequence(@Nonnull Object sequenceIdentifier) {
        return inMemorySequences.getOrDefault(sequenceIdentifier, Collections.emptyList());
    }


    @Override
    public Iterable<Iterable<DeadLetter<? extends EventMessage<?>>>> deadLetters() {
        return (Iterable)inMemorySequences.values();
    }

    @Override
    public boolean isFull(@Nonnull Object sequenceIdentifier) {
        return sequenceSize(sequenceIdentifier) >= maxQueueSize;
    }

    @Override
    public long size() {
        return inMemorySequences.values().stream().mapToLong(List::size).sum();
    }

    @Override
    public long sequenceSize(@Nonnull Object sequenceIdentifier) {
        return inMemorySequences.getOrDefault(sequenceIdentifier, Collections.emptyList()).size();
    }

    @Override
    public long amountOfSequences() {
        return inMemorySequences.size();
    }



    @Override
    public boolean process(
            @NotNull final Predicate<DeadLetter<? extends EventMessage<?>>> sequenceFilter,
            @NotNull final Function<DeadLetter<? extends EventMessage<?>>,
            EnqueueDecision<EventMessage<?>>> processingTask
    ) {
        boolean processed = false;

        for (final var entry : inMemorySequences.entrySet()) {
            final var sequence = entry.getValue();
            for (final var letter : sequence) {
                if (sequenceFilter.test(letter)) {
                    final var decision = processingTask.apply(letter);
                    if (decision.shouldEnqueue()) {
                        evict(letter);
                        processed = true;
                    }
                }
            }
        }
        return processed;
    }

    @Override
    public void clear() {
        inMemorySequences.clear();
    }
}
