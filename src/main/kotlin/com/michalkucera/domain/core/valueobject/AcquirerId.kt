package com.michalkucera.domain.core.valueobject

import org.intellij.lang.annotations.Identifier
import java.util.UUID

@JvmInline
@Identifier
value class AcquirerId(
    val id: UUID
)
