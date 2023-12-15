package com.michalkucera.domain.sharedkernel.event

interface DomainEventSubscriber<T : DomainEvent> {
    suspend fun processDomainEvent(domainEvent: T)
}
