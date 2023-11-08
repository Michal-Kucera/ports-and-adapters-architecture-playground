package com.michalkucera.framework.adapter.output.provider.fixed

import com.michalkucera.application.port.output.provider.UuidProviderOutputPort
import java.util.UUID

class FixedUuidProviderOutputAdapter(
    private val fixedUuid: UUID
) : UuidProviderOutputPort {

    override fun getRandomUuid() = fixedUuid
}
