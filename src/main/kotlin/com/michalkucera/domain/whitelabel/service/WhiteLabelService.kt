package com.michalkucera.domain.whitelabel.service

import com.michalkucera.domain.whitelabel.entity.WhiteLabel

class WhiteLabelService {
    fun createNewWhiteLabel(
        name: String,
        description: String
    ): WhiteLabel = WhiteLabel.create(name, description)
}
