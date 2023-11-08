package com.michalkucera.application.port.input.whitelabel.fetchstrategy

import com.michalkucera.domain.whitelabel.entity.WhiteLabel

interface FetchWhiteLabelStrategy {

    fun fetchWhiteLabel(): WhiteLabel
}
