package com.michalkucera.domain.core.entity

import com.michalkucera.domain.core.valueobject.AcquirerId
import org.jmolecules.ddd.annotation.AggregateRoot
import org.jmolecules.ddd.annotation.Identity

@AggregateRoot
data class Acquirer(
    @Identity val acquirerId: AcquirerId
)
