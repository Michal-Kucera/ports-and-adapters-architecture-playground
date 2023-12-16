package com.michalkucera.domain.sharedkernel.event

import com.michalkucera.domain.sharedkernel.type.DomainEventRegistry

interface DomainEventPublisher {
    suspend fun publishDomainEvents(domainEventRegistry: DomainEventRegistry)
}
