package com.michalkucera.domain.sharedkernel.event

class EventBus<E : Event> {
    private val subscribers = mutableSetOf<EventSubscriber<E>>()

    fun subscribe(subscriber: EventSubscriber<E>) {
        subscribers += subscriber
    }

    fun publish(event: E) = publish(listOf(event))

    fun publish(events: Iterable<E>) = events.forEach { event ->
        subscribers.forEach { subscriber -> subscriber(event) }
    }
}

typealias EventSubscriber<E> = (E) -> Unit
