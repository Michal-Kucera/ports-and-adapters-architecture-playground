package com.michalkucera.application.usecase

import com.michalkucera.domain.sharedkernel.event.ApplicationDomainEventPublisher
import com.michalkucera.domain.sharedkernel.event.DomainEventBus
import com.michalkucera.domain.sharedkernel.event.DomainEventSubscriber
import com.michalkucera.domain.whitelabel.entity.WhiteLabel
import com.michalkucera.domain.whitelabel.event.WhiteLabelCreatedEvent
import com.michalkucera.domain.whitelabel.event.WhiteLabelNameChangedEvent
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test

class CreateChannelsTest {
    @Test
    fun createChannels() = runBlocking {
        val domainEventPublisher = ApplicationDomainEventPublisher()

        DomainEventBus<WhiteLabelCreatedEvent>().apply {
            domainEventPublisher.registerDomainEventBus(this)
            registerSubscriber(WhiteLabelCreatedEventDomainEventSubscriber1())
            registerSubscriber(WhiteLabelCreatedEventDomainEventSubscriber2())
            registerSubscriber(WhiteLabelCreatedEventDomainEventSubscriber3())
        }

        DomainEventBus<WhiteLabelNameChangedEvent>().apply {
            domainEventPublisher.registerDomainEventBus(this)
            registerSubscriber(WhiteLabelNameChangedEventDomainEventSubscriberA())
            registerSubscriber(WhiteLabelNameChangedEventDomainEventSubscriberB())
            registerSubscriber(WhiteLabelNameChangedEventDomainEventSubscriberC())
        }

        val whiteLabel = WhiteLabel.create("Hello World", "This is my great white label")
        whiteLabel.changeName("My white label", "This is my great white label")
        domainEventPublisher.publishDomainEvents(whiteLabel)
        println("Other operations are async")
        delay(10000)
        println("End of test")
    }
}

class WhiteLabelCreatedEventDomainEventSubscriber1 : DomainEventSubscriber<WhiteLabelCreatedEvent> {
    override suspend fun processDomainEvent(domainEvent: WhiteLabelCreatedEvent) {
        println("1")
        delay(5000)
    }
}

class WhiteLabelCreatedEventDomainEventSubscriber2 : DomainEventSubscriber<WhiteLabelCreatedEvent> {
    override suspend fun processDomainEvent(domainEvent: WhiteLabelCreatedEvent) {
        println("2")
        delay(5000)
    }
}

class WhiteLabelCreatedEventDomainEventSubscriber3 : DomainEventSubscriber<WhiteLabelCreatedEvent> {
    override suspend fun processDomainEvent(domainEvent: WhiteLabelCreatedEvent) {
        println("3")
        error("Sorry, I cannot run properly")
    }
}

class WhiteLabelNameChangedEventDomainEventSubscriberC : DomainEventSubscriber<WhiteLabelNameChangedEvent> {
    override suspend fun processDomainEvent(domainEvent: WhiteLabelNameChangedEvent) {
        println("C")
        delay(5000)
    }
}

class WhiteLabelNameChangedEventDomainEventSubscriberB : DomainEventSubscriber<WhiteLabelNameChangedEvent> {
    override suspend fun processDomainEvent(domainEvent: WhiteLabelNameChangedEvent) {
        println("B")
        delay(5000)
    }
}

class WhiteLabelNameChangedEventDomainEventSubscriberA : DomainEventSubscriber<WhiteLabelNameChangedEvent> {
    override suspend fun processDomainEvent(domainEvent: WhiteLabelNameChangedEvent) {
        println("A")
        delay(5000)
    }
}
