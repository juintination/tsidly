package dev.deokjae.urlshortener.common.outbox.relay

import dev.deokjae.urlshortener.common.outbox.coordinator.MessageRelayCoordinator
import dev.deokjae.urlshortener.common.outbox.entity.Outbox
import dev.deokjae.urlshortener.common.outbox.event.OutboxEvent
import dev.deokjae.urlshortener.common.outbox.repository.OutboxRepository
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.data.domain.Pageable
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.scheduling.annotation.Async
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit

@Component
class MessageRelay(
    private val outboxRepository: OutboxRepository,
    private val messageRelayCoordinator: MessageRelayCoordinator,
    private val messageRelayKafkaTemplate: KafkaTemplate<String, String>,
) {
    private val log = KotlinLogging.logger {}

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    fun createOutbox(
        event: OutboxEvent,
    ) {
        log.info { "[MessageRelay.createOutbox] outboxEvent=$event" }
        outboxRepository.save(
            Outbox.create(
                eventType = event.eventType,
                payload = event.payload,
                shardKey = event.shardKey,
            )
        )
    }

    @Async("messageRelayPublishEventExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun publishEvent(
        event: OutboxEvent,
    ) {
        publishEvent(
            outbox = Outbox.create(
                eventType = event.eventType,
                payload = event.payload,
                shardKey = event.shardKey
            ),
        )
    }

    private fun publishEvent(
        outbox: Outbox,
    ) {
        try {
            messageRelayKafkaTemplate.send(
                outbox.eventType.topic,
                outbox.shardKey.toString(),
                outbox.payload
            ).get(1, TimeUnit.SECONDS)
            outboxRepository.delete(outbox)
        } catch (
            e: Exception,
        ) {
            log.error(e) { "[MessageRelay.publishEvent] outbox=$outbox" }
        }
    }

    @Scheduled(
        fixedDelay = 10000,
        initialDelay = 5000,
        scheduler = "messageRelayPublishPendingEventExecutor"
    )
    fun publishPendingEvent() {
        val assignedShard = messageRelayCoordinator.assignShards()
        log.info { "[MessageRelay.publishPendingEvent] assignedShard size=${assignedShard.shards.size}" }

        assignedShard.shards.forEach { shard ->
            val outboxes = outboxRepository.findAllByShardKeyAndCreatedAtLessThanEqualOrderByCreatedAtAsc(
                shardKey = shard,
                from = LocalDateTime.now().minusSeconds(10),
                pageable = Pageable.ofSize(100),
            )
            outboxes.forEach { publishEvent(it) }
        }
    }
}
