package com.michalkucera.domain.sharedkernel.type

import com.michalkucera.domain.sharedkernel.event.DomainEvent
import com.michalkucera.domain.sharedkernel.event.DomainEventPublisher
import org.jmolecules.ddd.types.Identifier
import org.jmolecules.ddd.types.AggregateRoot as AggregateRootDdd

abstract class AggregateRoot<T : AggregateRoot<T, ID>, ID : Identifier> : AggregateRootDdd<T, ID> {
    private val domainEvents = mutableListOf<DomainEvent>()

    protected fun registerDomainEvent(domainEvent: DomainEvent) {
        domainEvents + domainEvent
    }

    suspend fun publishDomainEvents(domainEventPublisher: DomainEventPublisher) {
        domainEvents.forEach { domainEventPublisher.publishDomainEvent(it) }
        clearDomainEvents()
    }

    private fun clearDomainEvents() {
        domainEvents.clear()
    }
}
