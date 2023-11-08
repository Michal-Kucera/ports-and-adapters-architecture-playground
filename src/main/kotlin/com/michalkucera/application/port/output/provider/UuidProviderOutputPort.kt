package com.michalkucera.application.port.output.provider

import java.util.UUID

interface UuidProviderOutputPort {
    fun getRandomUuid(): UUID
}
