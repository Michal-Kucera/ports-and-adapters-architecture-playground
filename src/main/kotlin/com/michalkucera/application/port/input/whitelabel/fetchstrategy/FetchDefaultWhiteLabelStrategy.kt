package com.michalkucera.application.port.input.whitelabel.fetchstrategy

import com.michalkucera.application.port.output.whitelabel.FetchDefaultWhiteLabelOutputPort

class FetchDefaultWhiteLabelStrategy(
    private val fetchDefaultWhiteLabelOutputPort: FetchDefaultWhiteLabelOutputPort
) : FetchWhiteLabelStrategy {

    override fun fetchWhiteLabel() = fetchDefaultWhiteLabelOutputPort.fetchDefaultWhiteLabel()
}
