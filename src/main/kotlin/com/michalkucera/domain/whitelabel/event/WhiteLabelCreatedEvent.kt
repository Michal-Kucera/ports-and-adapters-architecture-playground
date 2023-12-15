package com.michalkucera.domain.whitelabel.event

import com.michalkucera.domain.sharedkernel.event.DomainEvent
import java.util.UUID

data class WhiteLabelCreatedEvent(
    val whiteLabelId: UUID
) : DomainEvent
