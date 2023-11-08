package com.michalkucera

import com.michalkucera.domain.sharedkernel.event.channel.ChannelEventBus
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test

class ChannelEventBusTest {

    @Test
    fun testChannel() = runBlocking {
        ChannelEventBus().testChannel()
    }

    @Test
    fun testSharedFlow() = runBlocking {
        ChannelEventBus().testSharedFlow()
    }
}
