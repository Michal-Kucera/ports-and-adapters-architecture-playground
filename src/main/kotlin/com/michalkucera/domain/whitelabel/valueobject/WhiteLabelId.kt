package com.michalkucera.domain.whitelabel.valueobject

import org.jmolecules.ddd.types.Identifier
import java.util.UUID

@JvmInline
value class WhiteLabelId(
    val whiteLabelId: UUID
) : Identifier
