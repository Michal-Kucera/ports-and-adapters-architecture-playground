package com.michalkucera.domain.whitelabel.valueobject

import org.jmolecules.ddd.types.ValueObject

data class WhiteLabelName(
    val name: String,
    val description: String
) : ValueObject {
    init {
        require(name.isNotBlank()) { "Name cannot be blank" }
        require(description.isNotBlank()) { "Description cannot be blank" }
    }
}
