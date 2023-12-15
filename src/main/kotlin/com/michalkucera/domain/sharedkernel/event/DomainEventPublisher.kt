package com.michalkucera.domain.sharedkernel.event

interface DomainEventPublisher {
    suspend fun publishDomainEvents(domainEvents: Iterable<DomainEvent>)

    suspend fun publishDomainEventsAsync(domainEvents: Iterable<DomainEvent>)

    suspend fun <E : DomainEvent> publishDomainEvent(domainEvent: E)
}
