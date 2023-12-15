package com.michalkucera.domain.sharedkernel.event

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.joinAll
import kotlin.reflect.KClass

class DomainEventBus<E : DomainEvent>(
    private val domainEventType: KClass<E>
) {
    companion object {
        // This inlined factory method that gets us the class object is inspired by https://stackoverflow.com/a/33158859/8765371
        inline operator fun <reified E : DomainEvent> invoke() = DomainEventBus(E::class)
    }

    private val subscribers = mutableSetOf<DomainEventSubscriber<E>>()

    fun supportsDomainEvent(domainEvent: DomainEvent): Boolean = domainEventType.java.isAssignableFrom(domainEvent.javaClass)

    fun subscribe(subscriber: DomainEventSubscriber<E>) {
        subscribers += subscriber
    }

    suspend fun publish(domainEvent: E) = coroutineScope {
        subscribers
            .map { domainEventSubscriber ->
                async {
                    runCatching {
                        domainEventSubscriber.processDomainEvent(domainEvent)
                    }.onFailure { e ->
                        println(
                            "Subscriber '${domainEventSubscriber::class.simpleName ?: "anonymous subscriber"}' couldn't process " +
                                "domain event '${domainEvent::class.simpleName}' due to an exception $e"
                        )
                    }
                }
            }
    }.joinAll()
}
