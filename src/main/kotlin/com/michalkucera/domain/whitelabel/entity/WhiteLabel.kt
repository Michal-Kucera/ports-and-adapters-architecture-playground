package com.michalkucera.domain.whitelabel.entity

import com.michalkucera.domain.sharedkernel.type.DomainEventRegistry
import com.michalkucera.domain.whitelabel.event.WhiteLabelCreatedEvent
import com.michalkucera.domain.whitelabel.event.WhiteLabelNameChangedEvent
import com.michalkucera.domain.whitelabel.valueobject.WhiteLabelId
import com.michalkucera.domain.whitelabel.valueobject.WhiteLabelName
import org.jmolecules.ddd.types.AggregateRoot
import java.util.UUID

class WhiteLabel(
    override val id: WhiteLabelId,
    var name: WhiteLabelName
) : DomainEventRegistry(),
    AggregateRoot<WhiteLabel, WhiteLabelId> {
    companion object {
        fun create(
            name: String,
            description: String
        ) = WhiteLabel(
            id = WhiteLabelId(UUID.randomUUID()),
            name = WhiteLabelName(name, description)
        ).apply {
            registerDomainEvent(WhiteLabelCreatedEvent(id.whiteLabelId))
        }
    }

    fun changeName(
        newName: String,
        newDescription: String
    ) {
        name = WhiteLabelName(newName, newDescription)
        registerDomainEvent(WhiteLabelNameChangedEvent(id.whiteLabelId))
    }
}
