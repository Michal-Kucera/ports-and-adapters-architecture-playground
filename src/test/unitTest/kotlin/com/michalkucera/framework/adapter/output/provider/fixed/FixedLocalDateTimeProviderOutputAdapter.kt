package com.michalkucera.framework.adapter.output.provider.fixed

import com.michalkucera.application.port.output.provider.LocalDateTimeProviderOutputPort
import java.time.LocalDateTime

class FixedLocalDateTimeProviderOutputAdapter(
    private val fixedLocalDateTime: LocalDateTime
) : LocalDateTimeProviderOutputPort {
    override fun getCurrentLocalDateTime() = fixedLocalDateTime
}
