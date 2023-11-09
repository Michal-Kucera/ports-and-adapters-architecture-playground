package com.michalkucera.application.port.output.provider

import java.time.LocalDateTime

interface LocalDateTimeProviderOutputPort {
    fun getCurrentLocalDateTime(): LocalDateTime
}
