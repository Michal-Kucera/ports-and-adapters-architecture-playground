package com.michalkucera.domain.sharedkernel.event

import com.michalkucera.domain.whitelabel.valueobject.ResellerCompanyId
import com.michalkucera.domain.whitelabel.valueobject.WhiteLabelId

data class ResellerCompanyCreatedEvent(
    val resellerCompanyId: ResellerCompanyId,
    val whiteLabelId: WhiteLabelId
) : DomainEvent
