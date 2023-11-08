package com.michalkucera.application.port.output.whitelabel

import com.michalkucera.domain.whitelabel.entity.WhiteLabel

interface FetchDefaultWhiteLabelOutputPort {

    fun fetchDefaultWhiteLabel(): WhiteLabel
}
