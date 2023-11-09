package com.michalkucera.application.port.input.whitelabel.fetchstrategy

import com.michalkucera.application.port.output.whitelabel.FetchWhiteLabelByIdOutputPort
import com.michalkucera.domain.whitelabel.valueobject.WhiteLabelId

class FetchWhiteLabelByIdStrategy(
    private val fetchWhiteLabelByIdOutputPort: FetchWhiteLabelByIdOutputPort,
    private val whiteLabelId: WhiteLabelId
) : FetchWhiteLabelStrategy {
    override fun fetchWhiteLabel() = fetchWhiteLabelByIdOutputPort.fetchWhiteLabelById(whiteLabelId)
        ?: throw WhiteLabelNotFoundException

    object WhiteLabelNotFoundException : Exception("White label not found")
}
