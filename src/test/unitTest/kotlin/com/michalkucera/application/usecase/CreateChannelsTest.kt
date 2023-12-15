package com.michalkucera.application.usecase

import com.michalkucera.domain.sharedkernel.event.ApplicationDomainEventPublisher
import com.michalkucera.domain.sharedkernel.event.DomainEventBus
import com.michalkucera.domain.sharedkernel.event.DomainEventSubscriber
import com.michalkucera.domain.whitelabel.event.WhiteLabelCreatedEvent
import com.michalkucera.domain.whitelabel.event.WhiteLabelNameChangedEvent
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import java.util.UUID

class CreateChannelsTest {
    @Test
    fun createChannels() = runBlocking {
        val whiteLabelCreatedDomainEventBus = DomainEventBus<WhiteLabelCreatedEvent>()
        val whiteLabelNameChangedDomainEventBus = DomainEventBus<WhiteLabelNameChangedEvent>()

        val domainEventPublisher = ApplicationDomainEventPublisher(
            setOf(whiteLabelCreatedDomainEventBus, whiteLabelNameChangedDomainEventBus)
        )

        whiteLabelCreatedDomainEventBus.subscribe(object : DomainEventSubscriber<WhiteLabelCreatedEvent> {
            override suspend fun processDomainEvent(domainEvent: WhiteLabelCreatedEvent) {
                println("1")
                delay(5000)
            }
        })

        whiteLabelCreatedDomainEventBus.subscribe(object : DomainEventSubscriber<WhiteLabelCreatedEvent> {
            override suspend fun processDomainEvent(domainEvent: WhiteLabelCreatedEvent) {
                println("2")
                delay(5000)
            }
        })

        whiteLabelCreatedDomainEventBus.subscribe(object : DomainEventSubscriber<WhiteLabelCreatedEvent> {
            override suspend fun processDomainEvent(domainEvent: WhiteLabelCreatedEvent) {
                println("3")
                error("Sorry, I cannot run properly")
            }
        })

        whiteLabelNameChangedDomainEventBus.subscribe(object : DomainEventSubscriber<WhiteLabelNameChangedEvent> {
            override suspend fun processDomainEvent(domainEvent: WhiteLabelNameChangedEvent) {
                println("A")
                delay(5000)
            }
        })

        whiteLabelNameChangedDomainEventBus.subscribe(object : DomainEventSubscriber<WhiteLabelNameChangedEvent> {
            override suspend fun processDomainEvent(domainEvent: WhiteLabelNameChangedEvent) {
                println("B")
                delay(5000)
            }
        })

        whiteLabelNameChangedDomainEventBus.subscribe(object : DomainEventSubscriber<WhiteLabelNameChangedEvent> {
            override suspend fun processDomainEvent(domainEvent: WhiteLabelNameChangedEvent) {
                println("C")
                delay(5000)
            }
        })

        val domainEvents = listOf(
            WhiteLabelCreatedEvent(UUID.randomUUID()),
            WhiteLabelCreatedEvent(UUID.randomUUID()),
            WhiteLabelNameChangedEvent(UUID.randomUUID()),
            WhiteLabelCreatedEvent(UUID.randomUUID()),
            WhiteLabelNameChangedEvent(UUID.randomUUID()),
            WhiteLabelCreatedEvent(UUID.randomUUID()),
            WhiteLabelNameChangedEvent(UUID.randomUUID()),
            WhiteLabelCreatedEvent(UUID.randomUUID())
        )

        domainEventPublisher.publishDomainEvents(domainEvents)
        println("Other operations are async")
        delay(10000)
        println("End of test")
    }
}
