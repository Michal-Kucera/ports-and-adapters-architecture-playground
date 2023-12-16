package com.michalkucera.domain.sharedkernel.type

import com.michalkucera.domain.sharedkernel.event.DomainEvent

open class DomainEventRegistry {
    private val domainEvents = mutableListOf<DomainEvent>()

    protected fun registerDomainEvent(domainEvent: DomainEvent) {
        domainEvents += domainEvent
    }

    fun popDomainEvents(): Iterable<DomainEvent> {
        val domainEventsSnapshot = domainEvents.createSnapshot()
        domainEvents.clear()
        return domainEventsSnapshot
    }

    private fun <T> Iterable<T>.createSnapshot() = toList()
}
