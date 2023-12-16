package com.michalkucera.domain.sharedkernel.event

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlin.reflect.KClass

class DomainEventBus<E : DomainEvent>(
    private val domainEventType: KClass<E>
) {
    companion object {
        // This inlined factory method that gets us the class object is inspired by https://stackoverflow.com/a/33158859/8765371
        inline operator fun <reified E : DomainEvent> invoke() = DomainEventBus(E::class)
    }

    private val subscribers = MutableSharedFlow<E>()

    fun supportsDomainEvent(domainEvent: DomainEvent): Boolean = domainEventType.java.isAssignableFrom(domainEvent.javaClass)

    suspend fun registerSubscriber(domainEventSubscriber: DomainEventSubscriber<E>) {
        GlobalScope.launch(Dispatchers.IO) {
            subscribers.collect {
                runCatching {
                    domainEventSubscriber.processDomainEvent(it)
                }.onFailure { e ->
                    println(
                        "Subscriber '${domainEventSubscriber::class.simpleName ?: "anonymous subscriber"}' couldn't process " +
                            "domain event '${it::class.simpleName}' due to an exception $e"
                    )
                }
            }
        }
    }

    suspend fun publish(domainEvent: E) = subscribers.emit(domainEvent)
}
