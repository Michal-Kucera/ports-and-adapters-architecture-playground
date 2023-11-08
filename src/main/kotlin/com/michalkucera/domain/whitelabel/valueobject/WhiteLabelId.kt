package com.michalkucera.domain.whitelabel.valueobject

import org.intellij.lang.annotations.Identifier
import java.util.UUID

@JvmInline
@Identifier
value class WhiteLabelId(val id: UUID)
