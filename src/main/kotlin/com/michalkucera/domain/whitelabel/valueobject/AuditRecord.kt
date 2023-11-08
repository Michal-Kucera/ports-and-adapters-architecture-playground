package com.michalkucera.domain.whitelabel.valueobject

import org.jmolecules.ddd.annotation.Factory
import org.jmolecules.ddd.annotation.ValueObject
import java.time.LocalDateTime

@ValueObject
data class AuditRecord(
    val creationDateTime: LocalDateTime,
    val lastModificationDateTime: LocalDateTime,
    val deletionDateTime: LocalDateTime?
)

@Factory
object AuditRecordFactory {

    fun createdRecord(creationDateTime: LocalDateTime) = AuditRecord(creationDateTime, creationDateTime, null)
}
