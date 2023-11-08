package com.michalkucera.framework.adapter.output.whitelabel

import com.michalkucera.application.port.output.whitelabel.FetchDefaultWhiteLabelOutputPort
import com.michalkucera.application.port.output.whitelabel.FetchWhiteLabelByIdOutputPort
import com.michalkucera.domain.whitelabel.valueobject.WhiteLabelId

class InMemoryWhiteLabelByIdPersistenceAdapter(
    private val database: InMemoryWhiteLabelDatabase
) : FetchWhiteLabelByIdOutputPort, FetchDefaultWhiteLabelOutputPort {

    override fun fetchWhiteLabelById(
        whiteLabelId: WhiteLabelId
    ) = database.firstOrNull { it.whiteLabelId == whiteLabelId }

    override fun fetchDefaultWhiteLabel() = database.first()
}
