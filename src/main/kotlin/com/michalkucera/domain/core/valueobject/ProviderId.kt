package com.michalkucera.domain.core.valueobject

import org.intellij.lang.annotations.Identifier
import java.util.UUID

@JvmInline
@Identifier
value class ProviderId(val id: UUID)
