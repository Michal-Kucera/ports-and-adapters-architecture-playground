package com.michalkucera.domain.sharedkernel.event

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch

class ApplicationDomainEventPublisher(
    private val domainEventBuses: Set<DomainEventBus<out DomainEvent>>
) : DomainEventPublisher {
    override suspend fun publishDomainEvents(domainEvents: Iterable<DomainEvent>) {
        GlobalScope.launch(Dispatchers.IO) {
            domainEvents.forEach { publishDomainEvent(it) }
        }
    }

    override suspend fun publishDomainEventsAsync(domainEvents: Iterable<DomainEvent>) = coroutineScope {
        domainEvents
            .map { async { publishDomainEvent(it) } }
            .joinAll()
    }

    override suspend fun <E : DomainEvent> publishDomainEvent(domainEvent: E) = domainEventBuses
        .filter { it.supportsDomainEvent(domainEvent) }
        .map { it as DomainEventBus<E> }
        .forEach { it.publish(domainEvent) }
}
