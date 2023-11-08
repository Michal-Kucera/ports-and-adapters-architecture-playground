package com.michalkucera.domain.core.entity

import com.michalkucera.domain.core.valueobject.ProviderId
import org.jmolecules.ddd.annotation.AggregateRoot
import org.jmolecules.ddd.annotation.Identity

@AggregateRoot
data class Provider(
    @Identity val providerId: ProviderId
)
