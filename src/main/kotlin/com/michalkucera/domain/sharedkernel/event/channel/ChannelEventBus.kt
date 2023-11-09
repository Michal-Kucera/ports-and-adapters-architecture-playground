package com.michalkucera.domain.sharedkernel.event.channel

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class ChannelEventBus {
    suspend fun testSharedFlow(): Unit = coroutineScope {
        val mutableSharedFlow = MutableSharedFlow<String>()
        val sharedFlow: SharedFlow<String> = mutableSharedFlow
        val collector: FlowCollector<String> = mutableSharedFlow

        launch {
            sharedFlow.collect {
                println("Receiver #1: $it")
            }
        }

        launch {
            collector.emit("Hello")
            collector.emit("Hola")
            collector.emit("Hi")
            collector.emit("Hey")
        }

        launch {
            sharedFlow.collect {
                println("Receiver #2: $it")
            }
        }
    }

    suspend fun testChannel(): Unit = coroutineScope {
        val channel = Channel<String>()
        val sendChannel = channel as SendChannel<String>
        val receiveChannel = channel as ReceiveChannel<String>

        launch {
            receiveChannel.consumeEach {
                println("Receiver 1: $it")
            }
        }

        launch {
            for (message in receiveChannel) {
                println("Receiver 2: $message")
            }
        }

        produce<String> {
            sendChannel.send("Hello 1")
            delay(3000)
            sendChannel.send("Hello 2")
            delay(3000)
            sendChannel.send("Hello 3")
            channel.close()
        }
    }
}
