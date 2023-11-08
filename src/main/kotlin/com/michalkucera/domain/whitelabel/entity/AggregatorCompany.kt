package com.michalkucera.domain.whitelabel.entity

import com.michalkucera.domain.whitelabel.valueobject.AggregatorCompanyId
import com.michalkucera.domain.whitelabel.valueobject.AuditRecord
import com.michalkucera.domain.whitelabel.valueobject.WhiteLabelId
import org.jmolecules.ddd.annotation.Association
import org.jmolecules.ddd.annotation.Entity
import org.jmolecules.ddd.annotation.Identity

@Entity
data class AggregatorCompany(
    @Identity val aggregatorCompanyId: AggregatorCompanyId,
    val companyName: String,
    @Association(aggregateType = WhiteLabel::class) val whiteLabelId: WhiteLabelId,
    val auditRecord: AuditRecord
)
