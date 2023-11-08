package com.michalkucera.application.port.output.whitelabel

import com.michalkucera.domain.whitelabel.entity.WhiteLabel
import com.michalkucera.domain.whitelabel.valueobject.WhiteLabelId

interface FetchWhiteLabelByIdOutputPort {

    fun fetchWhiteLabelById(whiteLabelId: WhiteLabelId): WhiteLabel?
}
