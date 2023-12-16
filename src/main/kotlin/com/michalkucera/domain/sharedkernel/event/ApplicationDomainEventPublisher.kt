package com.michalkucera.domain.sharedkernel.event

import com.michalkucera.domain.sharedkernel.type.DomainEventRegistry
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class ApplicationDomainEventPublisher : DomainEventPublisher {
    private val domainEventBuses = mutableSetOf<DomainEventBus<out DomainEvent>>()

    fun registerDomainEventBus(domainEventBus: DomainEventBus<out DomainEvent>) {
        domainEventBuses += domainEventBus
    }

    override suspend fun publishDomainEvents(domainEventRegistry: DomainEventRegistry) {
        coroutineScope {
            launch {
                domainEventRegistry.popDomainEvents().forEach { publishDomainEvent(it) }
            }
        }
    }

    private suspend fun <E : DomainEvent> publishDomainEvent(domainEvent: E) = domainEventBuses
        .filter { it.supportsDomainEvent(domainEvent) }
        .filterIsInstance<DomainEventBus<E>>()
        .forEach { it.publish(domainEvent) }
}
